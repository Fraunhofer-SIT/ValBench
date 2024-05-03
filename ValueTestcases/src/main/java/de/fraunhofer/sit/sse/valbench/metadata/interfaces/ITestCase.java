package de.fraunhofer.sit.sse.valbench.metadata.interfaces;

import java.lang.reflect.Method;
import java.util.Set;

public interface ITestCase {
	public String getName();

	public Method getTestCaseMethod();

	public IResultVerifier getResultVerifier();

	public IResultVerifier getResultVerifier(IResultComparisonStrategy strategy);

	public Set<Object> getExpectedResults();

	public Set<IRequirement> getRequirements();

	public ITestSuite getTestSuite();

	public String getSignature();

	public boolean hasRequirement(IRequirement req);

}
