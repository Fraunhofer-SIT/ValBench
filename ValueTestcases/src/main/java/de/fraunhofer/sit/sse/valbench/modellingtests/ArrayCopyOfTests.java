package de.fraunhofer.sit.sse.valbench.modellingtests;

import java.io.IOException;
import java.util.Arrays;

import de.fraunhofer.sit.sse.valbench.metadata.ValueComputationTestCase;

public class ArrayCopyOfTests {
	@ValueComputationTestCase
	public static String testCopyOf1() throws Exception {
		return Arrays.toString(Arrays.copyOf(new char[] { 'a' }, 4));
	}

	@ValueComputationTestCase
	public static String testCopyOf2() throws Exception {
		return String.valueOf(Arrays.copyOf(new int[] { 4 }, 4)[1]);
	}

	@ValueComputationTestCase
	public static String testCopyOf3() throws Exception {
		return String.valueOf(Arrays.copyOf(new long[] { 4 }, 4)[1]);
	}

	@ValueComputationTestCase
	public static String testCopyOf4() throws Exception {
		return String.valueOf(Arrays.copyOf(new boolean[] { true }, 4)[1]);
	}

	@ValueComputationTestCase
	public static String testCopyOfSpecial1() throws Exception {
		Integer[] x = Arrays.copyOf(new Integer[] { 4 }, 4, Integer[].class);
		return String.valueOf(x[1]);
	}

	@ValueComputationTestCase
	public static String testCopyOfSpecial2() throws Exception {
		String[] x = Arrays.copyOf(new CharSequence[] { "Foo" }, 4, String[].class);
		return String.valueOf(x[1]);
	}

	@ValueComputationTestCase
	public static String testArrayCopy() throws IOException {
		String s = "Fjafiaoiowqei913i31udaejöl";
		byte[] b = s.getBytes("UTF-8");
		byte[] res = new byte[40];
		System.arraycopy(b, 3, res, 1, 2);
		byte[] ss = Arrays.copyOf(b, 4);
		System.arraycopy(ss, 0, res, 15, ss.length);
		Arrays.fill(res, 30, 39, (byte) 112);
		String o = new String(res);
		return o;
	}
}
