package de.fraunhofer.sit.sse.valbench.basictests;

import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import de.fraunhofer.sit.sse.valbench.metadata.ValueComputationTestCase;

public class AnnotationTests {

	@Retention(RetentionPolicy.RUNTIME)
	@interface TestAnnotation2 {

		String str()

		default "Bar";

		int i();
	}

	@Retention(RetentionPolicy.RUNTIME)
	@interface TestAnnotation {
		boolean booleanTest() default true;

		int intTest() default 42;

		byte byteTest() default -12;

		short shortTest() default 55;

		String stringTest() default "Foo";

		float floatTest() default 1.2F;

		double doubleTest() default 1.3;

		TestAnnotation2 simpleClassTest() default @TestAnnotation2(i = 8);

		byte[] byteArrayTest() default { 1, 2 };

		String[] stringArrayTest() default { "a", "b" };

		TestAnnotation2[] classTest() default { @TestAnnotation2(str = "x", i = 3), @TestAnnotation2(i = 4) };

		Foo enumTest() default Foo.a;

	}

	public enum Foo {
		a, b
	}

	@TestAnnotation(enumTest = Foo.b, booleanTest = false)
	public static class ClassWithAnnotations {
	}

	@TestAnnotation
	public static class DefaultClassWithAnnotations {
	}

	@ValueComputationTestCase
	public static String testSimpleAnnotationArray()
			throws IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, InstantiationException, NoSuchFieldException {
		TestAnnotation a = ClassWithAnnotations.class.getDeclaredAnnotation(TestAnnotation.class);
		return Arrays.toString(a.stringArrayTest());
	}

	@ValueComputationTestCase
	public static String testSimpleAnnotationArray2()
			throws IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, InstantiationException, NoSuchFieldException {
		TestAnnotation a = ClassWithAnnotations.class.getDeclaredAnnotation(TestAnnotation.class);
		return Arrays.toString(a.byteArrayTest());
	}

	@ValueComputationTestCase
	public static String testSimpleAnnotationEnum()
			throws IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, InstantiationException, NoSuchFieldException {
		TestAnnotation a = ClassWithAnnotations.class.getDeclaredAnnotation(TestAnnotation.class);
		return String.valueOf(a.enumTest());
	}

	@ValueComputationTestCase
	public static String testSimpleAnnotationEnum2()
			throws IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, InstantiationException, NoSuchFieldException {
		TestAnnotation a = DefaultClassWithAnnotations.class.getDeclaredAnnotation(TestAnnotation.class);
		return String.valueOf(a.enumTest());
	}

	@ValueComputationTestCase
	public static String testSimpleAnnotation()
			throws IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, InstantiationException, NoSuchFieldException {
		TestAnnotation a = ClassWithAnnotations.class.getDeclaredAnnotation(TestAnnotation.class);
		return String.valueOf(a.booleanTest()) + a.byteTest() + a.doubleTest() + a.floatTest() + a.intTest()
				+ a.shortTest() + a.stringTest() + a.simpleClassTest().str() + a.simpleClassTest().i()
				+ a.classTest()[0].str() + a.classTest()[0].i() + a.classTest()[1].str() + a.classTest()[1].i();
	}

	@ValueComputationTestCase
	public static String testSimpleAnnotationClass()
			throws IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, InstantiationException, NoSuchFieldException {
		return DefaultClassWithAnnotations.class.getDeclaredAnnotation(TestAnnotation.class).annotationType().getName()
				+ "_"
				+ ClassWithAnnotations.class.getDeclaredAnnotation(TestAnnotation.class).annotationType().getName();
	}

	@ValueComputationTestCase
	public static String testSimpleAnnotationDefault()
			throws IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, InstantiationException, NoSuchFieldException {
		return String.valueOf(DefaultClassWithAnnotations.class.getDeclaredAnnotation(TestAnnotation.class).intTest());
	}

	@ValueComputationTestCase
	public static String testSimpleAnnotation2()
			throws IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, InstantiationException, NoSuchFieldException {
		return String.valueOf(ClassWithAnnotations.class.getAnnotation(TestAnnotation.class).booleanTest());
	}

}
