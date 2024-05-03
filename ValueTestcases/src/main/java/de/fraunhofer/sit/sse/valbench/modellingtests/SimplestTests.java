package de.fraunhofer.sit.sse.valbench.modellingtests;

import java.util.TreeSet;

import de.fraunhofer.sit.sse.valbench.metadata.ValueComputationTestCase;

public class SimplestTests {
	@ValueComputationTestCase
	public static String simplestTreeSet() {
		TreeSet<String> list = new TreeSet<String>();
		list.add("a");
		return list.toString();
	}
}
