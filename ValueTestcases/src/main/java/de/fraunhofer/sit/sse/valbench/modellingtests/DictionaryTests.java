package de.fraunhofer.sit.sse.valbench.modellingtests;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Map.Entry;

import de.fraunhofer.sit.sse.valbench.metadata.ValueComputationTestCase;

import java.util.Properties;

public class DictionaryTests {

	static char ch = 't';

	@ValueComputationTestCase
	public static String test() {
		Dictionary<String, String> c = createDict1();
		return c.get("Tes" + ch);
	}

	@ValueComputationTestCase
	public static String test2() {
		Dictionary<Object, Object> c = createDict2();
		return c.get("Tes" + ch).toString();
	}

	@ValueComputationTestCase
	public static String test3() {
		Properties p = new Properties();
		p.put("Foo", "x");
		Entry<Object, Object> r = p.entrySet().iterator().next();
		return p.getProperty("Foo") + r.getKey() + r.getValue();
	}

	@ValueComputationTestCase
	public static String test4() {
		Hashtable<Integer, Integer> ht = new Hashtable<>();
		ht.put(2, 4);
		return String.valueOf(ht.get(2) + 1);
	}

	private static Dictionary<String, String> createDict1() {
		Hashtable<String, String> r = new Hashtable();
		r.put("Foo", "bar");
		r.put("Test", "x");
		r.put("A", "bar2");
		return r;
	}

	private static Dictionary<Object, Object> createDict2() {
		Dictionary<Object, Object> r = new Properties();
		r.put("Foo", "bar");
		r.put("Test", "x");
		r.put("A", "bar2");
		return r;
	}
}
