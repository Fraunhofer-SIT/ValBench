package de.fraunhofer.sit.sse.valbench.basictests;

import de.fraunhofer.sit.sse.valbench.metadata.ValueComputationTestCase;

public class FieldDepthTests {

	static class ClassOne {
		public ClassTwo two;

		public ClassOne(String string) {
			two = new ClassTwo(string);
		}

	}

	static class ClassTwo {
		public ClassTwo(String r) {
			interesting = r;
		}

		public String interesting;
	}

	@ValueComputationTestCase
	public static String testFoo() {
		new ClassTwo("o");
		new ClassOne("y");
		ClassOne a = new ClassOne("correct");
		new ClassOne("false");
		new ClassTwo("p");
		return a.two.interesting;
	}

}
