package de.fraunhofer.sit.sse.valbench.modellingtests;

import java.util.Random;

import de.fraunhofer.sit.sse.valbench.metadata.ValueComputationTestCase;

public class MergeTests {
	static class Foo {
		public String xx() {
			return "Abc";
		}
	}

	static class A extends Foo {
		@Override
		public String xx() {
			return "Foo";
		}

	}

	static class B extends Foo {
		@Override
		public String xx() {
			return "Doo";
		}

	}

	static Foo foo;

	static {
		switch (new Random().nextInt()) {
		case 0:
			foo = new Foo();
			break;
		case 1:
			foo = new A();
			break;
		case 2:
		default:
			foo = new B();
			break;
		}
	}

	@ValueComputationTestCase(expectedValues = { "fDoocaa", "fDooaaa", "fDooaac", "fDoocac", "fDooacc", "fDooaaa", "fDoocaa",
			"fDooccc" })
	public static String testStringBuilders() {
		String x = doRand();
		if (x.equals("a") || x.equals("c"))// && !x.equals("b"))
		{
			String r = "f" + foo.xx() + doRand() + doRand() + x;
			if (r.startsWith("fDoo") && !r.endsWith("ca"))
				return r;
			return "other2";
		} else
			return "other1";
	}


	private static String doRand() {
		if (System.currentTimeMillis() % 2 == 0)
			return "a";
		else
			return "c";
	}

}
