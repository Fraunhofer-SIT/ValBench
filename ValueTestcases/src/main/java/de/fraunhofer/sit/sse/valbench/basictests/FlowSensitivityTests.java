package de.fraunhofer.sit.sse.valbench.basictests;

import de.fraunhofer.sit.sse.valbench.metadata.ValueComputationTestCase;

public class FlowSensitivityTests {


	@ValueComputationTestCase
	public static String testFlowSensitivity() {
		StringBuilder sb = new StringBuilder();
		sb.append("x");
		sb.append("y");
		sb.append("z");
		return sb.toString();
	}

}
