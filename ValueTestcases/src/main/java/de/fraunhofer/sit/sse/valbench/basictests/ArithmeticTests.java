package de.fraunhofer.sit.sse.valbench.basictests;

import de.fraunhofer.sit.sse.valbench.metadata.ValueComputationTestCase;

public class ArithmeticTests {

	
 
	@ValueComputationTestCase
	public static String testShiftLeft() {
		String s = "";
		for (int i = -30; i <= 30; i++) {
			s += (Integer.MAX_VALUE << i) + "_";
			s += (Integer.MIN_VALUE << i) + "_";
			s += (Short.MAX_VALUE << (short) i) + "_";
			s += (Short.MIN_VALUE << (short) i) + "_";
			s += (Byte.MAX_VALUE << (byte) i) + "_";
			s += (Byte.MIN_VALUE << (byte) i) + "_";
			s += (Character.MAX_VALUE << (char) i) + "_";
			s += (Character.MIN_VALUE << (char) i) + "_";
			s += (Long.MAX_VALUE << i) + "_";
			s += (Long.MIN_VALUE << i) + "_";

		}
		return s;
	}

	@ValueComputationTestCase
	public static String testShiftRight() {
		String s = "";
		for (int i = -30; i <= 30; i++) {
			s += (Integer.MAX_VALUE >> i) + "_";
			s += (Integer.MIN_VALUE >> i) + "_";
			s += (Short.MAX_VALUE >> (short) i) + "_";
			s += (Short.MIN_VALUE >> (short) i) + "_";
			s += (Byte.MAX_VALUE >> (byte) i) + "_";
			s += (Byte.MIN_VALUE >> (byte) i) + "_";
			s += (Character.MAX_VALUE >> (char) i) + "_";
			s += (Character.MIN_VALUE >> (char) i) + "_";
			s += (Long.MAX_VALUE >> i) + "_";
			s += (Long.MIN_VALUE >> i) + "_";

		}
		return s;
	}

	@ValueComputationTestCase
	public static String testShiftRight2() {
		String s = "";
		for (int i = -30; i <= 30; i++) {
			s += (Integer.MAX_VALUE >>> i) + "_";
			s += (Integer.MIN_VALUE >>> i) + "_";
			s += (Short.MAX_VALUE >>> (short) i) + "_";
			s += (Short.MIN_VALUE >>> (short) i) + "_";
			s += (Byte.MAX_VALUE >>> (byte) i) + "_";
			s += (Byte.MIN_VALUE >>> (byte) i) + "_";
			s += (Character.MAX_VALUE >>> (char) i) + "_";
			s += (Character.MIN_VALUE >>> (char) i) + "_";
			s += (Long.MAX_VALUE >>> i) + "_";
			s += (Long.MIN_VALUE >>> i) + "_";

		}
		return s;
	}

	@ValueComputationTestCase
	public static String testXor() {
		String s = "";
		for (int i = -10; i <= 10; i++) {
			s += (Integer.MAX_VALUE ^ i) + "_";
			s += (Integer.MIN_VALUE ^ i) + "_";
			s += (Short.MAX_VALUE ^ (short) i) + "_";
			s += (Short.MIN_VALUE ^ (short) i) + "_";
			s += (Byte.MAX_VALUE ^ (byte) i) + "_";
			s += (Byte.MIN_VALUE ^ (byte) i) + "_";
			s += (Character.MAX_VALUE ^ (char) i) + "_";
			s += (Character.MIN_VALUE ^ (char) i) + "_";
			s += (Long.MAX_VALUE ^ i) + "_";
			s += (Long.MIN_VALUE ^ i) + "_";

		}
		return s;
	}

	@ValueComputationTestCase
	public static String testAnd() {
		String s = "";
		for (int i = -10; i <= 10; i++) {
			s += (Integer.MAX_VALUE & i) + "_";
			s += (Integer.MIN_VALUE & i) + "_";
			s += (Short.MAX_VALUE & (short) i) + "_";
			s += (Short.MIN_VALUE & (short) i) + "_";
			s += (Byte.MAX_VALUE & (byte) i) + "_";
			s += (Byte.MIN_VALUE & (byte) i) + "_";
			s += (Character.MAX_VALUE & (char) i) + "_";
			s += (Character.MIN_VALUE & (char) i) + "_";
			s += (Long.MAX_VALUE & i) + "_";
			s += (Long.MIN_VALUE & i) + "_";

		}
		return s;
	}

	@ValueComputationTestCase
	public static String testOr() {
		String s = "";
		for (int i = -10; i <= 10; i++) {
			s += (Integer.MAX_VALUE | i) + "_";
			s += (Integer.MIN_VALUE | i) + "_";
			s += (Short.MAX_VALUE | (short) i) + "_";
			s += (Short.MIN_VALUE | (short) i) + "_";
			s += (Byte.MAX_VALUE | (byte) i) + "_";
			s += (Byte.MIN_VALUE | (byte) i) + "_";
			s += (Character.MAX_VALUE | (char) i) + "_";
			s += (Character.MIN_VALUE | (char) i) + "_";
			s += (Long.MAX_VALUE | i) + "_";
			s += (Long.MIN_VALUE | i) + "_";

		}
		return s;
	}

	@ValueComputationTestCase
	public static String testMod() {
		String s = "";
		for (int i = -10; i <= 10; i++) {
			if (i == 0)
				continue;
			s += (Integer.MAX_VALUE % i) + "_";
			s += (Integer.MIN_VALUE % i) + "_";
			s += (Short.MAX_VALUE % (short) i) + "_";
			s += (Short.MIN_VALUE % (short) i) + "_";
			s += (Byte.MAX_VALUE % (byte) i) + "_";
			s += (Byte.MIN_VALUE % (byte) i) + "_";
			s += (Character.MAX_VALUE % (char) i) + "_";
			s += (Character.MIN_VALUE % (char) i) + "_";
			s += (Long.MAX_VALUE % i) + "_";
			s += (Long.MIN_VALUE % i) + "_";

		}
		return s;
	}

	@ValueComputationTestCase
	public static String testAdd() {
		String s = "";
		for (int i = -10; i <= 10; i++) {
			s += (Integer.MAX_VALUE + i) + "_";
			s += (Integer.MIN_VALUE + i) + "_";
			s += (Short.MAX_VALUE + (short) i) + "_";
			s += (Short.MIN_VALUE + (short) i) + "_";
			s += (Byte.MAX_VALUE + (byte) i) + "_";
			s += (Byte.MIN_VALUE + (byte) i) + "_";
			s += (Character.MAX_VALUE + (char) i) + "_";
			s += (Character.MIN_VALUE + (char) i) + "_";
			s += (Long.MAX_VALUE + i) + "_";
			s += (Long.MIN_VALUE + i) + "_";

		}
		return s;
	}

	@ValueComputationTestCase
	public static String testSub() {
		String s = "";
		for (int i = -10; i <= 10; i++) {
			s += (Integer.MAX_VALUE - i) + "_";
			s += (Integer.MIN_VALUE - i) + "_";
			s += (Short.MAX_VALUE - (short) i) + "_";
			s += (Short.MIN_VALUE - (short) i) + "_";
			s += (Byte.MAX_VALUE - (byte) i) + "_";
			s += (Byte.MIN_VALUE - (byte) i) + "_";
			s += (Character.MAX_VALUE - (char) i) + "_";
			s += (Character.MIN_VALUE - (char) i) + "_";
			s += (Long.MAX_VALUE - i) + "_";
			s += (Long.MIN_VALUE - i) + "_";

		}
		return s;
	}

	@ValueComputationTestCase
	public static String testMul() {
		String s = "";
		for (int i = -10; i <= 10; i++) {
			s += (Integer.MAX_VALUE * i) + "_";
			s += (Integer.MIN_VALUE * i) + "_";
			s += (Short.MAX_VALUE * (short) i) + "_";
			s += (Short.MIN_VALUE * (short) i) + "_";
			s += (Byte.MAX_VALUE * (byte) i) + "_";
			s += (Byte.MIN_VALUE * (byte) i) + "_";
			s += (Character.MAX_VALUE * (char) i) + "_";
			s += (Character.MIN_VALUE * (char) i) + "_";
			s += (Long.MAX_VALUE * i) + "_";
			s += (Long.MIN_VALUE * i) + "_";

		}
		return s;
	}

	@ValueComputationTestCase
	public static String testDiv() {
		String s = "";
		for (int i = -10; i <= 10; i++) {
			if (i == 0)
				continue;
			s += (Integer.MAX_VALUE / i) + "_";
			s += (Integer.MIN_VALUE / i) + "_";
			s += (Short.MAX_VALUE / (short) i) + "_";
			s += (Short.MIN_VALUE / (short) i) + "_";
			s += (Byte.MAX_VALUE / (byte) i) + "_";
			s += (Byte.MIN_VALUE / (byte) i) + "_";
			s += (Character.MAX_VALUE / (char) i) + "_";
			s += (Character.MIN_VALUE / (char) i) + "_";
			s += (Long.MAX_VALUE / i) + "_";
			s += (Long.MIN_VALUE / i) + "_";

		}
		return s;
	}
	
}

	
