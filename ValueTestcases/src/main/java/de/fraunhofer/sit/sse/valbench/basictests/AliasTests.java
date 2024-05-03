package de.fraunhofer.sit.sse.valbench.basictests;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import de.fraunhofer.sit.sse.valbench.metadata.ValueComputationTestCase;

public class AliasTests {

	private byte[] aliased = new byte[] { 1, 2, 3, 4, 5 };
	private byte[] notAliased = new byte[] { 10, 9, 8, 7, 6 };
	private static StringBuilder fsb;
	private static StringBuilder fsb2;

	@ValueComputationTestCase
	public static String testAlias1() throws UnsupportedEncodingException {
		AliasTests a = new AliasTests();
		byte[] b = a.aliased;
		return a.test1(b);
	}

	@ValueComputationTestCase
	public static String testAlias2() throws UnsupportedEncodingException {
		AliasTests a = new AliasTests();
		byte[] b = a.notAliased;
		return a.test1(b);
	}

	@ValueComputationTestCase
	public static String testAlias3() throws UnsupportedEncodingException {
		AliasTests a = new AliasTests();
		byte[] b = a.aliased;
		return new AliasTests().test1(b);
	}

	@ValueComputationTestCase
	public static String testAlias4() throws UnsupportedEncodingException {
		AliasTests a = new AliasTests();
		for (int i = 0; i < 3; i++) {
			byte[] b = a.aliased;
			a.test1(b);
		}
		return a.test1(a.aliased);
	}

	@ValueComputationTestCase
	public static String testFieldAlias() {
		fsb = new StringBuilder();
		fsb2 = new StringBuilder();

		if (retFalse()) {
			fsb2 = fsb;
		}
		fsb.append("right");
		fsb2.append("Wrong");
		return fsb.toString();
	}

	private static boolean retFalse() {
		return false;
	}

	private String test1(byte[] b) {
		String s = "Part 1";
		for (int x : b) {
			s += x;
		}
		mutate(b);
		s += Arrays.toString(b);
		if (b == aliased)
			s += "Aliased";
		return s;
	}

	private void mutate(byte[] b) {
		Arrays.sort(b);
	}

	static class ObjHolder<T> {
		public ObjHolder(T l) {
			t = l;
		}

		@Override
		public String toString() {
			return t.toString();
		}

		T t;
	}

	@ValueComputationTestCase
	public static String stest2() {
		ObjHolder<StringBuilder> sb = create();
		ObjHolder<StringBuilder> sb2 = new ObjHolder(new StringBuilder());

		Object o;

		String a = "wrong";
		if (retFalse()) {
			sb2 = sb;
		}
		sb.t.append("right");
		sb2.t.append("Wrong");
		return sb.t.toString();
	}

	@ValueComputationTestCase
	public static String stest3() {
		ObjHolder<StringBuilder> sb = create();
		ObjHolder<StringBuilder> sb2 = new ObjHolder(new StringBuilder());

		Object o;

		String a = "wrong";
		if (retFalse()) {
			sb2 = sb;
		}
		sb.t.append("right");
		sb2.t.append("Wrong");
		return sb.t.toString();
	}

	private static ObjHolder create() {
		return new ObjHolder(new StringBuilder());
	}

}