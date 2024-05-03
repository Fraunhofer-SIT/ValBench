package de.fraunhofer.sit.sse.valbench.modellingtests.android;

import android.net.wifi.WifiConfiguration;
import android.util.Pair;
import android.util.SparseArray;
import de.fraunhofer.sit.sse.valbench.metadata.ValueComputationTestCase;

public abstract class AndroidTests {

	@ValueComputationTestCase(expectedValues = { "null01234_null02468" }, jvmFails = true)
	public static String simpleLoopTestField() {
		WifiConfiguration wifi = new WifiConfiguration();
		for (int i = 0; i < 5; i++) {
			wifi.SSID += i;
			wifi.preSharedKey += (i * 2);
		}
		return wifi.SSID + "_" + wifi.preSharedKey;
	}

	@ValueComputationTestCase(jvmFails = true, expectedValues = { "TestFoonullx" })
	public static String simpleAndroidSparseArrayTest() {
		SparseArray<String> list = new SparseArray<String>();
		list.append(1, "Test");
		list.append(2, "Foo");
		String s = list.get(1) + list.get(2) + list.get(44) + list.get(44, "x");

		return s;
	}

	@SuppressWarnings("unchecked")
	@ValueComputationTestCase(jvmFails = true, expectedValues = { "x" })
	public static String testPair()
			throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		Pair p = new Pair("x", "b");
		return p.first.toString();
	}

	@SuppressWarnings("unchecked")
	@ValueComputationTestCase(jvmFails = true, expectedValues = { "y" })
	public static String testPairOverwrite()
			throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		Pair p = new Pair("x", "b");
		Pair p2 = new Pair("wrong", "wrong2");
		return p.first.toString();
	}

	@SuppressWarnings("unchecked")
	@ValueComputationTestCase(jvmFails = true, expectedValues = { "test" })
	public static String testPairWithReflection()
			throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		Pair p = new Pair("x", "b");
		p.getClass().getDeclaredField("first").set(p, "test");
		return p.first.toString();
	}

}