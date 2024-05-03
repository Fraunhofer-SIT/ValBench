package de.fraunhofer.sit.sse.valbench.modellingtests;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

import de.fraunhofer.sit.sse.valbench.metadata.ValueComputationTestCase;

public class HashCodeEqualsTests {

	static class Null {
		@Override
		public int hashCode() {
			return -1;
		}

		@Override
		public boolean equals(Object obj) {
			return false;
		}
	}

	static class Foo extends Null implements Comparable<Foo> {

		private int x;

		public Foo(int x) {
			this.x = x;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = super.hashCode();
			result = prime * result + x;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (!super.equals(obj))
				return false;
			if (getClass() != obj.getClass())
				return false;
			Foo other = (Foo) obj;
			if (x != other.x)
				return false;
			return true;
		}

		@Override
		public int compareTo(Foo o) {
			if (x < o.x)
				return -1;
			else if (x == o.x)
				return 0;
			else
				return 1;
		}

	}

	@ValueComputationTestCase
	public static String testHashCode() {
		Null n = create(1);
		return "" + n.hashCode();
	}

	@ValueComputationTestCase
	public static String testEquals() {
		Null n = create(1);
		Null n2 = create(2);
		Null n3 = create(2);
		return "" + n.equals(n2) + n.equals(n3) + n2.equals(n3) + n2.equals(n2);
	}

	@ValueComputationTestCase
	public static String testSet() {
		Null n = create(1);
		Null n2 = create(2);
		Null n3 = create(2);
		Set<Null> r = new LinkedHashSet<>();
		r.add(n);
		r.add(n2);
		r.add(n3);
		return r.toString();
	}

	@ValueComputationTestCase
	public static String testOrderedSet() {
		Null n1 = create(3);
		Null n2 = create(1);
		Null n3 = create(2);
		Null n4 = create(2);
		Set<Null> r = new TreeSet<>(new Comparator<Null>() {

			@Override
			public int compare(Null o1, Null o2) {
				if (!(o1 instanceof Foo))
					throw new RuntimeException("Wrong instance");
				return Integer.compare(o1.hashCode(), o2.hashCode());
			}
		});
		r.add(n1);
		r.add(n2);
		r.add(n3);
		r.add(n4);
		return r.toString();
	}

	@ValueComputationTestCase
	public static String testOrderedSet2() {
		Null n1 = create(3);
		Null n2 = create(1);
		Null n3 = create(2);
		Null n4 = create(2);
		Set<Null> r = new TreeSet<>();
		r.add(n1);
		r.add(n2);
		r.add(n3);
		r.add(n4);
		return r.toString();
	}

	@ValueComputationTestCase
	public static String testOrderedSet3() {
		Set<Integer> r = new TreeSet<>();
		r.add(1);
		r.add(100);
		r.add(120);
		r.add(20);
		r.add(200);
		return r.toString();
	}

	private static Null create(int x) {
		return new Foo(x);
	}

}
