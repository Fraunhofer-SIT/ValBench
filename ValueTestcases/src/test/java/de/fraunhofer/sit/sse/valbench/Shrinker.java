package de.fraunhofer.sit.sse.valbench;

import java.lang.reflect.Field;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.base.Optional;

import soot.ArrayType;
import soot.Body;
import soot.BooleanType;
import soot.DoubleType;
import soot.FloatType;
import soot.IntType;
import soot.IntegerType;
import soot.LongType;
import soot.PackManager;
import soot.RefLikeType;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.VoidType;
import soot.asm.AsmUtil;
import soot.jimple.AssignStmt;
import soot.jimple.ClassConstant;
import soot.jimple.DoubleConstant;
import soot.jimple.DynamicInvokeExpr;
import soot.jimple.FloatConstant;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InstanceOfExpr;
import soot.jimple.IntConstant;
import soot.jimple.InvokeExpr;
import soot.jimple.Jimple;
import soot.jimple.JimpleBody;
import soot.jimple.LongConstant;
import soot.jimple.NullConstant;
import soot.jimple.SpecialInvokeExpr;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.Stmt;
import soot.options.Options;
import soot.toDex.DexPrinter;

public class Shrinker {
	private Set<SootMethod> keep = new HashSet<>();
	Set<SootClass> keepClasses = new HashSet<>();
	public Set<String> keepClassesOriginal = new HashSet<>();

	public void addKeep(SootMethod m) {
		keep.add(m);
	}
	
	public void addKeepClassCompletely(SootClass c) {
		keepClasses.add(c);
		keep.addAll(c.getMethods());
	}

	public void write() throws Exception {
		TestUtils.loadAll();
		doShrink();
		for (SootMethod i : keep) {
			keepClasses.add(i.getDeclaringClass());
		}
		ArrayDeque<SootClass> queue = new ArrayDeque<>(keepClasses);
		Set<SootClass> seen = new HashSet<>();
		while (true) {
			SootClass p = queue.poll();
			if (!seen.add(p))
				continue;
			if (p == null)
				break;
			keepClasses.add(p);
			for (SootMethod d : p.getMethods()) {
				for (Type pd : d.getParameterTypes()) {
					RefType rt = getReferenceType(pd);
					if (rt != null)
						queue.add(rt.getSootClass());
					
				}
				RefType rt = getReferenceType(d.getReturnType());
				if (rt != null)
					queue.add(rt.getSootClass());
				List<SootClass> l = d.getExceptionsUnsafe();
				if (l != null) {
					queue.addAll(l);
				}
			}
			for (SootField d : p.getFields()) {
				Type t = d.getType();
				RefType rt = getReferenceType(t);
				if (rt != null)
					queue.add(rt.getSootClass());
			}
			queue.addAll(p.getInterfaces());
			if (p.hasSuperclass()) {
				queue.add(p.getSuperclass());
			}
		}
		Set<SootClass> phantomizedClass = new HashSet<>();
		Map<SootMethod,Body> phantomized = new HashMap<>();
		try {
			Jimple j = Jimple.v();
			for (SootClass i : new ArrayList<>(Scene.v().getApplicationClasses())) {
				if (!keepClasses.contains(i)) {
					i.setPhantomClass();
					phantomizedClass.add(i);
					continue;
				}
				for (SootMethod m : i.getMethods()) {
					if (!keep.contains(m) && m.isConcrete()) {
						phantomized.put(m, m.retrieveActiveBody());
						JimpleBody b = j.newBody(m);
						b.insertIdentityStmts();
						if (m.getReturnType() instanceof VoidType) {
							b.getUnits().add(j.newReturnVoidStmt());
						} else {
							if (m.getReturnType() instanceof FloatType)
								b.getUnits().add(j.newReturnStmt(FloatConstant.v(0)));
							else if (m.getReturnType() instanceof DoubleType)
								b.getUnits().add(j.newReturnStmt(DoubleConstant.v(0)));
							else if (m.getReturnType() instanceof LongType)
								b.getUnits().add(j.newReturnStmt(LongConstant.v(0)));
							else if (m.getReturnType() instanceof IntegerType)
								b.getUnits().add(j.newReturnStmt(IntConstant.v(0)));
							else if (m.getReturnType() instanceof RefLikeType)
								b.getUnits().add(j.newReturnStmt(NullConstant.v()));
							else
								throw new RuntimeException("Unsupported: " + m.getReturnType());
						}
						m.setActiveBody(b);
					}
				}
			}
			Options.v().set_no_writeout_body_releasing(true);
			PackManager.v().writeOutput();
		} finally {
			for (SootClass i : phantomizedClass) {
				i.setApplicationClass();
			}
			for (Entry<SootMethod, Body> d : phantomized.entrySet()) {
				d.getKey().setActiveBody(d.getValue());
			}
		}
	}

	private RefType getReferenceType(Type t) {

		if (t instanceof ArrayType) {
			Type pt = ((ArrayType) t).getElementType();
			if (!(pt instanceof RefType))
				return null;
			return (RefType) pt;
		}
		else if (t instanceof RefType)
			return (RefType) t;
		return null;
	}

	private void doShrink() {
		ArrayDeque<SootMethod> queue = new ArrayDeque<>(keep);
		keep.clear();
		while (true) {
			SootMethod p = queue.poll();
			if (p == null)
				break;

			if (!p.getDeclaringClass().isApplicationClass() || !keep.add(p))
				continue;

			for (SootMethod i : p.getDeclaringClass().getMethods()) {
				if (i.isStaticInitializer() || i.isConstructor())
					queue.add(i);
			}
			
			if (p.isConcrete()) {
				Body body = p.retrieveActiveBody();
				for (Unit u : body.getUnits()) {
					Stmt s = (Stmt) u;
					for (ValueBox d : u.getUseBoxes()) {
						if (d.getValue() instanceof ClassConstant) {
							ClassConstant c = (ClassConstant) d.getValue();
							Type type = AsmUtil.toBaseType(c.getValue(), Optional.absent());
							if (type instanceof RefType) {
								RefType tt = (RefType) type;
								SootClass sc = tt.getSootClass();
								if (sc.isApplicationClass() && sc.getName().startsWith("de.fraunhofer.sit.sse.valbench"))
								{
									for (SootMethod sm : sc.getMethods()) {
										queue.add(sm);
									}
								}
							}
						}
					}
					if (s.containsFieldRef()) {
						SootClass f = s.getFieldRef().getField().getDeclaringClass();
						keepClasses.add(f);
						for (SootMethod d : f.getMethods()) {
							if (d.isStaticInitializer())
								queue.add(d);
						}
					}
					if (s instanceof AssignStmt) {
						AssignStmt assign = (AssignStmt) s;
						if (assign.getRightOp() instanceof InstanceOfExpr) {
							InstanceOfExpr inst = (InstanceOfExpr) assign.getRightOp();
							RefType t = getReferenceType(inst.getCheckType());
							if (t != null)
								keepClasses.add(t.getSootClass());
						}
					}
					if (s.containsInvokeExpr()) {
						InvokeExpr expr = s.getInvokeExpr();
						if (expr.getMethodRef().declaringClass().getName().contains("$lambda")) {
							keepOriginal(queue, p.getDeclaringClass());
							continue;
						}
						if (expr instanceof SpecialInvokeExpr || expr instanceof StaticInvokeExpr) {
							queue.add(expr.getMethod());
						} else if (expr instanceof DynamicInvokeExpr) {
							DynamicInvokeExpr die = (DynamicInvokeExpr) expr;
							queue.add(die.getMethod());
							queue.add(die.getBootstrapMethodRef().resolve());

							keepOriginal(queue, p.getDeclaringClass());
							keepOriginal(queue, die.getMethod().getDeclaringClass());
							keepOriginal(queue, die.getBootstrapMethodRef().getDeclaringClass());
							
						} else {
							InstanceInvokeExpr e = (InstanceInvokeExpr) expr;
							SootMethod mm = expr.getMethod();
							Value t = e.getBase();
							if (t.getType() instanceof ArrayType) {
								queue.add(mm);
								continue;
							}
							RefType rt = (RefType) t.getType();
							for (SootMethod d : Scene.v().getOrMakeFastHierarchy().resolveAbstractDispatch(rt.getSootClass(), mm)) {
								
								if (rt.getClassName().startsWith("java.") ||  rt.getClassName().startsWith("javax.") || rt.getClassName().startsWith("android."))
								{
									if (!d.getDeclaringClass().getPackageName().startsWith(p.getDeclaringClass().getPackageName()))
										continue;
								}
								queue.add(d);
							}
						}
					}
				}
			}
		}
	}

	private void keepOriginal(ArrayDeque<SootMethod> queue, SootClass declaringClass) {
		keepClassesOriginal.add(declaringClass.getName());
		queue.addAll(declaringClass.getMethods());
	}
}
