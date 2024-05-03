package de.fraunhofer.sit.sse.valbench.basictests;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Random;

import de.fraunhofer.sit.sse.valbench.metadata.ValueComputationTestCase;

public class FieldTests {

	static int[] x = new int[3];
	static byte[] b = new byte[3];
	byte[] baseb = new byte[3];
	String sImmutable;

	static {
		reset();
	}

	//The reset method is called by the test cases in order to simulate clinit when
	//running the code in the JVM to create the ground truth
	static void reset() {
		x[0] = 2;
		x[1] = 3;
	}
	
	
	static class Fields {
		String a, b;
	}
	
	@ValueComputationTestCase
	public static String fieldSensitivityTest() {
		Fields f = new Fields();
		f.a = "A";
		f.b = "B";
		return f.a;
	}
	
	@ValueComputationTestCase
	public static String objectSensitivityTest() {
		Fields f = new Fields();
		Fields f2 = new Fields();
		f.a = "A";
		f2.a = "Wrong";
		return f.a;
	}

	@ValueComputationTestCase(expectedValues = { "[6, 4, 1]", "[3, 4, 5]" })
	public static String testSimple() throws UnsupportedEncodingException {
		if (new Random().nextBoolean())
			x[2] = 4;
		else
			x[0] = 5;
		for (int i = 0; i < x.length; i++)
			x[i]++;
		return Arrays.toString(x);
	}

	@ValueComputationTestCase
	public static String testLoopCopy() throws UnsupportedEncodingException {
		int[] y = new int[3];
		for (int i = 0; i < 2; i++)
			y[i] = x[i] >> 2;
		return Arrays.toString(y);
	}

	@ValueComputationTestCase
	public static String testMultiLoop() throws UnsupportedEncodingException {
		for (int i = 0; i < x.length; i++)
			x[i] *= 2;
		x[2] += 55;
		for (int i = 0; i < x.length; i++)
			x[i] >>= 4;
		x[1] -= 35;
		return Arrays.toString(x);
	}

	@ValueComputationTestCase
	public static String testOverwrite() throws UnsupportedEncodingException {
		b = new byte[] { 4, 4 };
		Random rand = new Random(1);
		rand.nextBytes(b);
		return Arrays.toString(b);
	}

	@ValueComputationTestCase
	public static String testFieldAlias() throws UnsupportedEncodingException {
		FieldTests f = new FieldTests();
		f.baseb = new byte[] { 4, 4 };
		Random rand = new Random(1);
		rand.nextBytes(f.baseb);
		return Arrays.toString(f.baseb);
	}

	@ValueComputationTestCase
	public static String testFieldAlias2() throws UnsupportedEncodingException {
		FieldTests f = new FieldTests();
		f.baseb = new byte[] { 4, 4 };
		Random rand = new Random(1);
		byte[] x = f.baseb; //Create alias early
		rand.nextBytes(f.baseb);
		return Arrays.toString(x);
	}

	@ValueComputationTestCase
	public static String testFieldInLoop() throws UnsupportedEncodingException {
		return new FieldTests().testFieldLoop();
	}

	@ValueComputationTestCase
	public static String testFieldInLoop2() throws UnsupportedEncodingException {
		return String.valueOf(new FieldTests().testFieldLoop2());
	}

	private String testFieldLoop() {
		//We use a field in a loop, but it is hidden in a callee.
		baseb[0] = 1;
		String s = "";
		for (int i = 0; i < 2; i++) {
			s += useField();
		}
		return s;
	}

	private int useField() {
		return useField2();
	}

	private byte useField2() {
		return baseb[0]++;
	}

	private byte testFieldLoop2() {
		for (int i = 0; i < 2; i++) {
			setField();
		}
		return baseb[0];
	}

	private void setField() {
		setField2();
	}

	private void setField2() {
		baseb[0] = 120;
	}

	@ValueComputationTestCase
	public static String testArrayInLoop() throws UnsupportedEncodingException {
		return String.valueOf(new FieldTests().testArrayLoop());
	}

	private String testArrayLoop() {
		byte[] b = new byte[2];
		for (int i = 0; i < 3; i++) {
			changeArr(b);
		}
		return Arrays.toString(b);
	}

	private void changeArr(byte[] b) {
		changeArr2(b);
	}

	private void changeArr2(byte[] b2) {
		b2[0] = 4;
	}

	@ValueComputationTestCase
	public static String testImmutableInLoop() throws UnsupportedEncodingException {
		return String.valueOf(new FieldTests().testImmutableLoop());
	}

	private String testImmutableLoop() {
		sImmutable = "No"; //invalid result
		byte[] b = new byte[2];
		for (int i = 0; i < 3; i++) {
			changeImmutable();
		}
		return Arrays.toString(b);
	}

	private void changeImmutable() {
		changeImmutable2();
	}

	private void changeImmutable2() {
		changeImmutable3();
	}

	private void changeImmutable3() {
		changeImmutable4();
	}

	private void changeImmutable4() {
		sImmutable = "Test";
	}

	static class ArrayField {
		String[] s;
	}

	@ValueComputationTestCase
	public static String testArrayField() throws UnsupportedEncodingException {
		ArrayField arrF = new ArrayField();
		arrF.s = new String[] { "a", "b" };
		ArrayField arrF2 = new ArrayField();
		arrF2.s = new String[] { "c", "d" };
		return arrF2.s[1];
	}

	private static int INIT_D = 0x10325476;

	private static int[] SHIFT_AMTS = { 7, 12, 17, 22, 5, 9, 14, 20, 4, 11, 16, 23, 6, 10, 15, 21 };

	@ValueComputationTestCase
	public static String testStaticFields() {
		String s = INIT_D + "_ " + Arrays.toString(SHIFT_AMTS);
		return s;
	}

	static class Field {
		public String value;
		public Field next;
	}

	static class Field2 {
		public String v;
	}

	static class Field3 {
		public String v;
	}

	static class Field4 {
		public String v;
	}

	private static Field field;
	private static Field4 h;

	static {
		field = new Field();
		Field2 f = new Field2();
		f.v = "Foo";
		Field3 g = new Field3();
		g.v = f.v + "1";
		h = new Field4();
		h.v = g.v + "2";
	}

	static {
		field.next = new Field();
		field.next.next = new Field();
		field.next.next.value = "field";
		field.next.value = "field-wrong";

	}

	@ValueComputationTestCase(expectedValues = { "field" })
	public static String complicatedFieldTest() {
		return field.next.next.value;
	}

	@ValueComputationTestCase(expectedValues = { "Foo12" })
	public static String complicatedFieldTest2() {
		return h.v;
	}

	static class OwnLinkedList {
		Object first;
		OwnLinkedList next;
	}

	@ValueComputationTestCase
	public static String findFields() {
		OwnLinkedList f = new OwnLinkedList();
		OwnLinkedList f2 = new OwnLinkedList();
		OwnLinkedList f3 = new OwnLinkedList();
		f3.first = "c";
		f.first = "a";
		f2.first = "d";
		f2.next = f;
		f3.next = f2;
		String x = f3.next.next.first.toString();
		return x;
	}

}
