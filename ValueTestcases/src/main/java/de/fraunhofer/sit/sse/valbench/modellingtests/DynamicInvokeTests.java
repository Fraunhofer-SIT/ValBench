package de.fraunhofer.sit.sse.valbench.modellingtests;

import java.util.Arrays;

import de.fraunhofer.sit.sse.valbench.ExplicitLoggingPoint;
import de.fraunhofer.sit.sse.valbench.metadata.Requires;
import de.fraunhofer.sit.sse.valbench.metadata.ValueComputationTestCase;

//Due to dynamic invoke, we must not allow any modifications by soot
@Requires(doNotModify=true, doesNotWorkOnAndroid=true)
public class DynamicInvokeTests {
	@ValueComputationTestCase
	public static String doSth() {
		String res = Arrays.toString(Arrays.asList("a", "b", "te").stream().filter(x -> x.length() == 1).toArray());
		ExplicitLoggingPoint.explicitLoggingPoint(res);
		return res;
	}
}
