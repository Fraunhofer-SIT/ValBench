package de.fraunhofer.sit.sse.valbench.eval.violist;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.google.common.collect.HashMultimap;

import de.fraunhofer.sit.sse.valbench.SootUtils;
import de.fraunhofer.sit.sse.valbench.eval.EvaluationUtils;
import de.fraunhofer.sit.sse.valbench.eval.IEvaluator;
import de.fraunhofer.sit.sse.valbench.eval.Inputs;
import de.fraunhofer.sit.sse.valbench.eval.EvaluationUtils.Result;
import de.fraunhofer.sit.sse.valbench.metadata.interfaces.ITestCase;

public class ViolistEvaluator implements IEvaluator {
	  HashMultimap<String, String> s = HashMultimap.create();

	@Override
	public void run(File resFile, Inputs inp) throws Exception {
		EvaluationUtils.run("java", "-Xmx200g", "-jar", "../Approaches/Violist/code/NewStringAnalysis/target/NewStringAnalysis-0.0.1-SNAPSHOT.jar", inp.rtJar.getAbsolutePath(), inp.jar.getAbsolutePath());
		FileUtils.copyFile(new File("Violist-Results.txt"), resFile);
		
	}


	@Override
	public void readIn(File f) throws Exception {
		List<Result> r = EvaluationUtils.readStandardFormat(f);
		EvaluationUtils.splitRegEx(r);

		for (EvaluationUtils.Result res : r) {
			res.value = res.value.replace("NULL-CONSTANT", "null");
			s.put(res.method, res.value);
		}
	}

	@Override
	public Object evaluate(ITestCase testcase) {
		return s.get(SootUtils.getSootSignature(testcase.getTestCaseMethod()));
	}

	@Override
	public String getName() {
		return "Violist";
	}

}
