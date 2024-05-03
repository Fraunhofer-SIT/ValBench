package de.fraunhofer.sit.sse.valbench.basictests;

import de.fraunhofer.sit.sse.valbench.metadata.ValueComputationTestCase;

public class CalleeTests {

	static class Base {
		protected String foo;

		public Base(String val) {
			foo = val;
		}

		public String get() {
			return "Base" + foo;
		}
	}

	static class A extends Base {
		public A(String val) {
			super(val);
		}

		@Override
		public String get() {
			return "A" + foo;
		}

	}

	static class B extends A {
		public B(String val) {
			super(val);
		}

		@Override
		public String get() {
			return "B" + foo;
		}

	}

	@SuppressWarnings("unchecked")
	private static <T> T get(int i, String val) {
		switch (i) {
		case 0:
			return (T) new Base(val);
		case 1:
			return (T) new A(val);
		case 2:
		default:
			return (T) new B(val);
		}
	}

	@ValueComputationTestCase
	public static String test() {
		Base a = get(1, "Test");
		Base b = get(2, "Test");
		return a.get() + "_" + b.get();
	}
	
	
	public static void unused() {
		id("Wrong");
	}
	
	@ValueComputationTestCase
	public static String testContextSensitivity() {
		return id("Right");
	}

	private static String id(String string) {
		return string;
	}

}
