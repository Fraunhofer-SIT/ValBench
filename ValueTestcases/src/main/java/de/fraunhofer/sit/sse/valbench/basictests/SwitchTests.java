package de.fraunhofer.sit.sse.valbench.basictests;

import java.util.Random;

import de.fraunhofer.sit.sse.valbench.metadata.ValueComputationTestCase;

public class SwitchTests {

	@ValueComputationTestCase
	public static String testLoopFallthrough() throws Exception {
		String s = "";
		for (int i = 0; i < 100; i++) {
			switch (i % 2) {
			case 1:
				s += "a";
			default:
			}
		}
		return s;
	}

	@ValueComputationTestCase
	public static String testLoopFallthrough2() throws Exception {
		String s = "";
		for (int i = 0; i < 100; i++) {
			switch (i % 2) {
			case 1:
				s += "a";
			default:
				s += "b";
			}
		}
		return s;
	}

	@ValueComputationTestCase
	public static String testLoopFallthrough3() throws Exception {
		String s = "";
		for (int i = 0; i < 100; i++) {
			switch (i % 3) {
			case 0:
				s += "x";
			case 1:
				s += "y";
			case 2:
				s += "z";
				switch (i % 2) {
				case 1:
					s += "a";
				default:
					s += "b";
				}
			}
		}
		return s;
	}

	@ValueComputationTestCase
	public static String testFallthrough() throws Exception {
		StringBuilder sb = new StringBuilder("");
		int i = 2;
		switch (i % 2) {
		case 1:
			sb.append("a");
		default:
		}
		return sb.toString();
	}

	@ValueComputationTestCase
	public static String testFallthrough2() throws Exception {
		StringBuilder sb = new StringBuilder("");
		int i = 0;
		switch (i % 2) {
		case 1:
			sb.append("a");
		default:
			sb.append("b");
		}
		return sb.toString();
	}

	@ValueComputationTestCase
	public static String testFallthrough3() throws Exception {
		String s = "";
		int i = 0;
		switch (i % 3) {
		case 0:
			s += "x";
		case 1:
			s += "y";
		case 2:
			s += "z";
			switch (i % 2) {
			case 1:
				s += "a";
			default:
				s += "b";
			}
		}
		return s;
	}

	@ValueComputationTestCase
	public static String testFallthrough4() throws Exception {
		StringBuilder s = new StringBuilder();
		int i = 0;
		switch (i % 4) {
		case 0:
		case 1:
			s.append("x");
			break;
		case 2:
		default:
			s.append("z");
			switch (i % 2) {
			case 1:
				s.append("a");
			default:
				s.append("b");
			}
		}
		return s.toString();
	}

	@ValueComputationTestCase(expectedValues = { "xyzab", "xyzb", "yzab", "yzb", "zab", "zb", "" })
	public static String testSwitch() throws Exception {
		String s = "";
		int i = new Random().nextInt();
		switch (i % 3) {
		case 0:
			s += "x";
		case 1:
			s += "y";
		case 2:
			s += "z";
			switch (i % 2) {
			case 1:
				s += "a";
			default:
				s += "b";
			}
		}
		return s;
	}

	@ValueComputationTestCase(expectedValues = { "xyzab", "xyzb", "yzab", "yzb", "zab", "zb", "" })
	public static String testSwitch2() throws Exception {
		String s = "";
		int i = new Random().nextInt();
		switch (i % 3) {
		case 0:
			s += "x";
		case 1:
			s += "y";
		case 2:
			s += "z";
			switch (i % 2) {
			case 1:
				s += "a";
			default:
				s += "b";
			}
		}
		return s;
	}

	@ValueComputationTestCase
	public static String testStringSwitch() throws Exception {
		String sCond = "y";
		String s = "No";
		switch (sCond) {
		case "y":
			s += "_Yes";
			break;
		case "n":
			break;
		}
		return s;
	}

}