package de.fraunhofer.sit.sse.valbench.eval.valdroid;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

import com.google.gson.Gson;

import de.fraunhofer.sit.sse.valbench.SootUtils;
import de.fraunhofer.sit.sse.valbench.eval.EvaluationUtils;
import de.fraunhofer.sit.sse.valbench.eval.IAndroidEvaluator;
import de.fraunhofer.sit.sse.valbench.eval.Inputs;
import de.fraunhofer.sit.sse.valbench.metadata.TestCaseManager;
import de.fraunhofer.sit.sse.valbench.metadata.interfaces.ITestCase;

public class ValDroidEvaluator implements IAndroidEvaluator {
	
	private App app;

	@Override
	public Object evaluate(ITestCase testcase) {
		String search = SootUtils.getSootSignature(testcase.getTestCaseMethod());
		Set<String> res = new HashSet<>();
		for (ProgramLocation d : app.programLocations) {
			if (d.getMethod().equals(search)) {
				for (FindingMap f : d.findings) {
					for (Finding m : f.findings) {
						res.add(m.value);
					}
				}
			}
		}
		return res;
	}

	@Override
	public String getName() {
		return "ValDroid";
	}

	@Override
	public void run(File resFile, Inputs inp) throws Exception {

		File serverConf = new File("server.conf");
		try (PrintWriter pw = new PrintWriter(serverConf)) {
			String configLine = "EvaluateStaticConstantFinder.ReturnLoggingPoints=";
			for (ITestCase t : TestCaseManager.getAllTestCases()) {
				configLine += SootUtils.getSootSignature(t.getTestCaseMethod()) + "|";
			}
			configLine = configLine.substring(0, configLine.length() - 1);
			pw.println(configLine);
			pw.println("EvaluateStaticConstantFinder.ResultJSONPath=" + resFile.getAbsolutePath());
			pw.println("JavaAnalyzer.ValueFinder.Static.MaxAdditionalStatesPerEmulator=4");
			pw.println("JavaAnalyzer.ValueFinder.Static.MaxSplittedPathsPerEmulator=5");
			pw.println("JavaAnalyzer.ValueFinder.Static.MaximumPathCount=100000");
			pw.println("JavaAnalyzer.ValueFinder.Static.MaximumStatementsEmulator=100000");
			pw.println("JavaAnalyzer.ValueFinder.Static.MaxCalleeDepth=8");
			pw.println("JavaAnalyzer.ValueFinder.Static.MaxDepth=10000");
			pw.println("JavaAnalyzer.ValueFinder.Static.MaximumSearchItems=1000");
			pw.println("JavaAnalyzer.ValueFinder.Static.FieldTrickMode=precise");
			pw.println("JavaAnalyzer.ValueFinder.Static.IfConditionAware=slicedependencies");
			pw.println("EvaluateStaticConstantFinder.DisableTimeouts=true");
			pw.println("Android.SDKDirectory=" + EvaluationUtils.getAndroidSDKPath());

		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
		
		//We use the EvaluateConstantFinder binary
		String binary = "ConstantFinderEval";
		File s = EvaluationUtils.checkConfigurablePath("VALDROID_PATH", binary);
		EvaluationUtils.run(s.getAbsolutePath(), "-Xmx12g", "--configfile", serverConf.getAbsolutePath(), inp.apk.getAbsolutePath());

	}

	@Override
	public void readIn(File f) throws IOException {
		Gson gson = new Gson();
		try (FileReader reader = new FileReader(f)) {
			this.app = gson.fromJson(reader, App.class);
		}
	}

}
