package de.fraunhofer.sit.sse.valbench.eval.blueseal;

import java.io.File;
import java.util.List;

import com.google.common.collect.HashMultimap;

import de.fraunhofer.sit.sse.valbench.SootUtils;
import de.fraunhofer.sit.sse.valbench.eval.EvaluationUtils;
import de.fraunhofer.sit.sse.valbench.eval.IAndroidEvaluator;
import de.fraunhofer.sit.sse.valbench.eval.Inputs;
import de.fraunhofer.sit.sse.valbench.eval.EvaluationUtils.Result;
import de.fraunhofer.sit.sse.valbench.metadata.interfaces.ITestCase;

public class BlueSealEvaluator implements IAndroidEvaluator {
	  HashMultimap<String, String> s = HashMultimap.create();

	@Override
	public void run(File resFile, Inputs inp) throws Exception {
		//We ran BlueSeal via the IDE
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
		return "BlueSeal";
	}

}
