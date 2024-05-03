package de.fraunhofer.sit.sse.valbench.basictests;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Random;

import de.fraunhofer.sit.sse.valbench.metadata.ValueComputationTestCase;

public class BasicStringTests {

	@ValueComputationTestCase
	public static String testCtor() {
		StringBuilder sb = new StringBuilder("A");
		return sb.toString();
	}

	@ValueComputationTestCase
	public static String test() {
		StringBuilder sb = new StringBuilder("Test");
		sb.append("foo");
		return sb.toString();
	}

	@ValueComputationTestCase
	public static String testInt() {
		StringBuilder sb = new StringBuilder("Test");
		sb.append(2);
		return sb.toString();
	}

	@ValueComputationTestCase
	public static String testDouble() {
		StringBuilder sb = new StringBuilder("Test");
		sb.append(2D);
		return sb.toString();
	}

	@ValueComputationTestCase
	public static String testChar() {
		StringBuilder sb = new StringBuilder("Test");
		sb.append('x');
		return sb.toString();
	}

	@ValueComputationTestCase(expectedValues = { "Testx1", "Testx2" })
	public static String testIf() {
		StringBuilder sb = new StringBuilder("Test");
		sb.append('x');
		if (new Random().nextBoolean())
			sb.append("1");
		else
			sb.append("2");
		return sb.toString();
	}

	@ValueComputationTestCase
	public static String testSimpleString() {
		return "Test".concat("Foo");
	}

	@ValueComputationTestCase
	public static String testSimpleString2() {
		StringBuilder b = new StringBuilder("Fop");
		b.setLength(2);
		b.append("x");
		b.reverse();
		b.deleteCharAt(1);
		b.delete(0, 1);
		return b.toString();
	}

	@ValueComputationTestCase
	public static String testSimpleString3() {
		return String.format("Format test: %s %d %f", "a", 2, 4.1D);
	}

	@ValueComputationTestCase
	public static String testSimpleString4() {
		byte[] b = new byte[100];
		Random rand = new Random(2);
		rand.nextBytes(b);
		Arrays.sort(b);
		return new String(b, StandardCharsets.UTF_8);
	}

}
