package de.fraunhofer.sit.sse.valbench.modellingtests;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import org.apache.commons.io.IOUtils;

import de.fraunhofer.sit.sse.valbench.metadata.Requires;
import de.fraunhofer.sit.sse.valbench.metadata.ValueComputationTestCase;

@Requires(requiredClasses = { "DefaultPackageClass" } )
public class ClassTests {
	static ClassTests r = new ClassTests() {
		//anonymous inner class
	};

	@Retention(RetentionPolicy.RUNTIME)
	public static @interface TestAnnotation {
		boolean booleanTest() default true;

	}

	static enum e {

	}

	static abstract interface i {
		public static class inner_inner_interface {

			public interface inner_inner_inner {

			}
		}
	}

	static class inner {

		public static class inner_inner_class {

		}
	}

	static Class defPackage;
	static {
		try {
			defPackage = Class.forName("DefaultPackageClass");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	static final Class[] cl = new Class[] { Void.class, long.class, double.class, short.class, boolean.class,
			float.class, void.class, int.class, int[].class, int[][].class, Integer.class, r.getClass(),
			ClassTests.class, inner.class, e.class, i.class, i.inner_inner_interface.class,
			i.inner_inner_interface[].class, inner.inner_inner_class.class,
			i.inner_inner_interface.inner_inner_inner.class, i.inner_inner_interface.inner_inner_inner[].class,
			i.inner_inner_interface.inner_inner_inner[][].class, i.inner_inner_interface.inner_inner_inner[][][].class,
			defPackage, TestAnnotation.class };

	@ValueComputationTestCase
	public static String testClasses1() throws IOException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		String s = "";
		for (Class c : cl)
			s += c.getCanonicalName() + "\n";
		return s;
	}

	@ValueComputationTestCase
	public static String testClasses2() throws IOException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		String s = "";
		for (Class c : cl)
			s += c.getName() + "\n";
		return s;
	}

	@ValueComputationTestCase
	public static String testClasses4() throws IOException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		String s = "";
		for (Class c : cl)
			s += c.getSimpleName() + "\n";
		return s;
	}

	@ValueComputationTestCase
	public static String testClasses5() throws IOException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		String s = "";
		for (Class c : cl)
			s += c.getTypeName() + "\n";
		return s;
	}

	@ValueComputationTestCase
	public static String testClasses6() throws IOException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		String s = "";
		for (Class c : cl) {
			if (c.getSuperclass() != null)
				s += c.getName() + " -> " + c.getSuperclass().getName() + "\n";
		}
		return s;
	}

	@ValueComputationTestCase
	public static String testClasses7() throws IOException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		String s = "";
		for (Class c : cl)
			//The static modifier is not encoded as normal modifier in case of classes
			//https://docs.oracle.com/javase/specs/jvms/se9/html/jvms-4.html#jvms-4.7.25
			//For now, it is unclear to me how it works, therefore we ignore it here for now.
			s += c.toGenericString().replace("static ", "") + "\n";
		return s;
	}

	@ValueComputationTestCase
	public static String testClasses8() throws IOException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		String s = "";
		for (Class c : cl)
			s += c.toString() + "\n";
		return s;
	}

	@ValueComputationTestCase
	public static String testClasses9() throws IOException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		String s = "";
		for (Class c : cl)
			s += c.isArray() + "\n";
		return s;
	}

	@ValueComputationTestCase
	public static String testCast() throws IOException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		String s = "";
		s += Long.class.cast(4L);
		s += Integer.class.cast(4);
		s += Boolean.class.cast(true);
		s += String.class.cast("Foo");
		s += CharSequence.class.cast("Foo");
		s += Arrays.toString(String[].class.cast(new String[] { "x", "y" }));
		return s;
	}

	@ValueComputationTestCase
	public static String testClasses13() throws Exception {
		Object[] o1 = new Object[2];
		return toArrayType(o1);
	}

	private static String toArrayType(Object o) {
		return new Object[] { o }.getClass().getName();
	}

	@ValueComputationTestCase
	public static String testPackages() throws Exception {
		String s = "";
		for (Class c : cl) {
			if (c.getPackage() == null)
				continue;
			s += c.getPackage().getName() + "\n";
		}
		return s;
	}

	@ValueComputationTestCase
	public static String testPackages2() throws Exception {
		String s = "";
		for (Class c : cl) {
			if (c.getPackage() == null)
				continue;
			String r = c.getPackage().getName();
			s += Package.getPackage(r) + "\n";
		}
		return s;
	}

	@ValueComputationTestCase
	public static String testIsInstance() throws Exception {
		String s = "";
		s += String.class.isInstance("Test") + "\n";
		s += int.class.isInstance(4) + "\n";
		s += Integer.class.isInstance(4) + "\n";
		s += int[].class.isInstance(new int[] { 1, 2 }) + "\n";
		s += Integer[].class.isInstance(new int[] { 1, 2 }) + "\n";
		s += int[].class.isInstance(new Integer[] { 1, 2 }) + "\n";
		s += Integer[].class.isInstance(new Integer[] { 1, 2 }) + "\n";
		return s;
	}

	@ValueComputationTestCase
	public static String testClassesGetClass() throws IOException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException, InstantiationException {
		String s = "";
		for (Class c : cl) {
			if (c == void.class)
				continue;
			s += Array.newInstance(c, 1).getClass().getCanonicalName() + "\n";
		}
		return s;
	}

	@ValueComputationTestCase
	public static String testClassesGetClass2() throws IOException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException, InstantiationException {
		String s = "";
		for (Object o : new Object[] { "x", 3, 3D, 9F, (short) 3, (byte) 2, true }) {
			s += o.getClass().getCanonicalName() + "\n";
		}
		return s;
	}

	@ValueComputationTestCase
	public static String testClassesGetClass3()
			throws IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, InstantiationException, NoSuchFieldException {
		return Math.class.getDeclaredField("PI").get(null).getClass().getCanonicalName();
	}

	static class A {
		Object o;
		float f;
	}

	@ValueComputationTestCase
	public static String testClassesGetClass5()
			throws IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, InstantiationException, NoSuchFieldException {
		A a = new A();
		a.getClass().getDeclaredField("o").set(a, 4.2F);
		a.getClass().getDeclaredField("f").set(a, 4.2F);
		return a.o.getClass().getCanonicalName()
				+ a.getClass().getDeclaredField("o").get(a).getClass().getCanonicalName()
				+ a.getClass().getDeclaredField("f").get(a).getClass().getCanonicalName();
	}

	@ValueComputationTestCase
	public static String testClassesGetClass4()
			throws IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, InstantiationException, NoSuchFieldException {
		return Math.class.getDeclaredMethod("pow", double.class, double.class).invoke(null, 1, 2).getClass()
				.getCanonicalName();
	}

	@ValueComputationTestCase
	public static String testClassesGetClass6()
			throws IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, InstantiationException, NoSuchFieldException {
		return ClassTests.class.getDeclaredMethod("getConstant").invoke(null).getClass().getCanonicalName();
	}

	@ValueComputationTestCase
	public static String testClassesGetClassGetResource3() throws Exception {
		InputStream s = ClassTests.class.getClassLoader()
				.getResourceAsStream("de/fraunhofer/sit/sse/valbench/modellingtests/TestResource");
		return Arrays.toString(IOUtils.toByteArray(s));
	}

	private final static Class[] c = new Class[] { String.class, int.class, Integer.class, int[].class,
			Integer[].class };

	@ValueComputationTestCase
	public static String findStaticFields() {
		return Arrays.toString(c);
	}

	public static float getConstant() {
		return 3.14159F;
	}

}
