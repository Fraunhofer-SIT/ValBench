package de.fraunhofer.sit.sse.valbench.modellingtests;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import de.fraunhofer.sit.sse.valbench.metadata.ValueComputationTestCase;

public class ReflectionArrayTests {

	@ValueComputationTestCase
	public static String testReflectionArray() throws IOException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException, InstantiationException {
		Object arr1 = Array.newInstance(int.class, 10);
		Object arr2 = Array.newInstance(int.class, 10, 15);
		Object obj1 = Array.newInstance(Object.class, 10);
		Object arr3 = Array.newInstance(Integer.class, 10);
		Array.set(arr1, 0, 2);
		Array.setInt(arr1, 1, 3);
		Array.setShort(arr1, 3, (short) 5);
		Array.set(obj1, 0, "Foo");
		Array.set(arr3, 1, 4);
		Integer[] i = (Integer[]) arr3;
		int[][] r = (int[][]) arr2;
		return Arrays.toString((int[]) arr1) + Arrays.deepToString((Object[]) obj1) + Arrays.toString(i)
				+ Arrays.deepToString(r) + Array.getLength(arr1) + "_" + Array.getLength(arr2) + Array.getInt(arr1, 3)
				+ Array.get(arr3, 1) + arr1.getClass().getComponentType() + arr3.getClass().getComponentType()
				+ obj1.getClass().getComponentType();
	}


}
