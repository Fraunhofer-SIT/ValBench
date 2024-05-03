package de.fraunhofer.sit.sse.valbench.modellingtests;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Locale;
import java.util.Random;

import de.fraunhofer.sit.sse.valbench.metadata.ValueComputationTestCase;

public class AdvancedStringTests {
	@ValueComputationTestCase
	public static String testSimpleString5() {
		byte[] b = new byte[100];
		Random rand = new Random(2);
		rand.nextBytes(b);
		Arrays.sort(b);
		return new String(b, Charset.forName("UTF-8"));
	}

	@ValueComputationTestCase
	public static String testSimpleString6() {
		byte[] b = new byte[100];
		Random rand = new Random(3);
		rand.nextBytes(b);
		String s = Charset.forName("UTF-8").displayName(Locale.US);
		String s2 = Charset.forName("UTF-8").name();
		return new String(b, Charset.forName("UTF-8")) + s + s2;
	}

	@ValueComputationTestCase
	public static String testStringFormat() {
		BigInteger b = BigInteger.TEN.multiply(new BigInteger("123"));
		String s = String.join(",", new String[] { "1", "2", "3" });
		return String.format("%d: %s", b, s);
	}

}
