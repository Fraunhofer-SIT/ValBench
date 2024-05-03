package de.fraunhofer.sit.sse.valbench.metadata;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.google.common.reflect.ClassPath;

import de.fraunhofer.sit.sse.valbench.metadata.impl.TestSuite;
import de.fraunhofer.sit.sse.valbench.metadata.interfaces.ITestCase;
import de.fraunhofer.sit.sse.valbench.metadata.interfaces.ITestSuite;

public class TestCaseManager {
	public static Set<Class<?>> ALL_CLASSES;
	
	static {
		String pck = TestCaseManager.class.getPackage().getName();
		final String pckS = pck.substring(0, pck.indexOf(".") + 1);
		try {
			ALL_CLASSES = ClassPath.from(ClassLoader.getSystemClassLoader()).getAllClasses().stream()
					.filter(clazz -> clazz.getPackageName().startsWith(pckS)).map(clazz -> clazz.load()).
					filter(c -> hasAtLeastOneTestCase(c))
					.collect(Collectors.toSet());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}
	
	private TestCaseManager() {
	}

	public static Collection<ITestSuite> getAllTestSuites() {
		Set<ITestSuite> testSuites = new HashSet<>();
		for (Class<?> p : ALL_CLASSES) {
				testSuites.add(new TestSuite(p));
		}

		return testSuites;
	}

	public static Collection<ITestCase> getAllTestCases() {
		Set<ITestCase> testCases = new HashSet<>();
		for (ITestSuite suite : getAllTestSuites()) {
			testCases.addAll(suite.getTestCases());
		}
		return testCases;
	}

	public static boolean hasAtLeastOneTestCase(Class<?> p) {
		for (Method m : p.getDeclaredMethods()) {
			ValueComputationTestCase tc = m.getDeclaredAnnotation(ValueComputationTestCase.class);
			if (tc != null) {
				return true;
			}
		}
		return false;
	}

	public static ITestCase getTestCaseFor(Method mTestcase) {
		for (ITestCase i : getAllTestCases()) {
			if (i.getTestCaseMethod().toGenericString().equals(mTestcase.toGenericString()))
				return i;
		}
		return null;
		
	}
}
