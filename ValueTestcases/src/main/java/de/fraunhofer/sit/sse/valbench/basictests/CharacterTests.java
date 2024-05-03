package de.fraunhofer.sit.sse.valbench.basictests;

import de.fraunhofer.sit.sse.valbench.metadata.ValueComputationTestCase;

public class CharacterTests {

	@ValueComputationTestCase
	public String testSimpleChars() {
		char a = 'a';

		return String.valueOf(a + 1);

	}

	@ValueComputationTestCase
	public String testSimpleStringCharArray() {
		char[] s = "Abc".toCharArray();

		return String.valueOf(s[1]);
	}
}
