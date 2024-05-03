package de.fraunhofer.sit.sse.valbench.modellingtests.android;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import android.util.Pair;
import de.fraunhofer.sit.sse.valbench.metadata.ValueComputationTestCase;

public class AndroidReflection {

	@ValueComputationTestCase(expectedValues = { "Yes" }, jvmFails = true)
	public static String testReflectionModelFieldAccessGet()
			throws IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, InstantiationException, NoSuchFieldException {
		Pair<String, String> s = new Pair<String, String>("Yes", "");
		return Pair.class.getDeclaredField("first").get(s).toString();
	}

}
