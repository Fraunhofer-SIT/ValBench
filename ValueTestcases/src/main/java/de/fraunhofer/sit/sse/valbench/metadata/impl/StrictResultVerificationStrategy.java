package de.fraunhofer.sit.sse.valbench.metadata.impl;

import de.fraunhofer.sit.sse.valbench.metadata.interfaces.IResultComparisonStrategy;

public class StrictResultVerificationStrategy implements IResultComparisonStrategy {

	public static final StrictResultVerificationStrategy INSTANCE = new StrictResultVerificationStrategy();

	@Override
	public boolean verifySingleResult(Object expected, Object actual) {
		if (expected == null)
			return actual == null;
		return expected.equals(actual);
	}

}
