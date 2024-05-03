package de.fraunhofer.sit.sse.valbench.metadata.impl;

import java.lang.reflect.Method;

public class CouldNotInvokeTestMethodException extends RuntimeException {

	private TestCase testCase;
	private Method testMethod;

	public CouldNotInvokeTestMethodException(TestCase testCase, Method testMethod, Exception e) {
		super("Could not invoke " + testMethod + " of testcase " + testCase.getName(), e);
		this.testCase = testCase;
		this.testMethod = testMethod;
	}

	public TestCase getTestCase() {
		return testCase;
	}

	public Method getTestMethod() {
		return testMethod;
	}

}
