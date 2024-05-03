package de.fraunhofer.sit.sse.valbench.eval.stringhound;

import java.io.File;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.google.common.collect.HashMultimap;

import de.fraunhofer.sit.sse.valbench.SootUtils;
import de.fraunhofer.sit.sse.valbench.eval.EvaluationUtils;
import de.fraunhofer.sit.sse.valbench.eval.EvaluationUtils.Result;
import de.fraunhofer.sit.sse.valbench.eval.IEvaluator;
import de.fraunhofer.sit.sse.valbench.eval.Inputs;
import de.fraunhofer.sit.sse.valbench.metadata.TestCaseManager;
import de.fraunhofer.sit.sse.valbench.metadata.interfaces.ITestCase;

public class StringHoundEvaluator implements IEvaluator {
	  HashMultimap<String, String> s = HashMultimap.create();

	@Override
	public void run(File resFile, Inputs inp) throws Exception {
		File stringHoundWorkingDir = new File("../Approaches/StringHound/analysis").getAbsoluteFile();
		try (PrintWriter pw = new PrintWriter(new File(stringHoundWorkingDir, "StringHound-Input.txt"))) {
			pw.println(getOpalSig(Class.forName("de.fraunhofer.sit.sse.valbench.ExplicitLoggingPoint").getDeclaredMethod("explicitLoggingPoint", Object.class)));
			for (ITestCase i : TestCaseManager.getAllTestCases()) {
				pw.println(getOpalSig(i.getTestCaseMethod()));
			}
		}
		File dir= EvaluationUtils.createTempDirectory();
		EvaluationUtils.runWithWorkingDir(stringHoundWorkingDir, inp.java8.getAbsolutePath(), "-jar", new File(stringHoundWorkingDir, "target/scala-2.12/deobfuscator-assembly-1.0.jar").getAbsolutePath(), 
				"-s", "-j","-o", dir.getAbsolutePath(), "-f",
				 inp.jar.getAbsolutePath());
		FileUtils.copyFile(new File(dir, "results/" + inp.jar.getName() + ".json"), resFile);
	}
	

	//e.g. com/google/common/collect/Multimaps MethodSignature(invertFrom,MethodDescriptor((com.google.common.collect.Multimap, com.google.common.collect.Multimap): com.google.common.collect.Multimap))
	private String getOpalSig(Method m) {
		String n = m.getDeclaringClass().getName().replace(".", "/");
		String params = "MethodDescriptor((";
		
		for (Class<?> p : m.getParameterTypes()) {
			params += p.getName() + ", ";
		}
		if (m.getParameterCount() > 0)
			params = params.substring(0, params.length() - 2);
		params += "): " + m.getReturnType().getName() + "))";
		String res = n + " MethodSignature(" + m.getName() + "," + params;
		return res;
	}

	@Override
	public void readIn(File f) throws Exception {
		List<Result> r = EvaluationUtils.readStandardFormat(f);
		
		for (EvaluationUtils.Result res : r) {
			s.put(res.method, res.value);
		}
	}

	@Override
	public Object evaluate(ITestCase testcase) {
		return s.get(SootUtils.getSootSignature(testcase.getTestCaseMethod()));
	}

	@Override
	public String getName() {
		return "StringHound";
	}

}
