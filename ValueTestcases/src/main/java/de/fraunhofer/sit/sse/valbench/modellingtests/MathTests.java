package de.fraunhofer.sit.sse.valbench.modellingtests;

import de.fraunhofer.sit.sse.valbench.metadata.ValueComputationTestCase;

public class MathTests {

	@ValueComputationTestCase
	public String testSimpleMath() {
		return String.valueOf(7 * 11 + 1);
	}

	@ValueComputationTestCase
	public String testSimpleMath2() {
		return String.valueOf(1000 % 2);
	}

	@ValueComputationTestCase
	public String testSimpleMathFloat() {
		return String.valueOf(10D / 3.14159F);
	}

	@ValueComputationTestCase
	public String testSimpleMathDouble() {
		return String.valueOf(10D / 3.14159D);
	}

	@ValueComputationTestCase
	public String testNumericCasts() {
		double pi = 3.145F;
		int piInt = (int) pi;
		return String.valueOf(piInt);
	}
}
