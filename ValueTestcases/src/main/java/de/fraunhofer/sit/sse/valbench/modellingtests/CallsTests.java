package de.fraunhofer.sit.sse.valbench.modellingtests;

import de.fraunhofer.sit.sse.valbench.metadata.ValueComputationTestCase;

public class CallsTests {

	@ValueComputationTestCase
	public static String simplestCallTest() {
		return arg(2, "Test", "Foo", "Bar").toString();
	}

	private static Object arg(int i, Object... a) {
		return a[i];
	}

}
