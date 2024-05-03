package de.fraunhofer.sit.sse.valbench;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import de.fraunhofer.sit.sse.valbench.metadata.TestCaseManager;
import de.fraunhofer.sit.sse.valbench.metadata.interfaces.ITestSuite;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.options.Options;

public class TestUtils {

	public static void loadAll() {

		for (ITestSuite t : TestCaseManager.getAllTestSuites()) {
			Scene.v().addBasicClass(t.getTestSuiteClass().getName(),SootClass.BODIES);
		}
		Scene.v().addBasicClass("de.fraunhofer.sit.sse.valbench.base.MainActivity",SootClass.BODIES);
		Scene.v().loadNecessaryClasses();
		boolean changed = true;
		while (changed) {
			changed = false;
			for (SootClass c : new ArrayList<>( Scene.v().getLibraryClasses())) {
				if (c.resolvingLevel() < SootClass.SIGNATURES) {
					changed = true;
					Scene.v().forceResolve(c.getName(), SootClass.SIGNATURES);
				}
			}
			for (SootClass c : new ArrayList<>( Scene.v().getApplicationClasses())) {
				if (c.resolvingLevel() < SootClass.BODIES) {
					changed = true;
					Scene.v().forceResolve(c.getName(), SootClass.BODIES);
					for (SootMethod i : c.getMethods())
						if (i.isConcrete())
							i.retrieveActiveBody();
				}
			}
		}		
	}

	public static void setProcessDir() {
		File apk = new File("../valbenchbaseapk/app/build/outputs/apk/debug/app-debug.apk");
		File tgt = new File("target/classes");
		if (!apk.exists())
			throw new RuntimeException("Does not exist: " + apk.getAbsolutePath());
		if (!tgt.exists())
			throw new RuntimeException("Does not exist: " + tgt.getAbsolutePath());
		new File(tgt, "de/fraunhofer/sit/sse/valbench/base/MainActivity.class").delete();
		new File(tgt, "de/fraunhofer/sit/sse/valbench/base/R.class").delete();
		new File(tgt, "de/fraunhofer/sit/sse/valbench/base/R$id.class").delete();
		new File(tgt, "de/fraunhofer/sit/sse/valbench/base/R$layout.class").delete();
		new File(tgt, "de/fraunhofer/sit/sse/valbench/base/base/R$raw.class").delete();
		Options.v().set_process_dir(Arrays.asList(apk.getAbsolutePath(), tgt.getAbsolutePath()));
	}

}
