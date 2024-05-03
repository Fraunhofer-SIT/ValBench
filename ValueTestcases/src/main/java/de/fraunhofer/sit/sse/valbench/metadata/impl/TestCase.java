package de.fraunhofer.sit.sse.valbench.metadata.impl;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import de.fraunhofer.sit.sse.valbench.metadata.ValueComputationTestCase;
import de.fraunhofer.sit.sse.valbench.metadata.impl.requirements.CachedRequirements;
import de.fraunhofer.sit.sse.valbench.metadata.interfaces.IRequirement;
import de.fraunhofer.sit.sse.valbench.metadata.interfaces.IResultComparisonStrategy;
import de.fraunhofer.sit.sse.valbench.metadata.interfaces.IResultVerifier;
import de.fraunhofer.sit.sse.valbench.metadata.interfaces.ITestCase;
import de.fraunhofer.sit.sse.valbench.metadata.interfaces.ITestSuite;

public class TestCase implements ITestCase {

	private Method testMethod;
	private ITestSuite suite;
	private ValueComputationTestCase annotation;

	public TestCase(ITestSuite suite, Method testClass, ValueComputationTestCase tc) {
		this.suite = suite;
		this.testMethod = testClass;
		this.annotation = tc;
	}

	@Override
	public String getName() {
		return testMethod.getName();
	}

	@Override
	public Method getTestCaseMethod() {
		return testMethod;
	}

	@Override
	public IResultVerifier getResultVerifier() {
		return new ResultVerifier(this, StrictResultVerificationStrategy.INSTANCE);
	}

	@Override
	public IResultVerifier getResultVerifier(IResultComparisonStrategy strategy) {
		return new ResultVerifier(this, strategy);
	}

	@Override
	public Set<Object> getExpectedResults() {
		if (annotation.expectedValues() != null && annotation.expectedValues().length > 0) {
			Set<Object> res = new HashSet<>();
			String[] strExpected = annotation.expectedValues();
			for (String d : strExpected) {
				res.add(d);
			}
			return res;
		}
		if (annotation.jvmFails()) {
			throw new RuntimeException("JVM would fail, but no expected results were specified");
		}
		Object result;
		try {
			try {
				Method resetMethod = testMethod.getDeclaringClass().getDeclaredMethod("reset");
				resetMethod.setAccessible(true);
				resetMethod.invoke(null);
			} catch (NoSuchMethodException e) {
			}
			testMethod.setAccessible(true);
			if ((testMethod.getModifiers() & Modifier.STATIC) != 0)
				result = testMethod.invoke(null);
			else {
				Object objInstance = testMethod.getDeclaringClass().newInstance();
				result = testMethod.invoke(objInstance);
			}
		} catch (Exception e) {
			throw new CouldNotInvokeTestMethodException(this, testMethod, e);
		}
		return Collections.singleton(result);
	}

	@Override
	public ITestSuite getTestSuite() {
		return suite;
	}
	
	@Override
	public String toString() {
		return getSignature();
	}

	@Override
	public String getSignature() {
		String params = "";
		for (Parameter p : testMethod.getParameters()) {
			if (!params.isEmpty())
				params +=", ";
			params += p.getType().getName();
		}
		return testMethod.getDeclaringClass().getName() + "." + testMethod.getName() + "(" + params + ")";
	}

	@Override
	public Set<IRequirement> getRequirements() {
		return CachedRequirements.getRequirementsFor(getSignature());
	}

	@Override
	public boolean hasRequirement(IRequirement req) {
		return getRequirements().contains(req);
	}

}
