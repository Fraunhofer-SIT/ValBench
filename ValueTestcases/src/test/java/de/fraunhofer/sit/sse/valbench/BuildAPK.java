package de.fraunhofer.sit.sse.valbench;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

import com.google.gson.Gson;

import de.fraunhofer.sit.sse.valbench.SootUtils;
import de.fraunhofer.sit.sse.valbench.metadata.TestCaseManager;
import de.fraunhofer.sit.sse.valbench.metadata.TestEntrypoint;
import de.fraunhofer.sit.sse.valbench.metadata.ValueComputationTestCase;
import de.fraunhofer.sit.sse.valbench.metadata.interfaces.ITestCase;
import de.fraunhofer.sit.sse.valbench.metadata.interfaces.ITestSuite;
import soot.ArrayType;
import soot.Body;
import soot.Local;
import soot.PackManager;
import soot.RefType;
import soot.Scene;
import soot.SootClass; 
import soot.SootMethod;
import soot.VoidType;
import soot.jimple.IdentityStmt;
import soot.jimple.Jimple;
import soot.options.Options;

public class BuildAPK {
	@Test
	public void build() throws IOException {
		Options.v().set_allow_phantom_refs(true);
		Options.v().set_prepend_classpath(true);
		Options.v().set_process_multiple_dex(true);
		Options.v().set_src_prec(Options.src_prec_apk_class_jimple);
		Options.v().set_output_format(Options.output_format_dex);
		Options.v().set_output_dir("target/");
		TestUtils.setProcessDir();
		TestUtils.loadAll();

		SootClass ma = Scene.v().forceResolve("de.fraunhofer.sit.sse.valbench.base.MainActivity", SootClass.BODIES);
		Scene.v().removeClass(Scene.v().getSootClass("de.fraunhofer.sit.sse.valbench.MainClass"));

		SootMethod mEntryPointOnCreate = Scene.v().getSootClass("de.fraunhofer.sit.sse.valbench.base.MainActivity").getMethodByName("startTests");
		Body body = mEntryPointOnCreate.retrieveActiveBody();
		EntryPointCreator androidCreator = new EntryPointCreator(mEntryPointOnCreate);

		for (Class<?> t : TestCaseManager.ALL_CLASSES) {
			for (Method m : t.getDeclaredMethods()) {
				boolean isEntryPoint = false;
				ValueComputationTestCase d = m.getDeclaredAnnotation(ValueComputationTestCase.class);
				if (d != null)
				{
					if (d.noEntryPoint()) {
						continue;
					}
					isEntryPoint= true;
				}
				if (m.getDeclaredAnnotation(TestEntrypoint.class) != null) {
					isEntryPoint= true;
				}

				if (isEntryPoint) {
					if (m.getParameterCount() > 0)
						throw new RuntimeException("Did expect no parameter: " + m);
					String sootMethod = SootUtils.getSootSignature(m);
					androidCreator.addEntryPoint(sootMethod, true);
				}
			}

		}
		Jimple j = Jimple.v();
		Local l = j.newLocal("exc", RefType.v("java.lang.Throwable"));
		body.getLocals().add(l);
		body.getUnits().add(j.newNopStmt());
		IdentityStmt handler = j.newIdentityStmt(l, j.newCaughtExceptionRef());
		body.getTraps().add(j.newTrap(Scene.v().getSootClass("java.lang.Throwable"), body.getUnits().getSuccOf(body.getUnits().getFirst()), body.getUnits().getLast(), handler));
		body.getUnits().add(j.newReturnVoidStmt());
		body.getUnits().add(handler);
		body.getUnits().add(j.newInvokeStmt(j.newSpecialInvokeExpr(body.getThisLocal(), ma.getMethodByName("handleException").makeRef(), Arrays.asList(l))));
		body.getUnits().add(j.newReturnVoidStmt());

		
		androidCreator.finish();

		writeout(new File("target/valbench-testcases-android.apk"));
	}

	private void writeout(File dest) throws IOException {
		Options.v().set_force_overwrite(true);
		PackManager.v().writeOutput();
		dest.delete();
		FileUtils.moveFile(new File("target/app-debug.apk"), dest);
	}

}
