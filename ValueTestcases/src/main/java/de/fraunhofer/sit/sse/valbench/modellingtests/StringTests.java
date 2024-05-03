package de.fraunhofer.sit.sse.valbench.modellingtests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import de.fraunhofer.sit.sse.valbench.metadata.ValueComputationTestCase;

public class StringTests {
	@ValueComputationTestCase
	public static String test() {
		List<String> s = new ArrayList<>();
		s.addAll(Arrays.asList("cool", "Op"));
		String[] arr = new String[s.size()];
		arr = s.toArray(arr);
		return String.join("Foo", arr) + String.join("b", s);
	}

	@ValueComputationTestCase
	public static String test2() {
		List<String> list = new ArrayList<>();
		list.add("Foo");
		list.add("Bla");
		Collections.reverse(list);
		String s = list.toString();
		Collections.sort(list);
		return s + list.toString();
	}

	@ValueComputationTestCase
	public static String test3() {
		StringBuilder b = new StringBuilder("Test");

		StringBuilder b2 = new StringBuilder(20);
		b2.append(true);
		b.append(b2);
		b.append("abcdefgh", 1, 2);
		b.append("abcdefgh".toCharArray(), 1, 2);
		b.insert(3, "abcdefgh", 1, 2);
		b.insert(5, "abcdefgh".toCharArray(), 1, 2);
		return b.toString();
	}

	@ValueComputationTestCase
	public static String stringTest() {
		return String.copyValueOf(new char[] { 'a', 'b' }) + String.valueOf("Fabcd".toCharArray(), 1, 2);
	}

	@ValueComputationTestCase
	public static String testValueOf1() {
		return String.valueOf(new char[] { 3, 5, 'a' });
	}

	@ValueComputationTestCase
	public static String testValueOf2() {
		return String.valueOf("ad".indexOf('d'));
	}

}
