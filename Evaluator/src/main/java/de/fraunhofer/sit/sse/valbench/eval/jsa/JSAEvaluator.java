package de.fraunhofer.sit.sse.valbench.eval.jsa;

import java.io.File;
import java.util.List;

import com.google.common.collect.HashMultimap;

import de.fraunhofer.sit.sse.valbench.SootUtils;
import de.fraunhofer.sit.sse.valbench.eval.EvaluationUtils;
import de.fraunhofer.sit.sse.valbench.eval.IEvaluator;
import de.fraunhofer.sit.sse.valbench.eval.Inputs;
import de.fraunhofer.sit.sse.valbench.eval.EvaluationUtils.Result;
import de.fraunhofer.sit.sse.valbench.metadata.interfaces.ITestCase;

public class JSAEvaluator implements IEvaluator {
	  HashMultimap<String, String> s = HashMultimap.create();

	@Override
	public void run(File resFile, Inputs inp) throws Exception {
		 	
		int i = 0;
		File tmp = EvaluationUtils.createTempDirectory();
		for (File r : inp.getSingleTestCaseJars()) {
			EvaluationUtils.run("java", "-Xmx30g", "-jar", "../Approaches/JSA/string-2.1/target/jsa-2.1.1-SNAPSHOT.jar",
				inp.rtJar.getAbsolutePath(), r.getAbsolutePath(), new File(tmp, i + ".jar").getAbsolutePath());
			i++;
		}
		EvaluationUtils.combineAll(tmp, resFile);
	}
	
	@Override
	public void readIn(File f) throws Exception {
		List<Result> r = EvaluationUtils.readStandardFormat(f);
		EvaluationUtils.splitRegEx(r);
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
		return "JSA";
	}

}
