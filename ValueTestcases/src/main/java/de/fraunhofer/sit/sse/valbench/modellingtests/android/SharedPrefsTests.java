package de.fraunhofer.sit.sse.valbench.modellingtests.android;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import de.fraunhofer.sit.sse.valbench.metadata.ValueComputationTestCase;

public class SharedPrefsTests {
	@ValueComputationTestCase(jvmFails = true, expectedValues = { "Foo2.022falsefalse" })
	public static String testDefaultValue() throws IOException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException, InstantiationException {

		Activity a = new Activity();
		SharedPreferences s = a.getPreferences(0);
		return s.getString("Test", "Foo") + s.getFloat("Test", 2) + s.getInt("T", 2) + s.getLong("t", 2)
				+ s.getBoolean("f", false) + s.contains("fo");
	}

	@ValueComputationTestCase(jvmFails = true, expectedValues = { "xtruefalse" })
	public static String testPut() throws IOException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException, InstantiationException {

		Activity a = new Activity();
		SharedPreferences s = a.getPreferences(0);
		Editor p = s.edit();
		p.putString("yy", "y");
		p.clear();
		p.putString("test", "x");
		p.commit();
		return s.getString("test", "Foo") + s.contains("test") + s.contains("yy");
	}
}
