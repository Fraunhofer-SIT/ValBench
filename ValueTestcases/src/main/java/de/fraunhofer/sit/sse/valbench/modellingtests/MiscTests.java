package de.fraunhofer.sit.sse.valbench.modellingtests;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.util.Random;

import de.fraunhofer.sit.sse.valbench.metadata.ValueComputationTestCase;

public class MiscTests {
	@ValueComputationTestCase
	public static String testCompares() {
		return "" + Byte.compare((byte) 0, (byte) 1) + new Byte((byte) 2).compareTo(new Byte((byte) 3))
				+ "fda".compareTo("bo") + new BigInteger("1492481348931489314893149831489314893148938943134891489314")
						.compareTo(new BigInteger("22"))
				+ new Integer(2222).compareTo(11);
	}

	@ValueComputationTestCase
	public static String testHex() {
		String s = "";
		for (int i = 0; i < 5; i++)
			s += Integer.toHexString(i) + Integer.toOctalString(i) + Integer.toBinaryString(i);
		return s;
	}

	@ValueComputationTestCase
	public static String testURLs() throws UnsupportedEncodingException {
		return URLEncoder.encode("Te?") + URLEncoder.encode("st", "UTF-8");
	}

	@ValueComputationTestCase
	public static String testVersionCode() throws IOException {
		int majvc = 25;
		int vc = 55;
		long longVersionCode = ((long) majvc << 32) | vc;

		int majorCode = (int) (longVersionCode >> 32);
		int versionCode = (int) (longVersionCode & 0x00000000ffffffff);
		String s = majvc + "_" + majorCode + "_" + vc + "_" + versionCode;
		return s;
	}

}
