package de.fraunhofer.sit.sse.valbench.modellingtests;

import java.util.ArrayList;
import java.util.List;

import de.fraunhofer.sit.sse.valbench.metadata.ValueComputationTestCase;

public class InheritanceLibraryTests {

	@ValueComputationTestCase
	public static String testInheritance() {
		List<String> ownList = createList();
		ownList.add(0, "x");
		String r = ownList.remove(0);
		return ownList.toString() + r;
	}

	private static List<String> createList() {
		return new ArrayList() {
			@Override
			public void add(int index, Object element) {
				add("Bar");
				super.add(index, element);
				add("Foo");
			}

			@Override
			public Object remove(int index) {
				add("xyz");
				return "Test";
			}
		};
	}

}
