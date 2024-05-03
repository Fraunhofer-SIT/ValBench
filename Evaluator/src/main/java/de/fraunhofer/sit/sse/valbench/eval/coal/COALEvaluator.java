package de.fraunhofer.sit.sse.valbench.eval.coal;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.google.common.collect.HashMultimap;

import de.fraunhofer.sit.sse.valbench.SootUtils;
import de.fraunhofer.sit.sse.valbench.eval.EvaluationUtils;
import de.fraunhofer.sit.sse.valbench.eval.IAndroidEvaluator;
import de.fraunhofer.sit.sse.valbench.eval.Inputs;
import de.fraunhofer.sit.sse.valbench.eval.EvaluationUtils.Result;
import de.fraunhofer.sit.sse.valbench.metadata.interfaces.ITestCase;

public class COALEvaluator implements IAndroidEvaluator {
	  HashMultimap<String, String> s = HashMultimap.create();

	@Override
	public void run(File resFile, Inputs inp) throws Exception {
		File platforms = new File(EvaluationUtils.getAndroidSDKPath(), "platforms");
		
		EvaluationUtils.run("java", "-jar", "../Approaches/COAL/target/coal-strings-0.1.2.jar", "--process-dir", inp.apk.getAbsolutePath(),
				"--allow-phantom-refs", "-src-prec", "apk", "-android-jars", platforms.getAbsolutePath());
		FileUtils.copyFile(new File("COAL-Output.txt"), resFile);
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
		return "COAL";
	}

}
