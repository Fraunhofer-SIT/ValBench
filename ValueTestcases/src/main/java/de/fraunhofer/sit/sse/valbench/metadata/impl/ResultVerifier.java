package de.fraunhofer.sit.sse.valbench.metadata.impl;

import de.fraunhofer.sit.sse.valbench.metadata.interfaces.IResult;
import de.fraunhofer.sit.sse.valbench.metadata.interfaces.IResultComparisonStrategy;
import de.fraunhofer.sit.sse.valbench.metadata.interfaces.IResultVerifier;

public class ResultVerifier implements IResultVerifier {

	private TestCase testCase;
	private IResultComparisonStrategy verificationStrategy;

	public ResultVerifier(TestCase testCase, IResultComparisonStrategy verificationStrategy) {
		this.testCase = testCase;
		this.verificationStrategy = verificationStrategy;
	}

	@Override
	public IResult verifyResult(Object res) {
		return verificationStrategy.verifyResult(testCase, testCase.getExpectedResults(), res);
	}

}
