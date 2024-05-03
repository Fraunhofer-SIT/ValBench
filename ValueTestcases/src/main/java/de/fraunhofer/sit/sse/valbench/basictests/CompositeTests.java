package de.fraunhofer.sit.sse.valbench.basictests;

import java.util.Random;

import de.fraunhofer.sit.sse.valbench.metadata.ValueComputationTestCase;

public class CompositeTests {

	@ValueComputationTestCase(expectedValues = { "aa", "bb" })
	public static String composite1() {
		String x, y;
		if (new Random().nextBoolean()) {
			x = "a";
			y = "a";
		} else {
			x = "b";
			y = "b";
		}
		return x + y;
	}

	@ValueComputationTestCase(expectedValues = { "aa", "bb" })
	public static String composite2() {
		String x, y;
		boolean b = new Random().nextBoolean();
		if (b) {
			x = "a";
		} else {
			x = "b";
		}
		if (b) {
			y = "a";
		} else {
			y = "b";
		}
		return x + y;
	}

	@ValueComputationTestCase(expectedValues = { "aa", "bb" })
	public static String composite3() {
		String x, y;
		boolean b = new Random().nextBoolean();
		if (b) {
			x = "a";
		} else {
			x = "b";
		}
		if (!b) {
			y = "b";
		} else {
			y = "a";
		}
		return x + y;
	}
}
