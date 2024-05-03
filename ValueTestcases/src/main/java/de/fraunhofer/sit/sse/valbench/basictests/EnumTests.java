package de.fraunhofer.sit.sse.valbench.basictests;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import de.fraunhofer.sit.sse.valbench.metadata.ValueComputationTestCase;

public class EnumTests {

	public enum Foo {
		a, b;

		public String test;
	}

	private static int foo;

	public static void reset() {
		Foo.a.test = null;
		Foo.b.test = null;
	}

	@ValueComputationTestCase
	public static String testSimpleEnumField()
			throws IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, InstantiationException, NoSuchFieldException {
		Foo.a.test = "Foo";
		String x = getATest();
		return x;
	}

	@ValueComputationTestCase
	public static String testSimpleEnumField2()
			throws IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, InstantiationException, NoSuchFieldException {
		Foo.a.test = "a";
		Foo.b.test = "b";
		Foo.a.test = "a";
		for (int i = 0; i < 5; i++) {
			Foo.b.test += "a";
		}
		String x = Foo.b.test;
		return x;
	}

	private static String skipMe(int i) {
		String o = "";
		for (int x = 0; x < i * 5; x++)
			o += x;
		return o;
	}

	private static String getATest() {
		return Foo.a.test;
	}

	@ValueComputationTestCase
	public static String testSimpleEnum()
			throws IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, InstantiationException, NoSuchFieldException {
		return Foo.a.name();
	}

	@ValueComputationTestCase
	public static String testSimpleEnum2()
			throws IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, InstantiationException, NoSuchFieldException {
		return Foo.a.toString();
	}

	@ValueComputationTestCase
	public static String testSimpleEnum5()
			throws IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, InstantiationException, NoSuchFieldException {
		return "" + Foo.a.ordinal();
	}

	@ValueComputationTestCase
	public static String testSimpleEnum3()
			throws IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, InstantiationException, NoSuchFieldException {
		return Arrays.toString(Foo.values());
	}

	@ValueComputationTestCase
	public static String testSimpleEnum4()
			throws IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, InstantiationException, NoSuchFieldException {
		return Foo.valueOf("b").name();
	}

	@ValueComputationTestCase
	public static String testSimpleEnumSwitch()
			throws IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, InstantiationException, NoSuchFieldException {
		Foo foo = getFooA();
		String s = "";
		switch (foo) {
		case a:
			s = "A" + foo;
			break;
		default:
		case b:
			s = "B" + foo;
			break;
		}
		return s;
	}

	private static Foo getFooA() {
		return Foo.a;
	}

	@ValueComputationTestCase
	public static String testSimpleEnum6()
			throws IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, InstantiationException, NoSuchFieldException {
		return Foo.valueOf("b").ordinal() + "";
	}

	static String a = "Ff";

	static int needed;

	private static int needed2;
	private static int needed3;

	private static int reallyUnneeded;

	private static boolean neededIf = true;

	private static int loopElements;

	static class FooClass {
		int n;
		public int r;
	}

	@ValueComputationTestCase
	public static String testLoop()
			throws IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, InstantiationException, NoSuchFieldException {
		neededIf = false;
		loopElements = 20000;
		needed = 10000000; // we need that
		needed3 = 1000; // 599;
		FooClass o = new FooClass();
		o.n = 3;

		int unneeded = -1;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 5; i++) {
			sb.append(foo(i + 1, o));
			set();
		}
		sb.append(needed2 + o.r);
		unneeded = Math.max(unneeded, unneeded);
		return sb.toString();

	}

	@ValueComputationTestCase
	public static String testLoop2()
			throws IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, InstantiationException, NoSuchFieldException {
		int unneeded = -1;
		needed2 = 50;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 5; i++) {
			int y = i + 1;
			y = y * 2;

			int z = Math.max(y, y);
			sb.append(z);
		}
		sb.append(needed2);
		unneeded = Math.max(unneeded, unneeded);
		return sb.toString();

	}

	private static void set() {
		needed2 = needed3;
	}

	private static int foo(int unneeded, FooClass o) {
		x(o);
		return unneeded + 4 + 2 + 4 + 5 + needed * 2; // we need that
	}

	private static int x(FooClass o) {
		o.r = o.n;
		int foo = 5;
		reallyUnneeded = o.r + o.n + reallyUnneeded;
		FooClass r = new FooClass();
		r.r = r.n = reallyUnneeded;
		if (neededIf) {
			o.r += 5;
		} else
			o.r += 2;
		for (int i = 0; i < 5; i++) {
			o.r += 2 + foo;
		}

		return reallyUnneeded;
	}

}
