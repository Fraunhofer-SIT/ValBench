package de.fraunhofer.sit.sse.valbench;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import soot.Body;
import soot.Local;
import soot.PrimType;
import soot.RefType;
import soot.Scene;
import soot.SootMethod;
import soot.Type;
import soot.Unit;
import soot.VoidType;
import soot.jimple.IdentityStmt;
import soot.jimple.InvokeStmt;
import soot.jimple.Jimple;
import soot.jimple.JimpleBody;
import soot.jimple.NopStmt;
import soot.jimple.ReturnStmt;

class ExplicitLoggingPointCreator {


	public static void createExplicitLP(SootMethod sootMethod) {
		if (sootMethod.getReturnType() instanceof VoidType)
			return;
		SootMethod m = Scene.v().grabMethod("<de.fraunhofer.sit.sse.valbench.ExplicitLoggingPoint: void explicitLoggingPoint(java.lang.Object)>");
		if (m.isPhantom())
			throw new RuntimeException("Not found " + m.getSignature());
		Body body = sootMethod.retrieveActiveBody();
		Iterator<Unit> it = body.getUnits().snapshotIterator();
		while (it.hasNext()) {
			Unit u = it.next();
			if (u instanceof ReturnStmt) {
				ReturnStmt ret = (ReturnStmt) u;
				Jimple j = Jimple.v();
				if (ret.getOp().getType() instanceof PrimType) {
					PrimType t = (PrimType) ret.getOp().getType();
					SootMethod v= t.boxedType().getSootClass().getMethod("valueOf", Arrays.asList(t));
					Local l = j.newLocal("convert", t.boxedType());
					body.getLocals().add(l);
					InvokeStmt nw = j.newInvokeStmt(j.newStaticInvokeExpr(m.makeRef(), Arrays.asList(l)));
					body.getUnits().insertBefore(nw, u);
					body.getUnits().insertBefore(j.newAssignStmt(l, j.newStaticInvokeExpr(v.makeRef(), Arrays.asList(ret.getOp()))), nw);
				} else
					body.getUnits().insertBefore(j.newInvokeStmt(j.newStaticInvokeExpr(m.makeRef(), Arrays.asList(ret.getOp()))), u);
			}
		}
	}

	
}