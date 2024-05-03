package de.fraunhofer.sit.sse.valbench.metadata.interfaces;

import java.util.Collection;

public interface ITestSuite {
	public String getName();

	public Collection<ITestCase> getTestCases();

	public Class<?> getTestSuiteClass();

}
