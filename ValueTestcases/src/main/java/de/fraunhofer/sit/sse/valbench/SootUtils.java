package de.fraunhofer.sit.sse.valbench;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class SootUtils {
	public static String getSootSignature(Method m) {
		String params = "";
		for (Parameter i : m.getParameters()) {
			if (!params.isEmpty())
				params += ",";
			params += getSootType(i.getType());
		}
		return "<" + m.getDeclaringClass().getName() + ": " + getSootType(m.getReturnType()) + " " + m.getName() + "(" + params + ")>";
		
	}

	private static String getSootType(Class<?> t) {
		if (t.isPrimitive())
			return t.toString();
		return t.getName();
	}
}
