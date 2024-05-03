package de.fraunhofer.sit.sse.valbench.modellingtests;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.Arrays;

import de.fraunhofer.sit.sse.valbench.metadata.ValueComputationTestCase;

public class ByteBufferTests {

	@ValueComputationTestCase
	public static String testSimple() throws UnsupportedEncodingException {
		byte[] b = new byte[20];
		ByteBuffer.wrap(b, 0, 8).putLong(2);
		return Arrays.toString(b);
	}

	@ValueComputationTestCase
	public static String testAllocate() throws UnsupportedEncodingException {
		int[] ints = new int[] { 1, 2, 3, 4 };
		ByteBuffer buf = ByteBuffer.allocate(ints.length * Integer.BYTES);
		for (int i = 0; i < ints.length; ++i) {
			buf.putInt(ints[i]);
		}

		return Arrays.toString(buf.array());
	}

	@ValueComputationTestCase
	public static String testTypes() throws UnsupportedEncodingException {
		ByteBuffer buf = ByteBuffer.allocate(40);
		buf.put(1, (byte) 2);
		((java.nio.Buffer)buf).position(6);
		buf.putFloat(4.5F);
		buf.putChar(3, 'O');
		buf.putDouble(20, 3.14159F);

		return Arrays.toString(buf.array()) + "_" + buf.getDouble(20);
	}

	@ValueComputationTestCase
	public static String testSimple2() throws UnsupportedEncodingException {
		byte[] bytes = new byte[] { 1, 2, 3, 4 };
		ByteBuffer buf = ByteBuffer.wrap(bytes);

		int[] result = new int[bytes.length / 4];
		for (int i = 0; i < result.length; ++i) {
			result[i] = buf.getInt();
		}

		return Arrays.toString(result);
	}

	@ValueComputationTestCase
	public static String testSlice() throws UnsupportedEncodingException {
		byte[] bytes = new byte[] { 1, 2, 3, 4, 5, 6, 7, 8 };
		ByteBuffer buf = ByteBuffer.wrap(bytes);
		((java.nio.Buffer)buf).position(2);
		ByteBuffer sliced = buf.slice();
		sliced.putInt(4);
		((java.nio.Buffer)buf).position(0);

		int[] result = new int[bytes.length / 4];
		for (int i = 0; i < result.length; ++i) {
			result[i] = buf.getInt();
		}

		return Arrays.toString(result);
	}

	@ValueComputationTestCase //(algorithmType = AlgorithmType.ONLY_EMULATE)
	public static String testSlice2() throws UnsupportedEncodingException {
		byte[] bytes = new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };
		ByteBuffer buf = ByteBuffer.wrap(bytes);
		((java.nio.Buffer)buf).position(8);
		ByteBuffer sliced = doSlice(buf);
		sliced.putInt(4);
		((java.nio.Buffer)buf).position(0);
		sliced.asCharBuffer().append("ab");

		int[] result = new int[bytes.length / Integer.BYTES];
		for (int i = 0; i < result.length; ++i) {
			result[i] = buf.getInt();
		}

		byte[] arr = buf.array();
		arr[2] = 2;
		return Arrays.toString(arr);
	}

	private static ByteBuffer doSlice(ByteBuffer buf) {
		return buf.slice();
	}

}
