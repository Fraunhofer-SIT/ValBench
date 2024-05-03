package de.fraunhofer.sit.sse.valbench.modellingtests;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;

import de.fraunhofer.sit.sse.valbench.metadata.ValueComputationTestCase;

public class CollectionTests {
	static class MyArrayList<T> extends ArrayList<T> {
		@Override
		public boolean add(T e) {
			super.add((T) "Foo");
			return super.add(e);
		}
	}

	@ValueComputationTestCase
	public static String ownImpl() {
		MyArrayList<String> list = new MyArrayList<String>();
		list.add("a");
		return list.toString();
	}

	@ValueComputationTestCase
	public static String simplestStackTest() {
		Stack<String> list = new Stack<String>();
		list.push("a");
		list.push("Foo");
		list.pop();
		list.push("b");
		return list.toString();
	}

	@ValueComputationTestCase
	public static String simplestVectorTest() {
		Vector<String> list = new Vector<String>();
		list.add("a");
		list.add("Foo");
		list.add("b");
		return list.toString();
	}

	@ValueComputationTestCase
	public static String simplestLinkedHashSet() {
		LinkedHashSet<String> list = new LinkedHashSet<String>();
		list.add("a");
		list.add("Foo");
		list.add("b");
		return list.toString();
	}

	@ValueComputationTestCase
	public static String simplestTreeSet() {
		TreeSet<String> list = new TreeSet<String>();
		list.add("a");
		list.add("Foo");
		list.add("b");
		return list.toString();
	}

	@ValueComputationTestCase
	public static String simplestTreeSet2() {
		TreeSet<Number> list = new TreeSet<Number>();
		list.add(3.2D);
		list.add(3.14159);
		list.add(50000000D);
		list.add(-22D);
		return list.toString();
	}

	@ValueComputationTestCase
	public static int simplestListTest() {
		List<String> list = new ArrayList<String>();
		list.add("a");
		return list.size();
	}

	@ValueComputationTestCase
	public static String simpleBreakListTest() {
		List<StringBuilder> list = new ArrayList<StringBuilder>();
		list.add(new StringBuilder("1-"));
		list.get(0).append("Foo");
		return list.get(0).toString();
	}

	@ValueComputationTestCase
	public static String simpleBreakListTest2() {
		List<StringBuilder> list = new ArrayList<StringBuilder>();
		list.add(new StringBuilder("1-"));
		list.add(new StringBuilder("2-"));
		String y = "foo" + "".equals("y");
		for (int i = 0; i < 2; i++) {
			list.get(0).append(y).append(i);
			list.get(1).append("bar").append(i);
		}
		return list.get(0).toString() + list.get(1).toString();
	}

	@ValueComputationTestCase
	public static String simpleBreakListTest3() {
		List<StringBuilder> list = new ArrayList<StringBuilder>();
		list.add(new StringBuilder("1-"));
		list.add(new StringBuilder("2-"));
		for (int i = 0; i < 2; i++) {
			list.get(0).append("x").append(i);
			list.get(1).append("bar").append(i);
		}
		return list.get(0).toString() + list.get(1).toString();
	}

	@ValueComputationTestCase
	public static String simpleListTest2() {
		List<StringBuilder> list = new LinkedList<StringBuilder>();
		list.add(new StringBuilder("1-"));
		list.add(new StringBuilder("2-"));
		for (int i = 0; i < 2; i++) {
			list.get(0).append("foo").append(i);
			list.get(1).append("bar").append(i);
		}
		return list.get(0).toString() + list.get(1).toString();
	}

	@ValueComputationTestCase
	public static String simpleListTest3() {
		List<StringBuilder> list = new CopyOnWriteArrayList<>(new StringBuilder[] { new StringBuilder("Foo") });
		list.add(new StringBuilder("2-"));
		for (int i = 0; i < 2; i++) {
			list.get(0).append("foo").append(i);
			list.get(1).append("bar").append(i);
		}
		return list.get(0).toString() + list.get(1).toString();
	}

	@ValueComputationTestCase
	public static String simpleIterableTest() {
		List<String> list = new ArrayList<String>();
		list.add("x");

		return getIterable(list);
	}

	private static String getIterable(Iterable<String> list) {
		String s = "";
		for (String r : list) {
			s += r;
		}
		return s;
	}

	@ValueComputationTestCase
	public static String iteratorTest() {
		List<String> ss = new ArrayList<>();
		ss.add("a1");
		ss.add("a2");
		String s = "";
		for (int i = 0; i < 3; i++) {
			for (String x : ss)
				s += i + x;
		}
		return s;
	}

	@ValueComputationTestCase
	public static String listTest2() {
		Stack<String> st = new Stack<>();
		st.push("FOo");
		st.add("Test");
		String s = st.peek();
		st.pop();
		Vector<String> v = new Vector<>(st);
		v.add("Bla");

		return v.toString() + s;
	}

	@ValueComputationTestCase
	public static String testList() throws IOException {
		ArrayList<String> s = new ArrayList<>();
		s.add("Foo");
		java.util.LinkedList<String> r = new java.util.LinkedList<>();
		r.add("Bar");
		r.add("x");
		r.add("y");
		r.remove(1);
		s.addAll(0, r);
		return Arrays.toString(s.toArray());
	}

	@ValueComputationTestCase
	public static String listTest() {
		ArrayList<String> rr = new ArrayList<>(Collections.unmodifiableList(Arrays.asList("a", "b")));
		java.util.LinkedList<String> m1 = new java.util.LinkedList<String>(rr);
		return m1.getLast() + rr.get(1);
	}

	@ValueComputationTestCase
	public static String setTest() {
		Set<String> st = new HashSet<>(Arrays.asList(Arrays.copyOf(new String[] { "a", "b", "c" }, 2)));
		Set<String> st2 = new TreeSet<>();
		st2.add("Foo");
		LinkedHashSet<String> ss = new LinkedHashSet<>(st2);
		return st.toString() + st2 + "-" + ss;
	}

	@ValueComputationTestCase
	public static String arraysTest() {
		Set<String> st = new HashSet<>(Arrays.asList(Arrays.copyOf(new String[] { "a", "b", "c" }, 2)));
		Set<String> st2 = new TreeSet<>();
		st2.add("Foo");
		LinkedHashSet<String> ss = new LinkedHashSet<>(st2);
		return st.toString() + st2 + "-" + ss;
	}

	@ValueComputationTestCase
	public static String testSet() throws IOException {
		LinkedHashSet<String> s = new LinkedHashSet<>();
		s.add("Foo");
		s.add("x");
		s.add("Bar");
		java.util.HashSet<String> r = new java.util.HashSet<>();
		r.add("y");
		r.add("fo");
		r.remove("fo");
		s.addAll(r);
		return Arrays.toString(s.toArray());
	}

}
