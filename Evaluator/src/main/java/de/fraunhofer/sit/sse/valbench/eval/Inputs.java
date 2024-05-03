package de.fraunhofer.sit.sse.valbench.eval;

import java.io.File;

public class Inputs {

	public final File jar, apk, singleTestCaseJars, rtJar, java8;

	public Inputs() {
		File base = new File(EvalMain.basedir, "../ValueTestcases/target");
		File jarFile = null;
		for (File f : base.listFiles()) {
			if (f.getName().startsWith("valbench-testcases") && f.getName().endsWith(".jar")) {
				jarFile = f;
			}
		}
		if (jarFile == null)
			throw new RuntimeException(String.format("ValBench jar file not found at %s", base.getAbsolutePath()));
		jar = jarFile;
		apk = new File(base, "valbench-testcases-android.apk");
		singleTestCaseJars = new File(base, "single-testcase-jars");
		
		//StringHound only runs on Java 8
		java8 = getEnv("JAVA8", "/usr/lib/jvm/java-8-openjdk-amd64/bin/java");
		
		File rtJarFile = getEnv("RT_JAR", "/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/rt.jar");
		if (rtJarFile == null) {
			rtJarFile = new File(java8.getParentFile().getParentFile(), "lib/rt.jar");
		}
		if (!rtJarFile.exists())
			throw new RuntimeException(String.format("rt.jar not found under %s", rtJarFile.getAbsolutePath()));
		rtJar = rtJarFile;
		for (File f : new File[] { jar, apk, singleTestCaseJars,rtJar  }) {
			if (!f.exists())
				throw new RuntimeException(String.format("%s does not exist", f.getAbsolutePath()));
		}
	}

	private File getEnv(String env, String defaultVal) {
		String e = System.getenv(env);
		if (e == null)
			e = defaultVal;
		return new File(e);
	}

	public File[] getSingleTestCaseJars() {
		return singleTestCaseJars.listFiles();
	}
	
}
