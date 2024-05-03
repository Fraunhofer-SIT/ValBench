package de.fraunhofer.sit.sse.valbench.metadata.impl.results;

import de.fraunhofer.sit.sse.valbench.metadata.impl.TestCase;
import de.fraunhofer.sit.sse.valbench.metadata.interfaces.IResult;
import de.fraunhofer.sit.sse.valbench.metadata.interfaces.ITestCase;

public class Result implements IResult {
	private Object expected;
	private Object actual;
	private int tp;
	private int fp;
	private int fn;
	private TestCase testCase;

	public Result(TestCase testCase, Object expected, Object actual, int tp, int fp, int fn) {
		this.testCase = testCase;
		this.expected = expected;
		this.actual = actual;
		this.tp = tp;
		this.fp = fp;
		this.fn = fn;
	}

	@Override
	public Object getExpected() {
		return expected;
	}

	@Override
	public Object getActual() {
		return actual;
	}

	@Override
	public int truePositives() {
		return tp;
	}

	@Override
	public int falsePositives() {
		return fp;
	}

	@Override
	public int falseNegatives() {
		return fn;
	}

	@Override
	public ITestCase getTestCase() {
		return testCase;
	}
	
	@Override
	public String toString() {
		return testCase.getSignature() + ": TP: " + tp + ", FP: " + fp + ", FN: " + fn + ", Precision: " + getPrecision() + ", Recall: " + getRecall() + "; Expected: " + clip(getExpected()) + ", Actual: " + clip(getActual());
	}

	private String clip(Object obj) {
		String s = String.valueOf(obj);
		int origLen = s.length();
		if (origLen > 100)
			s = s.substring(0, 100) + " [clipped, original length: " + origLen + "]";
		return s;
	}

}
