package de.fraunhofer.sit.sse.valbench;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.junit.jupiter.api.Test;

import com.google.gson.Gson;

import de.fraunhofer.sit.sse.valbench.SootUtils;
import de.fraunhofer.sit.sse.valbench.metadata.Requires;
import de.fraunhofer.sit.sse.valbench.metadata.TestCaseManager;
import de.fraunhofer.sit.sse.valbench.metadata.TestEntrypoint;
import de.fraunhofer.sit.sse.valbench.metadata.ValueComputationTestCase;
import de.fraunhofer.sit.sse.valbench.metadata.interfaces.IResult;
import de.fraunhofer.sit.sse.valbench.metadata.interfaces.ITestCase;
import de.fraunhofer.sit.sse.valbench.metadata.interfaces.ITestSuite;
import polyglot.visit.DeadCodeEliminator;
import soot.ArrayType;
import soot.Body;
import soot.G;
import soot.Local;
import soot.PackManager;
import soot.PhaseOptions;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Unit;
import soot.VoidType;
import soot.jimple.IdentityStmt;
import soot.jimple.Jimple;
import soot.jimple.JimpleBody;
import soot.jimple.ReturnStmt;
import soot.jimple.Stmt;
import soot.jimple.toolkits.scalar.DeadAssignmentEliminator;
import soot.options.Options;
import soot.toolkits.scalar.UnusedLocalEliminator;

public class BuildSingleTests {
	static Set<String> allUsedFiles = new HashSet<>();
	private static File normalJar;
	private static File fat;
	static 
	{
		allUsedFiles.add("Requirements.json");
		allUsedFiles.add("GroundTruth.json");
		

		normalJar = null;  //target/valbench-testcases-0.0.1-SNAPSHOT.jar
		fat = null; //target/valbench-testcases-0.0.1-SNAPSHOT-fat.jar
		for (File d : new File("target").listFiles()) {
			if (d.getName().startsWith("valbench-testcases-") && d.getName().endsWith(".jar")) {
				if (d.getName().endsWith("-fat.jar"))
					continue;
				normalJar = d;
				String fatFilename = d.getName().substring(0, d.getName().length() - 4) + "-fat.jar";
				fat = new File(d.getParentFile(), fatFilename);
			}
		}
	}
	
	@Test
	public void build() throws Exception {
		Options.v().set_allow_phantom_refs(true);
		Options.v().set_prepend_classpath(true);
		Options.v().set_process_multiple_dex(true);
		Options.v().set_force_overwrite(true);
		Options.v().set_src_prec(Options.src_prec_apk_class_jimple);
		PhaseOptions.v().setPhaseOption("jb", "model-lambdametafactory:false");
		TestUtils.setProcessDir();
		TestUtils.loadAll();
		Scene.v().forceResolve("de.fraunhofer.sit.sse.valbench.MainClass", SootClass.BODIES);

		for (Class<?> t : TestCaseManager.ALL_CLASSES) {
			for (Method m : t.getDeclaredMethods()) {
				boolean isEntryPoint = isEntryPoint(m);

				if (isEntryPoint) {
					if (m.getParameterCount() > 0)
						throw new RuntimeException("Did expect no parameter: " + m);
					String sootMethod = SootUtils.getSootSignature(m);
					SootMethod sm = Scene.v().grabMethod(sootMethod);

					buildJARFor(m, sm, t);
					buildAPKFor(m, sm, t);
				}
			}

		}
		if (normalJar == null)
			throw new FileNotFoundException("valbench testcase jar not found in target folder");
		if (fat.exists())
			fat.delete();
		FileUtils.moveFile(normalJar, fat);
		try (ZipFile srcFile = new ZipFile(fat)) {
			try (ZipOutputStream destFile = new ZipOutputStream(Files.newOutputStream(Paths
					.get(normalJar
							.toURI())))) {
				//Copy from source
				Enumeration<? extends ZipEntry> entries = srcFile.entries();
				while (entries.hasMoreElements()) {
					ZipEntry src = entries.nextElement();
					if (!allUsedFiles.contains(src.getName()) && !src.getName().startsWith("de/fraunhofer/sit/sse/valbench"))
						continue;
					
					ZipEntry dest = new ZipEntry(src.getName());
					destFile.putNextEntry(dest);
					try (InputStream content = srcFile.getInputStream(src)) {
						IOUtils.copy(content, destFile);
					}
					destFile.closeEntry();
				}
				destFile.finish();
			}
		}
		
	}

	private boolean isEntryPoint(Method m) {
		boolean isEntryPoint = false;
		ValueComputationTestCase d = m.getDeclaredAnnotation(ValueComputationTestCase.class);
		if (d != null) {
			isEntryPoint = !d.noEntryPoint();
		}
		if (m.getDeclaredAnnotation(TestEntrypoint.class) != null) {
			isEntryPoint = true;
		}
		return isEntryPoint;
	}

	private void buildJARFor(Method mTestcase, SootMethod smTestcase, Class<?> t) throws Exception {
		Options.v().set_output_format(Options.output_format_class);
		Options.v().set_java_version(Options.java_version_8);
		SootMethod mEntryPoint = Scene.v().getMethod("<de.fraunhofer.sit.sse.valbench.MainClass: void main(java.lang.String[])>");
		Body bef = mEntryPoint.retrieveActiveBody();
		JimpleBody jb = Jimple.v().newBody(mEntryPoint);
		jb.insertIdentityStmts();
		jb.getUnits().add(Jimple.v().newReturnVoidStmt());
		mEntryPoint.setActiveBody(jb);
		EntryPointCreator javaCreator = new EntryPointCreator(mEntryPoint);

		ValueComputationTestCase d = mTestcase.getDeclaredAnnotation(ValueComputationTestCase.class);

		boolean canRunOnJVM = true;
		if (d != null)
			canRunOnJVM = !d.jvmFails();
		javaCreator.addEntryPoint(smTestcase.getSignature(), canRunOnJVM);
		javaCreator.finish();
		Shrinker shrinker = new Shrinker();
		Requires  required = mTestcase.getDeclaredAnnotation(Requires.class);
		if (required != null) {
			for (String s : required.requiredClasses()) {
				shrinker.addKeepClassCompletely(Scene.v().getSootClass(s));
			}
		}
		required = mTestcase.getDeclaringClass().getDeclaredAnnotation(Requires.class);
		if (required != null) {
			for (String s : required.requiredClasses()) {
				shrinker.addKeepClassCompletely(Scene.v().getSootClass(s));
			}
		}

		for (SootClass i : Scene.v().getApplicationClasses())
			if (i.getName().startsWith(smTestcase.getDeclaringClass().getName() + "$"))
				shrinker.addKeepClassCompletely(i);
		shrinker.addKeep(smTestcase);
		shrinker.addKeep(mEntryPoint);
		File f = File.createTempFile("tmp", "dir");
		f.delete();
		f.mkdirs();
		removeExplicitLPs();
		ExplicitLoggingPointCreator.createExplicitLP(smTestcase);
		Options.v().set_output_dir(f.getAbsolutePath());
		shrinker.write();

		File fout = new File("target/single-testcase-jars");
		fout.mkdirs();
		File out = new File(fout, mTestcase.getDeclaringClass().getSimpleName() + "_" + mTestcase.getName() + ".jar");
		try (ZipFile srcFile = new ZipFile(normalJar)) {
			try (ZipOutputStream destFile = new ZipOutputStream(Files.newOutputStream(Paths
					.get(out
							.toURI())))) {
				//Copy from source
				Enumeration<? extends ZipEntry> entries = srcFile.entries();
				while (entries.hasMoreElements()) {
					ZipEntry src = entries.nextElement();
					if (src.getName().equals("Requirements.json") || src.getName().equals("GroundTruth.json"))
						continue;
					if (src.getName().endsWith(".class")) {
						String s = getClassName(src.getName());
						if (!shouldKeepOriginal(shrinker, s, mTestcase.getDeclaringClass().getName()))
							continue;
					}
					allUsedFiles.add(src.getName());
					ZipEntry dest = new ZipEntry(src.getName());
					destFile.putNextEntry(dest);
					try (InputStream content = srcFile.getInputStream(src)) {
						IOUtils.copy(content, destFile);
					}
					destFile.closeEntry();
				}
				
				//now the new classes
				for (File fc : FileUtils.listFiles(f, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE)) {
					Path p1 = Paths.get(fc.toURI());
					Path p2 = Paths.get(f.toURI());
					Path rel = p2.relativize(p1);
					String relp = rel.toString().replace("\\", "/");
					String s = getClassName(relp);
					if (shouldKeepOriginal(shrinker, s, mTestcase.getDeclaringClass().getName()))
						continue;
					allUsedFiles.add(relp);

					ZipEntry dest = new ZipEntry(relp);
					destFile.putNextEntry(dest);
					try (InputStream content = new FileInputStream(fc)) {
						IOUtils.copy(content, destFile);
					}
					destFile.closeEntry();
				}
				destFile.finish();
			}
		}
		FileUtils.deleteDirectory(f);
		
		


		if (canRunOnJVM) {
			
			run(mTestcase, t, out);
		}
		mEntryPoint.setActiveBody(bef);
	}
	
	private void buildAPKFor(Method mTestcase, SootMethod smTestcase, Class<?> t) throws Exception {
		Requires required = mTestcase.getDeclaredAnnotation(Requires.class);
		if (required != null && required.doesNotWorkOnAndroid())
			return;
		Requires requiredCl = mTestcase.getDeclaringClass().getDeclaredAnnotation(Requires.class);
		if (requiredCl != null && requiredCl.doesNotWorkOnAndroid())
			return;
		Options.v().set_output_format(Options.output_format_dex);
		SootClass classicalEntryPoint = Scene.v().getSootClass("de.fraunhofer.sit.sse.valbench.MainClass");
		classicalEntryPoint.setLibraryClass();
		SootClass ma = Scene.v().getSootClass("de.fraunhofer.sit.sse.valbench.base.MainActivity");
		SootMethod mEntryPoint = ma.getMethodByName("startTests");
		Body bef = mEntryPoint.retrieveActiveBody();
		JimpleBody jb = Jimple.v().newBody(mEntryPoint);
		jb.insertIdentityStmts();
		jb.getUnits().add(Jimple.v().newReturnVoidStmt());
		mEntryPoint.setActiveBody(jb);
		EntryPointCreator javaCreator = new EntryPointCreator(mEntryPoint);

		ValueComputationTestCase d = mTestcase.getDeclaredAnnotation(ValueComputationTestCase.class);
		boolean canRunOnJVM = true;
		if (d != null)
			canRunOnJVM = !d.jvmFails();
		javaCreator.addEntryPoint(smTestcase.getSignature(), canRunOnJVM);
		javaCreator.finish();
		Shrinker shrinker = new Shrinker();
		for (SootClass i : Scene.v().getApplicationClasses()) {
			if (i.getName().startsWith("de.fraunhofer.sit.sse.valbench.base.MainActivity"))
				shrinker.addKeepClassCompletely(i);
		}
		if (required != null) {
			for (String s : required.requiredClasses()) {
				shrinker.addKeepClassCompletely(Scene.v().getSootClass(s));
			}
		}
		required = mTestcase.getDeclaringClass().getDeclaredAnnotation(Requires.class);
		if (required != null) {
			for (String s : required.requiredClasses()) {
				shrinker.addKeepClassCompletely(Scene.v().getSootClass(s));
			}
		}

		for (SootClass i : Scene.v().getApplicationClasses())
			if (i.getName().startsWith(smTestcase.getDeclaringClass().getName() + "$"))
				shrinker.addKeepClassCompletely(i);
		shrinker.addKeep(smTestcase);
		shrinker.addKeep(mEntryPoint);
		removeExplicitLPs();
		ExplicitLoggingPointCreator.createExplicitLP(smTestcase);
		Options.v().set_output_dir("target/single-testcase-apks");
		shrinker.write();
		File dst = new File("target/single-testcase-apks/" + mTestcase.getDeclaringClass().getSimpleName() + "_" + mTestcase.getName() + ".apk");
		dst.delete();
		FileUtils.moveFile(new File("target/single-testcase-apks/app-debug.apk"), dst);
		
		mEntryPoint.setActiveBody(bef);
		

		classicalEntryPoint.setApplicationClass();
	}

	public void removeExplicitLPs() {
		SootMethod m = Scene.v().grabMethod("<de.fraunhofer.sit.sse.valbench.ExplicitLoggingPoint: void explicitLoggingPoint(java.lang.Object)>");
		for (SootClass sc : Scene.v().getApplicationClasses()) {
			if (!sc.getName().startsWith("de.fraunhofer.sit.sse.valbench."))
				continue;
			for (SootMethod mp : sc.getMethods()) {
				if (mp.isConcrete()) {
					Body body = mp.retrieveActiveBody();
					Iterator<Unit> it = body.getUnits().iterator();
					boolean changed = false;
					while (it.hasNext()) {
						Stmt s = (Stmt) it.next();
						if (s.containsInvokeExpr() && s.getInvokeExpr().getMethod() == m && body.getUnits().getSuccOf(s) instanceof ReturnStmt) {
							it.remove();
							changed = true;
						}
					}
					if (changed) {
						DeadAssignmentEliminator.v().transform(body);
						UnusedLocalEliminator.v().transform(body);
					}
					
				}
			}
		}
	}

	private boolean shouldKeepOriginal(Shrinker shrinker, String s, String testcase) {
		if (s.startsWith("de.fraunhofer.sit.sse.valbench.") && s.startsWith(testcase + "$"))
			return true;
		if (shrinker.keepClassesOriginal.contains(s))
			return true;
		
		s += "$";
		for (String i : shrinker.keepClassesOriginal)
			if (i.startsWith(s))
				return true;
		
		return false;
	}

	private void run(Method mTestcase, Class<?> t, File out) throws MalformedURLException {
		URLClassLoader child = new URLClassLoader(
		        new URL[] {out.toURI().toURL()},
		        new ClassLoader() {
		        	protected java.lang.Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
		        		if (name.startsWith("java") || name.startsWith("org."))
		        			return this.getClass().getClassLoader().loadClass(name);
		        		return null;
		        	}
				}
		);
		String n = mTestcase.getDeclaringClass().getName();
		try {
			Class<?> cloaded = child.loadClass(n);
			if (t == cloaded)
				throw new RuntimeException("Classes are equals");
			Method c = cloaded.getDeclaredMethod(mTestcase.getName(), mTestcase.getParameterTypes());
			Object res;
			if ((c.getModifiers() & Modifier.STATIC) != 0)
				res = c.invoke(null, null);
			else {
				res = c.invoke(cloaded.newInstance(), null);;
			}
			IResult r = TestCaseManager.getTestCaseFor(mTestcase).getResultVerifier().verifyResult(res);
			if (r.falsePositives() > 0 || r.truePositives() == 0)
				//false negatives are expected when running only once
				throw new RuntimeException("Unexpected: " + r + " for testcase " + mTestcase.toGenericString());
		} catch (Throwable tt) {
			tt.printStackTrace();
			throw new RuntimeException(tt);
		}
	}

	private String getClassName(String src) {
		String s =  src.substring(0, src.length() - 6);
		s = s.replace("/", ".");
		return s;
	}

}
