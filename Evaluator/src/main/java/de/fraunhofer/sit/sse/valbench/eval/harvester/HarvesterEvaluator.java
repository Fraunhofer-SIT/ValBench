package de.fraunhofer.sit.sse.valbench.eval.harvester;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import de.fraunhofer.sit.harvester.loggingpoints.ExtractionResults;
import de.fraunhofer.sit.harvester.loggingpoints.LoggingPointResult;
import de.fraunhofer.sit.harvester.loggingpoints.SQLiteExtractionResultFile;
import de.fraunhofer.sit.sse.valbench.SootUtils;
import de.fraunhofer.sit.sse.valbench.eval.EvaluationUtils;
import de.fraunhofer.sit.sse.valbench.eval.IAndroidEvaluator;
import de.fraunhofer.sit.sse.valbench.eval.Inputs;
import de.fraunhofer.sit.sse.valbench.metadata.interfaces.ITestCase;

public class HarvesterEvaluator implements IAndroidEvaluator {

	private ExtractionResults res;

	@Override
	public void run(File resFile, Inputs inp) throws Exception {
		FileOutputStream fos = new FileOutputStream(new File("Harvester-LPSig.txt"));
		fos.write("<valbench.ExplicitLoggingPoint: void explicitLoggingPoint(java.lang.Object)>|0".getBytes());
		fos.close();
		
		File s = EvaluationUtils.checkConfigurablePath("HARVESTER_PATH", "Harvester-1.0.0-SNAPSHOT.jar");
		EvaluationUtils.run("java", "-jar", s.getAbsolutePath(), "-o", "Harvester-Results", "-sdkdir", EvaluationUtils.getAndroidSDKPath(), "-logging-point-signatures", "Harvester-LPSig.txt", "-timeout-per-path", "10s", inp.apk.getAbsolutePath()
				);
		FileUtils.copyFile(new File("Harvester-Results/Trace_valbench-testcases-android.apk.db"), resFile);
		
		//Run using 
		//we noticed that Harvester hangs in one test case, so we chose a path timeout.
		//apart from that we used standard options
	}

	@Override
	public void readIn(File f) throws IOException, SQLException {
		ExtractionResults res = SQLiteExtractionResultFile.readLoggingPointResults(f);
		this.res = res;
	}

	@Override
	public Object evaluate(ITestCase testcase) {
		String sootSig = SootUtils.getSootSignature(testcase.getTestCaseMethod());
		Set<String> values = new HashSet<>();
		for (LoggingPointResult r : res.getResults()) {
			if (r.getMethodSignature().equals(sootSig)) {
				values.addAll(r.getVariableValues());
			}
		}
		return values;
	}

	@Override
	public String getName() {
		return "Harvester";
	}

}
