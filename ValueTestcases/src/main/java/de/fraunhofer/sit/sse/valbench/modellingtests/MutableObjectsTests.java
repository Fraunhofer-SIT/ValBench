package de.fraunhofer.sit.sse.valbench.modellingtests;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import de.fraunhofer.sit.sse.valbench.metadata.ValueComputationTestCase;

public class MutableObjectsTests {

	@ValueComputationTestCase
	public static String test() throws IOException {
		AtomicReference<String> a = new AtomicReference<>("Foo");
		String s = a.get() + a.compareAndSet("Foo", "x") + a.get() + a.getAndSet("r") + a.get();
		return s;
	}
}
