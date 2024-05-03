package de.fraunhofer.sit.sse.valbench;

import de.fraunhofer.sit.sse.valbench.ExplicitLoggingPoint;
import de.fraunhofer.sit.sse.valbench.metadata.ValueComputationTestCase;

public class LoggingPointWithinMethod {

	@ValueComputationTestCase
	public static void testExplicit() {
		int x = 2;
		x++;
		ExplicitLoggingPoint.explicitLoggingPoint("Foo " + x, "bar" + x--, 5);
	}

	@ValueComputationTestCase
	public static void testExplicit2() {
		for (int i = 0; i < 5; i++)
			ExplicitLoggingPoint.explicitLoggingPoint(i);
	}

	@ValueComputationTestCase
	public static void testExplicit3() {
		int p = 0;
		for (int x = 0; x < 3; x++) {
			for (int i = 0; i < 5; i++) {
				ExplicitLoggingPoint.explicitLoggingPoint(i, x);
				p = p * i;
			}
			p += x;
		}
		ExplicitLoggingPoint.explicitLoggingPoint(p);
	}
}
