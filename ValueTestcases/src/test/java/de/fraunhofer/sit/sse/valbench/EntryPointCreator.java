package de.fraunhofer.sit.sse.valbench;

import java.util.HashMap;
import java.util.Map;

import soot.Body;
import soot.Local;
import soot.RefType;
import soot.Scene;
import soot.SootMethod;
import soot.Type;
import soot.Unit;
import soot.jimple.IdentityStmt;
import soot.jimple.Jimple;
import soot.jimple.JimpleBody;
import soot.jimple.NopStmt;

class EntryPointCreator {

	private Body body;
	private Jimple j = Jimple.v();
	private Map<Type, Local> lcl = new HashMap<>();
	private SootMethod method;

	public EntryPointCreator(SootMethod mEntryPoint) {
		this.method = mEntryPoint;
		body = mEntryPoint.retrieveActiveBody();
		body.getUnits().removeLast();
	}

	public void addEntryPoint(String sootMethod, boolean canRunWithoutErrors) {
		SootMethod m = Scene.v().getMethod(sootMethod);
		if (!canRunWithoutErrors) {
			NopStmt after = j.newNopStmt();
			body.getUnits().add(j.newGotoStmt(after));
			Local l = j.newLocal("exc", RefType.v("java.lang.Throwable"));
			body.getLocals().add(l);
			Unit bef = body.getUnits().getLast();
			insertCall(m);
			IdentityStmt handler = j.newIdentityStmt(l, j.newCaughtExceptionRef());
			body.getUnits().add(handler);
			body.getUnits().add(after);
			body.getTraps().add(j.newTrap(Scene.v().getSootClass("java.lang.Throwable"), bef, body.getUnits().getLast(), handler));
		} else
			insertCall(m);
	}

	private void insertCall(SootMethod m) {
		if (!m.isPublic())
			throw new RuntimeException("Illegal modifiers: Must be public: " + m.getSignature());
		if (m.isStatic()) {
			body.getUnits().addLast(j.newInvokeStmt(j.newStaticInvokeExpr(m.makeRef())));
		} else {
			Type t = m.getDeclaringClass().getType();
			Local l = lcl.get(t);
			if (l == null) {
				l = j.newLocal("lcl", t);
				body.getLocals().add(l);
				body.getUnits().add(j.newAssignStmt(l, j.newNewExpr((RefType) t)));
				SootMethod ctor = m.getDeclaringClass().getMethod("void <init>()");
				body.getUnits().add(j.newInvokeStmt(j.newSpecialInvokeExpr(l, ctor.makeRef())));
			}
			body.getUnits().addLast(j.newInvokeStmt(j.newVirtualInvokeExpr(l, m.makeRef())));
			
		}
	}

	public void finish() {
		body.getUnits().add(j.newReturnVoidStmt());
		body.validate();
	}
	
}