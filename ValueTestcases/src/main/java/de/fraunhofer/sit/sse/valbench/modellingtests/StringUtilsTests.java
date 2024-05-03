package de.fraunhofer.sit.sse.valbench.modellingtests;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.math3.util.MathUtils;

import de.fraunhofer.sit.sse.valbench.metadata.ValueComputationTestCase;

public class StringUtilsTests {
	@ValueComputationTestCase
	public static String testReplace() {
		return StringUtils.replace("Foobar", "b", "x");

	}

	@ValueComputationTestCase
	public static String testFooB() {
		return StringUtils.repeat('x', 4);
	}

	@ValueComputationTestCase
	public static String testContains() {
		return String.valueOf(StringUtils.containsOnly("afas", "oa"));
	}

	@ValueComputationTestCase
	public static String testFooD() {
		return StringUtils.swapCase("Hi");
	}

	@ValueComputationTestCase
	public static String testFooE() {
		return StringUtils.substringBetween("wx[b]yz", "[", "]");
	}

	@ValueComputationTestCase
	public static String testChop() {
		return StringUtils.chop("abc\r\n");
	}

	@ValueComputationTestCase
	public static String testChomp() {
		return StringUtils.chomp("abc\r\n\r\n");
	}

	@ValueComputationTestCase
	public static String testmath() {
		return String.valueOf(MathUtils.hash(200));
	}

	@ValueComputationTestCase
	public static String testmath2() {
		return String.valueOf(MathUtils.hash(new double[] { 1, 2 }));
	}

	@ValueComputationTestCase
	public static String testmath3() {
		return StringUtils.join(new char[] { 1, 2 }, 'a');
	}

	@ValueComputationTestCase
	public static String testmath4() {
		List<String> l = new ArrayList();
		l.add("x");
		return StringUtils.join(l, 'x');
	}

}
