package de.fraunhofer.sit.sse.valbench.basictests;

import de.fraunhofer.sit.sse.valbench.metadata.ValueComputationTestCase;

public class TestBitwise {

	@ValueComputationTestCase
	public static String testBitwiseAnd() {
		int a = getOneNumber() & getSecondNumber();
		int b = (short) getOneNumber() & getSecondNumber();
		int c = (byte) getOneNumber() & (short) getSecondNumber();
		int d = (short) getOneNumber() & (short) getSecondNumber();
		int e = (byte) getOneNumber() & (byte) getSecondNumber();
		boolean r = getTrue() & getFalse();

		return a + "_" + b + "_" + c + "_" + d + "_" + r + "_" + e;
	}

	@ValueComputationTestCase
	public static String testLogicalXor() {
		boolean b = getTrue() ^ getFalse();
		return String.valueOf(b);
	}

	@ValueComputationTestCase
	public static String testBitwiseXor() {
		int a = getOneNumber() ^ getSecondNumber();
		int b = (short) getOneNumber() ^ getSecondNumber();
		int c = (byte) getOneNumber() ^ (short) getSecondNumber();
		int d = (short) getOneNumber() ^ (short) getSecondNumber();
		int e = (byte) getOneNumber() ^ (byte) getSecondNumber();
		boolean r = getTrue() ^ getFalse();

		return a + "_" + b + "_" + c + "_" + d + "_" + r + "_" + e;
	}

	@ValueComputationTestCase
	public static String testShiftL() {
		int a = getOneNumber() << 3;
		int b = (short) getOneNumber() << 3;
		int c = (byte) getOneNumber() << 3;
		int d = (short) getOneNumber() << 3;
		int e = (byte) getOneNumber() << 3;

		return a + "_" + b + "_" + c + "_" + d + "_" + e;
	}

	@ValueComputationTestCase
	public static String testAdd() {
		int a = getOneNumber() + getSecondNumber();
		int b = (short) getOneNumber() + getSecondNumber();
		int c = (byte) getOneNumber() + (short) getSecondNumber();
		int d = (short) getOneNumber() + (short) getSecondNumber();
		int e = (byte) getOneNumber() + (byte) getSecondNumber();

		return a + "_" + b + "_" + c + "_" + d + "_" + e;
	}

	@ValueComputationTestCase
	public static String testSubtract() {
		int a = getOneNumber() - getSecondNumber();
		int b = (short) getOneNumber() - getSecondNumber();
		int c = (byte) getOneNumber() - (short) getSecondNumber();
		int d = (short) getOneNumber() - (short) getSecondNumber();
		int e = (byte) getOneNumber() - (byte) getSecondNumber();
		double f = getOneNumberFloat() - getSecondNumberFloat();

		return a + "_" + b + "_" + c + "_" + d + "_" + e + "_" + f;
	}

	@ValueComputationTestCase
	public static String testMul() {
		byte ab = (byte) getOneNumber();
		short sh = (short) getSecondNumber();
		int c = ab * sh;
		int d = (short) getOneNumber() * (short) getSecondNumber();
		int a = getOneNumber() * getSecondNumber();
		int b = (short) getOneNumber() * getSecondNumber();
		int e = (byte) getOneNumber() * (byte) getSecondNumber();
		double f = getOneNumberFloat() * getSecondNumberFloat();

		return c + "_" + d + "_" + a + "_" + b + "_" + e + "_" + f;
	}

	@ValueComputationTestCase
	public static String testShr() {
		int a = getOneNumber() >> getSecondNumber();
		int b = (short) getOneNumber() >> getSecondNumber();
		int c = (byte) getOneNumber() >> (short) getSecondNumber();
		int d = (short) getOneNumber() >> (short) getSecondNumber();
		int e = (byte) getOneNumber() >> (byte) getSecondNumber();

		return a + "_" + b + "_" + c + "_" + d + "_" + e;
	}

	@ValueComputationTestCase
	public static String testAShr() {
		int a = getOneNumber() >>> getSecondNumber();
		int b = (short) getOneNumber() >>> getSecondNumber();
		int c = (byte) getOneNumber() >>> (short) getSecondNumber();
		int d = (short) getOneNumber() >>> (short) getSecondNumber();
		int e = (byte) getOneNumber() >>> (byte) getSecondNumber();

		return a + "_" + b + "_" + c + "_" + d + "_" + e;
	}

	@ValueComputationTestCase
	public static String testDiv() {
		int a = getOneNumber() / getSecondNumber();
		int b = (short) getOneNumber() / getSecondNumber();
		int c = (byte) getOneNumber() / (short) getSecondNumber();
		int d = (short) getOneNumber() / (short) getSecondNumber();
		int e = (byte) getOneNumber() / (byte) getSecondNumber();
		double f = getOneNumberFloat() / getSecondNumberFloat();

		return a + "_" + b + "_" + c + "_" + d + "_" + e + "_" + f;
	}

	@ValueComputationTestCase
	public static String testRem() {
		int a = getOneNumber() % getSecondNumber();
		int b = (short) getOneNumber() % getSecondNumber();
		int c = (byte) getOneNumber() % (short) getSecondNumber();
		int d = (short) getOneNumber() % (short) getSecondNumber();
		int e = (byte) getOneNumber() % (byte) getSecondNumber();
		double f = getOneNumberFloat() % getSecondNumberFloat();

		return a + "_" + b + "_" + c + "_" + d + "_" + e + "_" + f;
	}

	private static double getSecondNumberFloat() {
		return Float.MAX_VALUE + 1;
	}

	private static float getOneNumberFloat() {
		return 2000F;
	}

	private static boolean getTrue() {
		return true;
	}

	private static boolean getFalse() {
		return false;
	}

	private static int getOneNumber() {
		return 444;
	}

	private static int getSecondNumber() {
		return 999999;
	}
}
