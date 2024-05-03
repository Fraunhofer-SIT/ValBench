package de.fraunhofer.sit.sse.valbench;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

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

public class DeterminismTests {
	private static final int NUMBER_OF_TIMES = 10;

	@Test
	public void checkDeterminism() throws FileNotFoundException {
		Collection<ITestCase> d = TestCaseManager.getAllTestCases();
		for (ITestCase tc : d) {
			Set<Object> exp = tc.getExpectedResults();
			for (int i = 0; i < NUMBER_OF_TIMES; i++) {
				Set<Object> retry = tc.getExpectedResults();
				assertTrue(tc.getResultVerifier().verifyResult(retry).isCompletelyCorrect());
				assertEquals(exp, retry);
			}
		}
	}
}
