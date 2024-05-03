package de.fraunhofer.sit.sse.valbench.basictests;

import java.util.Arrays;

import de.fraunhofer.sit.sse.valbench.metadata.ValueComputationTestCase;

public class ArrayTests {

	@ValueComputationTestCase
	public static String test1() {
		return Arrays.toString(getArray());
	}

	@ValueComputationTestCase
	public static String testArraySensitivity() {
		String[] x = new String[3];
		x[1] = "Right";
		x[0] = "Wrong";
		x[2] = "Wrong";
		return x[1];
	}

	@ValueComputationTestCase
	public static String testArraySensitivityWithNonConstantIndices() {
		String[] x = new String[3];
		int i = getIndex();
		x[1] = "Right";
		x[0] = "Wrong";
		x[2] = "Wrong";
		return x[i];
	}
	
	private static int getIndex() {
		return 1;
	}

	@ValueComputationTestCase
	public static String testGetConstantElementSimple() {
		String[] x = new String[] { "1", "2", "3" };
		return x[1];
	}

	@ValueComputationTestCase
	public static String testGetConstantElementOverwrite() {
		String[] x = new String[] { "1", "2", "3" };
		x[1] = "x";
		return x[1];
	}

	@ValueComputationTestCase
	public static String testGetConstantElementNoOverwrite() {
		String[] x = new String[] { "1", "2", "3" };
		x[2] = "x";
		return x[1];
	}

	@ValueComputationTestCase
	public static String testGetConstantElementLoop() {
		String[] x = new String[] { "1", "2", "3" };
		String res = "";
		for (String i : x)
			res += i;
		return res;
	}

	@ValueComputationTestCase
	public static String testArrayCopy() {
		byte[] src = new byte[] { 1, 2, 3, 4, 5 };
		byte[] dest = new byte[20];
		System.arraycopy(src, 1, dest, 6, 4);
		return Arrays.toString(dest);
	}

	private static long[] getArray() {
		return new long[] { 1, 2 };
	}

}