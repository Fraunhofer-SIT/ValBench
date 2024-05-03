package de.fraunhofer.sit.sse.valbench.basictests;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import de.fraunhofer.sit.sse.valbench.metadata.ValueComputationTestCase;

public class PolymorphicTests {

	static a argh = new b();

	@ValueComputationTestCase
	public static String testSimple() throws UnsupportedEncodingException {
		return argh.getString().toString();
	}

	static class a {
		public CharSequence getString() {
			return "Wrong";
		}
	}

	static class b extends a {
		@Override
		public String getString() {
			return "Right";
		}
	}

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
	public static String test1() throws IOException {
		Base r = ((Base) get(0, x("Def")));
		return r.get();
	}

	private static String x(String string) {
		return string + "x";
	}

	@ValueComputationTestCase
	public static String test2() throws IOException {
		return ((Base) get(1, x("x"))).get();
	}

	@ValueComputationTestCase
	public static String test3() throws IOException {
		return ((Base) get(2, x("x"))).get();
	}

	@ValueComputationTestCase
	public static String test4() throws IOException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		Base m = ((Base) get(0, x("x")));
		return Base.class.getDeclaredMethod("get").invoke(m).toString();
	}

	@ValueComputationTestCase
	public static String test5() throws IOException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		Base m = ((Base) get(1, x("x")));
		return Base.class.getDeclaredMethod("get").invoke(m).toString();
	}

	@ValueComputationTestCase
	public static String test6() throws IOException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		Base m = ((Base) get(2, x("x")));
		return Base.class.getDeclaredMethod("get").invoke(m).toString();
	}

	@ValueComputationTestCase
	public static String testReflection7() throws IOException, NoSuchMethodException, SecurityException,
			InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Base b = getReflection(0, x("x"));
		return b.get();
	}

	@ValueComputationTestCase
	public static String testReflection8() throws IOException, NoSuchMethodException, SecurityException,
			InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Base b = getReflection(1, x("x"));
		return b.get();
	}

	private static Base getReflection(int i, String x) throws NoSuchMethodException, SecurityException,
			InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Constructor<? extends Base> m = null;
		switch (i) {
		case 0:
			m = A.class.getDeclaredConstructor(String.class);
			break;
		case 1:
		default:
			m = B.class.getDeclaredConstructor(String.class);
			break;
		}
		return m.newInstance(x);
	}

	@ValueComputationTestCase
	public static String testDefaultMethod() {
		return getImpl().doStr();
	}

	private static Foo getImpl() {
		return new Bar();
	}

	interface Foo {
		default String doStr() {
			return "Right";
		}
	}

	static class Bar implements Foo {

	}
}
