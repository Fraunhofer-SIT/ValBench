package de.fraunhofer.sit.sse.valbench.modellingtests;

import java.math.BigInteger;

import de.fraunhofer.sit.sse.valbench.metadata.ValueComputationTestCase;

public class InstanceOfTests {
	@ValueComputationTestCase
	public static String testInstanceOf1() {
		return String.valueOf("" instanceof CharSequence);
	}

	@ValueComputationTestCase
	public static String testInstanceOf2() {
		Object o = "";
		return Boolean.toString(o instanceof Integer);
	}

	@ValueComputationTestCase
	public static String testInstanceOf3() {
		Object o = 4;
		return Boolean.toString(o instanceof Integer);
	}

	static class a {

	}

	static class b extends a {

	}

	static interface i {

	}

	static class c implements i {

	}

	@ValueComputationTestCase
	public static String testInstanceOf4() {
		return Boolean.toString(new a() instanceof b);
	}

	@ValueComputationTestCase
	public static String testInstanceOf5() {
		return Boolean.toString(new b() instanceof a);
	}

	@ValueComputationTestCase
	public static String testInstanceOf6() {
		return Boolean.toString(new b() instanceof i);
	}

	@ValueComputationTestCase
	public static String testInstanceOf7() {
		return Boolean.toString(new c() instanceof i);
	}

	@ValueComputationTestCase
	public static String testInstanceOf8() {
		return Boolean.toString(new BigInteger("2").add(new BigInteger("3")) instanceof Number);
	}

	@ValueComputationTestCase
	public static String testInstanceOf9() {
		int[] a = new int[2];
		return Boolean.toString(a instanceof int[]);
	}

	@ValueComputationTestCase
	public static String testInstanceOf10() {
		Object a = new int[2];
		return Boolean.toString(a instanceof Integer[]);
	}

	@ValueComputationTestCase
	public static String testInstanceOf11() {
		Object a = new Integer[2];
		return Boolean.toString(a instanceof Integer[]);
	}

	@ValueComputationTestCase
	public static String testInstanceOf12() {
		Object a = new Integer[2];
		return Boolean.toString(a instanceof int[]);
	}

	@ValueComputationTestCase
	public static String testInstanceOf13() {
		Object a = new Integer[2][2];
		return Boolean.toString(a instanceof int[][]);
	}

}
