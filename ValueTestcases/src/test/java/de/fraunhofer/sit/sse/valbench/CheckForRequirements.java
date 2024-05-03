package de.fraunhofer.sit.sse.valbench;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.google.gson.Gson;

import de.fraunhofer.sit.sse.valbench.SootUtils;
import de.fraunhofer.sit.sse.valbench.metadata.TestCaseManager;
import de.fraunhofer.sit.sse.valbench.metadata.impl.TestSuite;
import de.fraunhofer.sit.sse.valbench.metadata.impl.requirements.AndroidRequirement;
import de.fraunhofer.sit.sse.valbench.metadata.impl.requirements.ArithmeticRequirement;
import de.fraunhofer.sit.sse.valbench.metadata.impl.requirements.ArrayRequirement;
import de.fraunhofer.sit.sse.valbench.metadata.impl.requirements.CachedRequirements;
import de.fraunhofer.sit.sse.valbench.metadata.impl.requirements.DynamicInvokeRequirement;
import de.fraunhofer.sit.sse.valbench.metadata.impl.requirements.FieldHandlingRequirement;
import de.fraunhofer.sit.sse.valbench.metadata.impl.requirements.IfRequirement;
import de.fraunhofer.sit.sse.valbench.metadata.impl.requirements.InterproceduralRequirement;
import de.fraunhofer.sit.sse.valbench.metadata.impl.requirements.LoopRequirement;
import de.fraunhofer.sit.sse.valbench.metadata.impl.requirements.ModelledAPIFieldRequirement;
import de.fraunhofer.sit.sse.valbench.metadata.impl.requirements.ModelledAPIMethodRequirement;
import de.fraunhofer.sit.sse.valbench.metadata.impl.requirements.PrimitiveTypeRequirement;
import de.fraunhofer.sit.sse.valbench.metadata.impl.requirements.RecursionRequirement;
import de.fraunhofer.sit.sse.valbench.metadata.impl.requirements.ReflectionRequirement;
import de.fraunhofer.sit.sse.valbench.metadata.impl.requirements.StreamsRequirement;
import de.fraunhofer.sit.sse.valbench.metadata.interfaces.IRequirement;
import de.fraunhofer.sit.sse.valbench.metadata.interfaces.ITestCase;
import de.fraunhofer.sit.sse.valbench.metadata.interfaces.ITestSuite;
import soot.Body;
import soot.FastHierarchy;
import soot.PackManager;
import soot.PrimType;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.jimple.AssignStmt;
import soot.jimple.BinopExpr;
import soot.jimple.ConditionExpr;
import soot.jimple.DynamicInvokeExpr;
import soot.jimple.FieldRef;
import soot.jimple.IfStmt;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.SpecialInvokeExpr;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.toolkits.annotation.logic.Loop;
import soot.jimple.toolkits.annotation.logic.LoopFinder;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.thread.mhp.LoopBodyFinder;
import soot.options.Options;

public class CheckForRequirements {
	@Test
	public void createRequirementsFile() throws FileNotFoundException {
		Options.v().set_allow_phantom_refs(true);
		Options.v().set_prepend_classpath(true);
		Options.v().set_process_dir(Arrays.asList("target/classes"));
		for (ITestSuite t : TestCaseManager.getAllTestSuites()) {
			Scene.v().addBasicClass(t.getTestSuiteClass().getName(),SootClass.BODIES);
		}
		Scene.v().loadNecessaryClasses();
		loadLibs();
		
		List<CachedRequirements> cachedRequirements = new ArrayList<>();
		for (ITestCase t : TestCaseManager.getAllTestCases()) {
			String sootMethod = SootUtils.getSootSignature(t.getTestCaseMethod());
			SootMethod m = Scene.v().grabMethod(sootMethod);
			if (m == null)
				throw new RuntimeException("Could not find " + sootMethod);
			Set<IRequirement> requirements = new HashSet<>();
			
			Set<SootMethod> seen = new HashSet<>();
			searchForRequirements(m, requirements, seen);
			CachedRequirements cd = new CachedRequirements(t.getSignature(), requirements);
			cachedRequirements.add(cd);
			
			System.out.println(t.getSignature() + ":");
			System.out.println(requirements);
			System.out.println();
			
		}
		Gson gson = CachedRequirements.createGSON();
		String s = gson.toJson(cachedRequirements);
		gson.fromJson(s, CachedRequirements[].class);
		try (PrintWriter pw = new PrintWriter(new File("target/classes/Requirements.json"))) {
			pw.print(s);
		}
	}

	private static void loadLibs() {
		boolean changed = true;
		while (changed) {
			changed = false;
			for (SootClass c : new ArrayList<>( Scene.v().getLibraryClasses())) {
				if (c.resolvingLevel() < SootClass.SIGNATURES) {
					changed = true;
					Scene.v().forceResolve(c.getName(), SootClass.SIGNATURES);
				}
			}
		}
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		//This main method creates the requirements for the JSA test cases
		if (args.length == 0 || !new File(args[0]).exists())
			throw new RuntimeException("Give the path to the class files as arguments");
		

		List<CachedRequirements> cachedRequirements = new ArrayList<>();
		Options.v().set_allow_phantom_refs(true);
		Options.v().set_prepend_classpath(true);
		Options.v().set_process_dir(Arrays.asList(args[0]));
		Scene.v().loadNecessaryClasses();
		loadLibs();
		for (SootClass d : Scene.v().getApplicationClasses()) {
			for (SootMethod m : d.getMethods()) {
				if (m.isConcrete()) {
					Body b = m.retrieveActiveBody();
					for (Unit u : b.getUnits()) {
						Stmt s = (Stmt) u;
						if (s.containsInvokeExpr() && s.getInvokeExpr().getMethod().getSignature().equals("<testcases.tools.StringTest: void analyze(java.lang.String,java.lang.String,java.lang.String,java.lang.String)>")) {
							
							Set<IRequirement> requirements = new HashSet<>();
							
							Set<SootMethod> seen = new HashSet<>();
							b.getUnits().remove(s);
							searchForRequirements(m, requirements, seen);
							CachedRequirements cd = new CachedRequirements(m.getSignature(), requirements);
							cachedRequirements.add(cd);
							break;
						}
					}
				}
			}
		}
		Gson gson = CachedRequirements.createGSON();
		String s = gson.toJson(cachedRequirements);
		try (PrintWriter pw = new PrintWriter(new File("JSA-Testcase-Requirements.json"))) {
			pw.print(s);
		}
	}

	private static void searchForRequirements(SootMethod m, Set<IRequirement> requirements, Set<SootMethod> seen) {
		if (m.getDeclaringClass().getName().startsWith(TestCaseManager.class.getPackage().getName()))
			return;
		if (!m.isConcrete() || !m.getDeclaringClass().isApplicationClass())
			return;
		if (!seen.add(m)) {
			if (m.getDeclaringClass().getName().endsWith("ObjHolder"))
				return;
			requirements.add(new RecursionRequirement());
			return;
		}
		Body body = m.retrieveActiveBody();
		LoopFinder lp = new LoopFinder();
		Set<Loop> loops = lp.getLoops(body);
		if (loops != null && !loops.isEmpty()) {
			requirements.add(new LoopRequirement());
		}
		FastHierarchy fh = Scene.v().getOrMakeFastHierarchy();
		next:
		for (Unit i : body.getUnits()) {
			Stmt s = (Stmt) i;
			if (s instanceof IfStmt) {
				IfStmt ifstmt = (IfStmt) s;
				Set<Unit> allStatementsInLoop = new HashSet<>();
				for (Loop l : loops) {
					allStatementsInLoop.addAll(l.getLoopStatements());
					if (l.getHead() == ifstmt || l.getBackJumpStmt() == ifstmt)
						continue next;
				}
				if (allStatementsInLoop.contains(ifstmt) != allStatementsInLoop.contains(ifstmt.getTarget()))
					; //jump from or to loop counts as a loop if
				else
					requirements.add(new IfRequirement());
			}
			if (s instanceof AssignStmt) {
				Value rop = ((AssignStmt) s).getRightOp();
				if (rop instanceof BinopExpr) {
					if (!(rop instanceof ConditionExpr))
						requirements.add(new ArithmeticRequirement());
				}
				if (rop.getType() instanceof PrimType) {
					PrimType p = (PrimType) rop.getType();
					requirements.add(new PrimitiveTypeRequirement( p.getJavaPrimitiveType()));
				}
			}
			if (s.containsFieldRef()) {
				FieldRef fr = s.getFieldRef();
				if (fr.getField().getDeclaringClass().isApplicationClass())
					requirements.add(new FieldHandlingRequirement());
				else
					requirements.add(new ModelledAPIFieldRequirement(fr.getField().getDeclaringClass().getName() ,fr.getField().getSignature()));
				if (fr.getField().getDeclaringClass().getName().startsWith("android."))
					requirements.add(new AndroidRequirement());
			} else if (s.containsInvokeExpr()) {
				InvokeExpr inv = s.getInvokeExpr();
				if (fh.canStoreType(inv.getMethod().getDeclaringClass().getType(), RefType.v("java.io.InputStream"))) {
					requirements.add(new StreamsRequirement());
				}
				if (fh.canStoreType(inv.getMethod().getDeclaringClass().getType(), RefType.v("java.io.OutputStream"))) {
					requirements.add(new StreamsRequirement());
				}
				if (inv.getMethod().getDeclaringClass().getName().startsWith("java.lang.reflect."))
					requirements.add(new ReflectionRequirement());
				if (inv instanceof DynamicInvokeExpr) {
					requirements.add(new DynamicInvokeRequirement(s.toString()));
					continue;
				}
				SootMethod method = inv.getMethod();
				if (method.getDeclaringClass().getName().startsWith("android."))
					requirements.add(new AndroidRequirement());
				if (method.getDeclaringClass().isApplicationClass())
					requirements.add(new InterproceduralRequirement());
				else
					requirements.add(new ModelledAPIMethodRequirement(method.getDeclaringClass().getName(), method.getSignature()));
				
				if (inv instanceof StaticInvokeExpr || inv instanceof SpecialInvokeExpr)
					searchForRequirements(inv.getMethod(), requirements, new HashSet<>(seen));
				else {
					InstanceInvokeExpr inst = (InstanceInvokeExpr) inv;
					Type t = inst.getBase().getType();
					if (t instanceof RefType) {
						RefType rt =(RefType) t;
						for (SootMethod me : Scene.v().getOrMakeFastHierarchy().resolveAbstractDispatch(rt.getSootClass(), inv.getMethodRef()))
							searchForRequirements(me, requirements, new HashSet<>(seen));
					} else
						searchForRequirements(inv.getMethod(), requirements, new HashSet<>(seen));
				}
			} else if (s.containsArrayRef())
				requirements.add(new ArrayRequirement());
		}
	}
	
}
