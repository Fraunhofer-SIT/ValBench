package de.fraunhofer.sit.sse.valbench.modellingtests;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Random;

import de.fraunhofer.sit.sse.valbench.metadata.Requires;
import de.fraunhofer.sit.sse.valbench.metadata.ValueComputationTestCase;

@Requires(requiredClasses = { "de.fraunhofer.sit.sse.valbench.modellingtests.ReflectionTests" })
public class ReflectionTests {

	public static class ObjectHolder {
		public String o = "Foo";
		public int oInt = 3;
		public long oLong = Long.MAX_VALUE;
		public short oShort = 44;
		public byte oByte = -14;
		public float oFloat = 3.141F;
		public double oDouble = 3.14159D;
		public char oChar = 'x';
		public boolean oBoolean = true;

		public static String staticF = "init";

		public ObjectHolder() {

		}

		public ObjectHolder(Object o) {

		}

		public ObjectHolder(String r) {
			this.o = r;
		}
	}

	public static class A {
		public static String x() {
			return "A";
		}
	}

	public static class B {
		public static String x() {
			return "B";
		}
	}

	@ValueComputationTestCase(expectedValues = { "A", "B" })
	public static String testT()
			throws IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, InstantiationException, NoSuchFieldException {
		Class o;
		if (new Random().nextBoolean())
			o = A.class;
		else
			o = B.class;
		return o.getDeclaredMethod("x").invoke(null).toString();
	}

	@ValueComputationTestCase
	public static String testReflectionInReflectionLoop()
			throws IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, InstantiationException, NoSuchFieldException {
		// Check what happens if we need the field in the loop
		ObjectHolder h = new ObjectHolder();
		h.o = "Foo";
		String res = "";
		for (int i = 0; i < 2; i++) {
			res += ObjectHolder.class.getDeclaredField("o").get(h);
		}
		return res;
	}

	@ValueComputationTestCase
	public static String testReflectionInReflectionHard()
			throws IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, InstantiationException {
		return ReflectionTests.class.getDeclaredMethod("redirect").invoke(null).toString();
	}

	public static String redirect() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException {
		return ReflectionTests.class.getDeclaredMethod("redirect2", String.class).invoke(null, "Test").toString();
	}

	public static String redirect2(String s) throws IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		return ReflectionTests.class.getDeclaredMethod("redirect3", String.class)
				.invoke(new ReflectionTests(), s + "bar").toString();
	}

	public String redirect3(String s) throws IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		return "Wow" + s;
	}

	@ValueComputationTestCase
	public static String testReflectionConstructorArgs2()
			throws IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, InstantiationException {
		return String.class.getDeclaredConstructor(byte[].class, String.class)
				.newInstance(new byte[] { 1, 2, 3, 50, 120 }, "UTF-8");
	}

	@ValueComputationTestCase
	public static String testReflectionArrayCopy() throws IOException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException, InstantiationException {
		String s = "Fjafiaoiowqei913i31udaejöl";
		byte[] b = s.getBytes("UTF-8");
		byte[] res = new byte[40];
		System.class.getDeclaredMethod("arraycopy", Object.class, int.class, Object.class, int.class, int.class)
				.invoke(null, b, 3, res, 1, 2);
		return Arrays.toString(res);
	}

	@ValueComputationTestCase
	public static String testReflectionConstructorArgs()
			throws IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, InstantiationException {
		ObjectHolder o = ObjectHolder.class.getDeclaredConstructor(String.class).newInstance("XYZ");
		o.o += "Bar";
		return String.valueOf(o.o);
	}

	@ValueComputationTestCase
	public static String testReflectionConstructorArgs3()
			throws IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, InstantiationException {
		ObjectHolder o = ObjectHolder.class.getDeclaredConstructor().newInstance();
		o.o += "Bar";
		return String.valueOf(o.o);
	}

	@ValueComputationTestCase
	public static String testReflectionConstructor()
			throws IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, InstantiationException {
		ObjectHolder o = ObjectHolder.class.newInstance();
		o.o += "Bar";
		return String.valueOf(o.o);
	}

	@ValueComputationTestCase
	public static String testReflectionFieldAccessSet()
			throws IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, InstantiationException, NoSuchFieldException {
		ObjectHolder o = ObjectHolder.class.newInstance();
		o.getClass().getDeclaredField("o").set(o, "Noo");
		return String.valueOf(o.o);
	}

	@ValueComputationTestCase(expectedValues = { "xx" })
	public static String testNotExistingField()
			throws IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, InstantiationException, NoSuchFieldException {
		ObjectHolder o = new ObjectHolder("xx");
		try {
			o.getClass().getDeclaredField("ofoo").set(o, "Noo");
		} catch (Throwable t) {
		}
		return String.valueOf(o.o);
	}

	@ValueComputationTestCase
	public static String testReflectionBFieldAccessGetOtherTypes()
			throws IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, InstantiationException, NoSuchFieldException {
		ObjectHolder o = new ObjectHolder("FooTest123");
		Field fInt = o.getClass().getDeclaredField("oInt");
		return "" + fInt.getDouble(o) + "_" + fInt.getLong(o);
	}

	@ValueComputationTestCase
	public static String testReflectionFieldAccessGetOtherTypes()
			throws IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, InstantiationException, NoSuchFieldException {
		ObjectHolder o = new ObjectHolder("FooTest123");
		Field fInt = o.getClass().getDeclaredField("oInt");
		Field fFloat = o.getClass().getDeclaredField("oFloat");
		Field fDouble = o.getClass().getDeclaredField("oDouble");
		Field fBoolean = o.getClass().getDeclaredField("oBoolean");
		Field fChar = o.getClass().getDeclaredField("oChar");
		Field fShort = o.getClass().getDeclaredField("oShort");
		Field fLong = o.getClass().getDeclaredField("oLong");
		Field fByte = o.getClass().getDeclaredField("oByte");
		return "" + fInt.getInt(o) + fFloat.getFloat(o) + fBoolean.getBoolean(o) + fChar.getChar(o) + fShort.getShort(o)
				+ fLong.getLong(o) + fByte.getByte(o) + fDouble.getDouble(o) + fInt.getDouble(o) + fInt.getLong(o);
	}

	@ValueComputationTestCase
	public static String testReflectionFieldAccessSetAll()
			throws IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, InstantiationException, NoSuchFieldException {
		ObjectHolder o = new ObjectHolder("FooTest123");
		Object v = null;
		for (Field f : o.getClass().getDeclaredFields()) {
			Class<?> t = f.getType();
			if (t == int.class) {
				v = 2;
				f.set(o, v);
			}
		}
		return "" + o.oInt;
	}

	@ValueComputationTestCase
	public static String testReflectionFieldAccessGet()
			throws IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, InstantiationException, NoSuchFieldException {
		ObjectHolder o = new ObjectHolder("FooTest123");
		return o.getClass().getDeclaredField("o").get(o).toString();
	}

	@ValueComputationTestCase
	public static String testReflectionFieldAccessGetUnused()
			throws IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, InstantiationException, NoSuchFieldException {
		ObjectHolder o = new ObjectHolder("FooTest123");
		o.getClass().getDeclaredField("o").get(o);

		return o.o;
	}

	@ValueComputationTestCase
	public static String testReflectionModelStaticFieldAccessGet() throws IOException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException,
			InstantiationException, NoSuchFieldException, ClassNotFoundException {
		Class clM = Class.forName("java.lang.Math");
		return clM.getDeclaredField("PI").getDouble(null) + "";
	}

	@ValueComputationTestCase
	public static String testReflectionFieldAccessGetSet()
			throws IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, InstantiationException, NoSuchFieldException {
		ObjectHolder o = ObjectHolder.class.newInstance();
		Field f = o.getClass().getDeclaredField("o");
		f.set(o, "Noo");
		return String.valueOf(f.get(o));
	}

	@ValueComputationTestCase
	public static String testReflectionStaticFieldAccessGetSet()
			throws IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, InstantiationException, NoSuchFieldException {
		ObjectHolder o = ObjectHolder.class.newInstance();
		Field f = o.getClass().getDeclaredField("staticF");
		f.set(null, "Noo");
		return String.valueOf(f.get(null));
	}

	@ValueComputationTestCase
	public static String testReflection() throws IOException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		double d = (double) Math.class.getDeclaredMethod("pow", double.class, double.class).invoke(null, 2, 4);
		return String.valueOf(d);
	}

	@ValueComputationTestCase
	public static String testParamTypes() throws IOException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		return Arrays.toString(Math.class.getDeclaredMethod("pow", double.class, double.class).getParameterTypes());
	}

	@ValueComputationTestCase
	public static String testVoidCall() throws IOException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		ObjectHolder o = new ObjectHolder();

		o.o = ReflectionTests.class.getDeclaredMethod("voidMethod", ObjectHolder.class).invoke(null, o) + o.o;
		return o.o;
	}

	@ValueComputationTestCase
	public static String testNotExistingCall() throws IOException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		ObjectHolder o = new ObjectHolder();
		o.o = "Foo";
		try {
			ReflectionTests.class.getDeclaredMethod("notExistingMethod", ObjectHolder.class).invoke(null, o);

		} catch (Exception e) {
		}
		return o.o;
	}

	public static void voidMethod(ObjectHolder h) {
		h.o = "Test";
	}

	@ValueComputationTestCase
	public static String testArrayDoubleReflection() throws IOException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		int[][] ia = new int[2][2];
		String[][][] sa = new String[2][2][2];
		sa[0][0][0] = "First";
		sa[0][0][1] = "Second";
		ia[0][0] = 255;
		String[] d = (String[]) ReflectionTests.class
				.getDeclaredMethod("arrayDouble", int[][].class, String[][][].class).invoke(null, ia, sa);
		String s = Arrays.toString(d) + "_";
		return s + sa[0][0][1] + ia[0][0];
	}

	public static String[] arrayDouble(int[][] x, String[][]... y) {
		return new String[] { y[0][0][0] + x[0][0] };
	}
}
