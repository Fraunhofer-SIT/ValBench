package de.fraunhofer.sit.sse.valbench.modellingtests;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import de.fraunhofer.sit.sse.valbench.metadata.ValueComputationTestCase;

public class MapTests {
	@ValueComputationTestCase
	public static String testMaps1() {
		LinkedHashMap<String, String> map = new LinkedHashMap<>(2);
		Random rand = new Random(1);
		for (int i = 0; i < 60; i++)
			map.put(String.valueOf(rand.nextInt()), String.valueOf(rand.nextInt()));
		return map.toString();
	}

	@ValueComputationTestCase
	public static String testMaps2() {
		// Use access order
		LinkedHashMap<String, String> map = new LinkedHashMap<>(2, 0.75F, true);
		Random rand = new Random(1);
		List<String> access = new ArrayList<>();
		for (int i = 0; i < 60; i++) {
			String key = String.valueOf(rand.nextInt());
			map.put(key, String.valueOf(rand.nextInt()));
			if (rand.nextBoolean())
				access.add(key);
		}
		Collections.shuffle(access, rand);
		for (String i : access)
			map.get(i);
		String s = "";
		Iterator<String> i = access.iterator();
		while (i.hasNext()) {
			s += i.next();
		}
		return s;
	}

	@ValueComputationTestCase
	public static String testMaps3() {
		TreeMap<String, String> map = new TreeMap<>();
		Random rand = new Random(1);
		for (int i = 0; i < 60; i++)
			map.put(String.valueOf(rand.nextInt()), String.valueOf(rand.nextInt()));
		return map.toString();
	}

	@ValueComputationTestCase
	public static String testMaps4() {
		ConcurrentHashMap<Object, Object> map = new ConcurrentHashMap<>();
		Random rand = new Random(1);
		String s = "";
		for (int i = 0; i < 60; i++) {
			String x = String.valueOf(rand.nextInt());
			map.put(x, String.valueOf(rand.nextInt()));
			s += map.getOrDefault(x, "Foo");
			s += map.getOrDefault(x + 1, "Foo");
		}
		return s;
	}

	@ValueComputationTestCase
	public static String mapTest() {
		Map<String, String> m1 = new HashMap<>();
		Map<String, String> m2 = new HashMap<>();
		m1.put("b", "a");
		m1.put("a", "b");
		m1.put("b", "a");
		m2.put("a", "a");
		LinkedHashMap<String, String> hm = new LinkedHashMap<>(m1);
		hm.put("o", "v");
		hm.put("a", "b");
		hm.put("x", "y");
		return m1.get("a") + "_" + m2.toString();
	}

	@ValueComputationTestCase
	public static String testMap() throws IOException {
		HashMap<String, String> m = new HashMap<>();
		m.put("Foo", "Bar");
		m.put("Bar", "Baz");
		m.remove("Bar");
		return m.toString() + m.containsKey("Bar");
	}

}
