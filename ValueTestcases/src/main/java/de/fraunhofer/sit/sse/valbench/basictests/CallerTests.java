package de.fraunhofer.sit.sse.valbench.basictests;

import de.fraunhofer.sit.sse.valbench.metadata.TestEntrypoint;
import de.fraunhofer.sit.sse.valbench.metadata.ValueComputationTestCase;

public class CallerTests {

	static String s;

	static {
		new CallerTests().runTest1();
	}

	@ValueComputationTestCase
	public static String test() {
		return s;
	}

	private void runTest1() {
		s = "x";
		neededCallee();
		s += "Foo";
		s += callee(s);
	}

	private void neededCallee() {
		neededCallee4();
		unneededCallee3();
		s += "a";
		unneededCallee2();
	}

	private void neededCallee4() {
		unneededCallee3();
		s += "b";
	}

	private void unneededCallee3() {
		unneededCallee2();
	}

	private void unneededCallee2() {

	}

	private String callee(String s2) {
		return s2 + "x";
	}

	@ValueComputationTestCase(expectedValues = { "foo" }, noEntryPoint=true)
	public static String testParam(String param) {
		return param;
	}

	@TestEntrypoint
	public static void callerParam() {
		testParam(append1(append2("f")));
	}

	private static String append2(String string) {
		return string + "o";
	}

	private static String append1(String x) {
		return x + "o";
	}

}
