package de.fraunhofer.sit.sse.valbench.modellingtests;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import de.fraunhofer.sit.sse.valbench.metadata.ValueComputationTestCase;

public class ThreadTests {
	@ValueComputationTestCase
	public static String testThread() throws IOException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException, InstantiationException {

		return call() + Thread.currentThread().getStackTrace()[1].getMethodName();
	}

	private static String call() {
		StackTraceElement[] c = Thread.currentThread().getStackTrace();
		return c[0].getMethodName() + c[0].getClassName() + c[1].getMethodName() + c[1].getClassName();
	}
}
