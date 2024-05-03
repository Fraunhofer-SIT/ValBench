package de.fraunhofer.sit.sse.valbench.basictests;

import de.fraunhofer.sit.sse.valbench.metadata.ValueComputationTestCase;

public class TypeTests {

	@ValueComputationTestCase
	public static String doSth() {
		int i;
		double d;
		float f;
		short s;
		boolean b;
		byte bb;
		long l;
		char c;
		Object o;
		int[] a;

		i = 2343249;
		d = 3.14324;
		f = 3.143f;
		s = 4636;
		b = true;
		bb = (byte) i;
		l = 314435665;
		c = 123;
		a = new int[3];

		a[1] = 24355764;

		return (i + d + f + s + "" + b + bb + l + c + " ");

	}

}
