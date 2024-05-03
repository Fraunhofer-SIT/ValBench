package de.fraunhofer.sit.sse.valbench.modellingtests;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import de.fraunhofer.sit.sse.valbench.metadata.ValueComputationTestCase;

public class ComplexArrayTests {

	@ValueComputationTestCase
	public static String test() throws UnsupportedEncodingException {
		byte[] b = new byte[] { 1, 2 };
		byte[][] x = new byte[][] { { 2, 4 }, { 5, 6 } };
		return Arrays.toString(gen(b, x));
	}

	private static byte[] gen(byte[] b, byte[][] x) {
		for (int i = 0; i < 2; i++) {
			b = doStuff(b, x[i]);
		}
		return b;
	}

	private static byte[] doStuff(byte[] b, byte[] cs) {
		byte[] dst = new byte[2];
		dst[0] = cs[0];
		return dst;
	}

}