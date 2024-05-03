package de.fraunhofer.sit.sse.valbench.basictests;

import java.util.Random;

import de.fraunhofer.sit.sse.valbench.metadata.TestEntrypoint;
import de.fraunhofer.sit.sse.valbench.metadata.ValueComputationTestCase;

public class IfTests {

	@ValueComputationTestCase
	public static String testPathSensitivity() throws Exception {
		int i = getInt();
		boolean b = i < 2;
		StringBuilder sb = new StringBuilder();
		if (b) {
			sb.append("x");
		}
		if (b) {
			sb.append("y");
		}
		return sb.toString();
	}

	
	private static int getInt() {
		return 1;
	}

	@ValueComputationTestCase
	public static String testImpossibleIfCondition() throws Exception {
		String op = "True";
		boolean a = false;
		if (a)
			op = "Impossible";
		return op;
	}

	@ValueComputationTestCase(expectedValues = { "x" }, jvmFails = true)
	public static String testHashcode() {
		String r;
		if (new Random().nextBoolean())
			r = "y";
		else
			r = "x";
		if (r != null && r.hashCode() != 121)
			return r;
		throw new RuntimeException();
	}

	@ValueComputationTestCase(expectedValues = { "x" }, jvmFails = true)
	public static String testEquals() {
		String r;
		if (new Random().nextBoolean())
			r = "y";
		else
			r = "x";
		if (r != null && !r.equals("y"))
			return r;
		throw new RuntimeException();
	}

	private static String IMPOSSIBLE_VALUE = "IMPOSSIBLE.";

	@TestEntrypoint
	public static void testImpossibleIf2() {
		testImpossibleIf2("USA");
		testImpossibleIf2("CANADA");
		testImpossibleIf2("Foo");
	}
	
	@ValueComputationTestCase(expectedValues = { "USA", "CANADA", "AUSTRALIA" }, noEntryPoint = true)
	public static String testImpossibleIf2(String input) {
		String defaultVal = input;
		final String impossible = IMPOSSIBLE_VALUE;
		if (defaultVal.equals("US"))
			return "USA";
		if (defaultVal.equals("CA"))
			return "CANADA";
		if (defaultVal.equals("US"))
			return impossible;
		return "AUSTRALIA";
	}

}