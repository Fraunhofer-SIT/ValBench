package de.fraunhofer.sit.sse.valbench.metadata.impl;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import de.fraunhofer.sit.sse.valbench.metadata.ValueComputationTestCase;
import de.fraunhofer.sit.sse.valbench.metadata.interfaces.ITestCase;
import de.fraunhofer.sit.sse.valbench.metadata.interfaces.ITestSuite;

public class TestSuite implements ITestSuite {

	private Class<?> testSuiteClass;

	public TestSuite(Class<?> testSuiteClass) {
		this.testSuiteClass = testSuiteClass;
	}

	@Override
	public String getName() {
		return testSuiteClass.getName();
	}
	
	@Override
	public Class<?> getTestSuiteClass() {
		return testSuiteClass;
	}

	@Override
	public Collection<ITestCase> getTestCases() {
		Set<ITestCase> res = new HashSet<>();
		for (Method m : testSuiteClass.getDeclaredMethods()) {
			ValueComputationTestCase tc = m.getDeclaredAnnotation(ValueComputationTestCase.class);
			if (tc != null) {
				res.add(new TestCase(this, m, tc));
			}
		}
		return res;
	}

	@Override
	public String toString() {
		return getName();
	}

}
