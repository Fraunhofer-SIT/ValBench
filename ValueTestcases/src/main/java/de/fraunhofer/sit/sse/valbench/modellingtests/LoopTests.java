package de.fraunhofer.sit.sse.valbench.modellingtests;

import java.util.Arrays;
import java.util.Locale;
import java.util.Random;

import de.fraunhofer.sit.sse.valbench.metadata.ValueComputationTestCase;

public class LoopTests {

	private static int field;

	@ValueComputationTestCase
	public static String findLoop() {
		String s = "http://abc";
		for (int i = 0; i < 3; i++)
			s += i;
		return s;
	}

	@ValueComputationTestCase
	public static String simpleLoopTest() {
		String start = "Start-";
		StringBuilder b = null;
		for (int i = 0; i < 5; i++) {
			if (i == 0)
				b = new StringBuilder(start);
			b.append(i);
		}
		b.append("-afterLoop");
		return b.toString();
	}

	@ValueComputationTestCase
	public static String simpleLoopTest2() {
		int[] x = new int[] { 5, 6, 7 };
		StringBuilder b = new StringBuilder();
		for (int i = 0; i < 10; i++) {
			x[i / 4] = i;
			b.append(x[i % x.length]);
		}
		return b.toString();
	}

	@ValueComputationTestCase
	public static String simpleLoopTest3() {
		StringBuilder b = new StringBuilder();
		for (int i = 0; i < 10; i++) {
			field = i;
			b.append(field);
		}
		return b.toString();
	}

	@ValueComputationTestCase
	public static String simpleLoopTestInstanceField() {
		StringBuilder b = new StringBuilder();
		Container c1 = new Container();
		Container c2 = new Container();
		for (int i = 0; i < 10; i++) {
			c1.value = i;
			c2.value = 10 - i;
			b.append(c1.value).append(c2.value);
		}
		return b.toString();
	}

	@ValueComputationTestCase
	public static String doubleTest() {
		return Double.toHexString(22.1) + ": " + Double.doubleToLongBits(2);
	}

	@ValueComputationTestCase
	public static String formatTest() {
		return String.format("Test: %d", 2);
	}

	@ValueComputationTestCase
	public static String formatTest2() {
		return String.format(Locale.US, "Test: %f", 2f);
	}

	@ValueComputationTestCase
	public static String formatTest3() {
		return String.format(Locale.GERMAN, "Test: %f", 2f);
	}


	static class Container {
		int value;
	}

	@ValueComputationTestCase
	public static String simpleLoopTestDouble() {
		double d = 0;
		String s = "";
		while (d < 5) {
			d += 0.4F;
			s += d;
		}
		return s;
	}

	@ValueComputationTestCase
	public static String simpleTestNewArrayAndReturn() {
		String s = "a";
		int i = 0;
		while (true) {
			int[] x = new int[21];
			x[i] = i * 2 + 4;
			if (i == 10)
				return s;

			s += x[i];
			i++;
		}
	}

	@ValueComputationTestCase
	public static String complexTest() {
		String s = "";
		int r = 0;
		for (int x = 1; x < 4; x++) {
			for (int y = 1; y < 4; y++) {
				while (((x + r++) % y) != 0)
					s += x + ", " + y + "\n";
			}
		}
		return s;
	}

	@ValueComputationTestCase
	public static String simpleMethodInvocation() {
		String s = "";
		for (int x = 1; x < 2; x++) {
			s += simpleLoopTest();
			s += simpleLoopTest2();
			s += pow(5, 6);
		}
		return s;
	}

	//Recursion ftw
	public static float pow(float a, float n) {
		float result = 0;

		if (n == 0) {
			return 1;
		} else if (n < 0) {
			result = result * pow(a, n + 1);
		} else if (n > 0) {
			result = result * pow(a, n - 1);
		}

		return result;
	}

	@ValueComputationTestCase
	public static String simpleBase() {
		Container c1 = new Container();
		Container c2 = new Container();
		for (int x = 1; x < 2; x++) {
			c2.value = 0;
			c1.value = x;
			increase(c1, 1);
			decrease(c2, 1);
		}
		return c1.value + ";" + c2.value;
	}

	private static void decrease(Container c, int i) {
		c.value -= i;
	}

	private static void increase(Container c, int i) {
		c.value += i;
	}

	@ValueComputationTestCase
	public static String stringbuilderInsanity() {
		StringBuilder s1 = new StringBuilder("s1");
		StringBuilder s2 = new StringBuilder("s2");
		StringBuilder s3 = new StringBuilder("s3");
		StringBuilder s4 = new StringBuilder("s4");

		s2.append("a");
		s3.append("y");
		s4.append("x");
		s1.append("r");

		StringBuilder s = null;
		Random rand = new Random(20);
		for (int i = 0; i < 4; i++) {
			switch (rand.nextInt(5)) {
			case 0:
				s = s1;
				break;
			case 1:
				s = s2;
				break;
			case 2:
				s = s3;
				break;
			case 3:
			default:
				s = s4;
				break;
			}
			s.append(i);
		}
		return s1.toString() + "|" + s2.toString() + "|" + s3.toString() + "|" + s4.toString();
	}

	@ValueComputationTestCase
	public static String lookupSwitch() {
		StringBuilder s = new StringBuilder();
		int x = 0;
		for (int i = 0; i < 100; i++) {
			switch (x) {
			case -5:
				s.append("-5");
				x = 0;
				break;
			case 0:
				s.append("0");
				x = 20;
				break;
			case 100000000:
				s.append("LARGE");
				x = -5;
				break;
			default:
				s.append("Default");
				x = Integer.MIN_VALUE;
				break;
			case Integer.MIN_VALUE:
				s.append("Min");
				x = Integer.MAX_VALUE;
				break;
			case Integer.MAX_VALUE:
				s.append("Max");
				x = 100000000;
				break;
			}
			s.append(i);
		}
		return s.toString();
	}
	
	static class A {
		public B b;
	}

	static class B {
		public String str;
	}

	@ValueComputationTestCase
	public static String skippedLoopIf() {
		String sUnrelated = "";
		int[] ab = new int[2];
		ab[1] = 2;
		int x = 0;
		boolean a = true;
		if (a)
			;
		else {
			while (x > 0) {
				sUnrelated += "";
			}
			ab[0] = 1;
		}
		return Arrays.toString(ab);
	}

	@ValueComputationTestCase
	public static String skippedLoopSwitch() {
		String sUnrelated = "";
		int[] ab = new int[2];
		ab[1] = 2;
		int x = 3;
		switch (x) {
		case 4:
			break;
		case 3:
			break;
		case 2:
		case 1:
			while (x > 0) {
				sUnrelated += "";
			}
			ab[0] = 1;
			break;
		}
		return Arrays.toString(ab);
	}

	@ValueComputationTestCase
	public static String callTestWithException() {
		String s = simplestCallTest3();
		for (int i = 0; i < 2; i++) {
			s += simplestCallTest3();
		}
		return s;
	}

	private static String callee1() {
		RuntimeException e = callee2();
		StackTraceElement r = e.getStackTrace()[0];
		return r.getMethodName() + "_" + r.getClassName();
	}

	private static RuntimeException callee2() {
		return new RuntimeException();
	}

	public static String simplestCallTest2() {
		return callee1();
	}

	public static String simplestCallTest3() {
		String s = "";
		for (int i = 0; i < 2; i++)
			s += "a";
		return s;
	}

}
