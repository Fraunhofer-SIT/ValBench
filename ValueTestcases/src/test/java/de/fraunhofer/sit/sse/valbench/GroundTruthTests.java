package de.fraunhofer.sit.sse.valbench;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.google.gson.Gson;

import de.fraunhofer.sit.sse.valbench.metadata.TestCaseManager;
import de.fraunhofer.sit.sse.valbench.metadata.interfaces.ITestCase;

public class GroundTruthTests {
	@Test
	public void createGroundTruth() throws FileNotFoundException {
		List<GroundTruth> groundTruth = new ArrayList<>();
		Collection<ITestCase> d = TestCaseManager.getAllTestCases();
		for (ITestCase tc : d) {
			System.out.println(tc.toString());
			Set<Object> exp = tc.getExpectedResults();
			System.out.println("Expected: " + exp);
			groundTruth.add(new GroundTruth(tc.getSignature(), exp));
		}
		System.out.println(String.format("We've got %d testcases", d.size()));

		Gson gson = new Gson();
		try (PrintWriter pw = new PrintWriter(new File("target/classes/GroundTruth.json"))) {
			pw.print(gson.toJson(groundTruth));
		}
	}
	
	private static class GroundTruth {
		public GroundTruth(String name, Set<Object> exp) {
			this.testCase = name;
			expected = exp.toArray();

		}
		String testCase;
		Object[] expected;
	}
	
}
