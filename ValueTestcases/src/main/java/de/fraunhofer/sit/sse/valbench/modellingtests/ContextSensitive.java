package de.fraunhofer.sit.sse.valbench.modellingtests;

import java.util.Random;

import de.fraunhofer.sit.sse.valbench.metadata.TestEntrypoint;
import de.fraunhofer.sit.sse.valbench.metadata.ValueComputationTestCase;

public class ContextSensitive {

	static A b = create();

	@ValueComputationTestCase(expectedValues = { "aa", "bb", "xx" }) 
	public static String test() {
		A a = b;
		return a.foo() + a.foo();
	}

	@ValueComputationTestCase(expectedValues = { "oo", "pp", "yy" }) 
	public static String test2() {
		I a = b;
		return a.def() + a.def();
	}

	public static void caller1() {
		A x = create();

		testCallee(x);
	}

	private static A create() {
		Random rand = new Random();
		switch (rand.nextInt()) {
		case 0:
			return new A();
		case 1:
			return new B();
		case 2:
			return new D();
		case 3:
			return new E();
		default:
			return new C();

		}

	}
	
	@TestEntrypoint
	public static void doCalls() {
		String s = testCallee(create()) + testCallee2(create());
		
	}

	
	@ValueComputationTestCase(expectedValues = { "aa", "bb", "xx" }, noEntryPoint = true) 
	public static String testCallee(A a) {
		return a.foo() + a.foo();
	}

	@ValueComputationTestCase(expectedValues = { "oo", "pp", "yy" }, noEntryPoint = true) 
	public static String testCallee2(I a) {
		return a.def() + a.def();
	}

	static interface I {
		default String def() {
			return "o";
		}
	}

	static class A implements I {
		String foo() {
			return "a";
		}
	}

	static class B extends A {

	}

	static class D extends A {

	}

	static class E extends D {
		@Override
		String foo() {
			return "x";
		}

		@Override
		public String def() {
			return "y";
		}
	}

	static class C extends A {
		@Override
		String foo() {
			return "b";
		}

		@Override
		public String def() {
			return "p";
		}

	}
}