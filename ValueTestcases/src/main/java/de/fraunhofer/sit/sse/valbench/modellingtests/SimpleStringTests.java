package de.fraunhofer.sit.sse.valbench.modellingtests;

import de.fraunhofer.sit.sse.valbench.metadata.ValueComputationTestCase;

public class SimpleStringTests {
	static final boolean b = false;
	final static int x = 4;

	@ValueComputationTestCase
	public static String testStringBuilders() {
		StringBuilder s = new StringBuilder("Foo");
		StringBuilder x = s.append("vl").append("r").append("f");
		if (x.length() > 2)
			x.append("ad");
		s.append("ff");
		return s.toString();
	}

	@ValueComputationTestCase
	public static String callees() {
		String s = callee4s();
		int[] x = new int[3];
		for (int i = 0; i < 3; i++) {
			s += i;
			x[i] = i * 2;
		}
		for (int i = 0; i < 3; i++)
			s += x[i];
		return s;
	}

	static class Feld {
		int x;
	}

	public static String callee4s() {

		Feld f = new Feld();
		f.x = 2;
		foo(f);
		Feld g = new Feld();
		foo(g);
		g.x = 20;
		return String.valueOf(f.x);
	}

	private static void foo(Feld f) {
		f.x = 50;
	}

	@ValueComputationTestCase
	public static String callees2() {
		String s = callee("http://abc" + callee(callee("foo", callee("bla", "wow")) + "/test", "rr"), "ff");
		return s;
	}

	private static String callee(String string, String string2) {
		return string + "_" + string2 + callee3() + callee2();
	}

	private static String callee2() {
		return "Hehehehe" + callee4();
	}

	private static String callee4() {
		return "xyz";
	}

	private static String callee3() {
		return "Hehehehe" + String.valueOf(callee2());
	}

}
