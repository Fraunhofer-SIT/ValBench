package de.fraunhofer.sit.sse.valbench.metadata;

import de.fraunhofer.sit.sse.valbench.metadata.interfaces.IStatisticResult;

public class StatisticResultsAggregator implements IStatisticResult {
	
	private Iterable<? extends IStatisticResult> inner;
	private int tp;
	private int fn;
	private int fp;

	public StatisticResultsAggregator(Iterable<? extends IStatisticResult> inner) {
		this.inner = inner;
		for (IStatisticResult i : inner) {
			this.tp += i.truePositives();
			this.fp += i.falsePositives();
			this.fn += i.falseNegatives();
		}
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
	public String toString() {
		return "TP: " + tp + ", FP: " + fp + ", FN: " + fn + ", Precision: " + getPrecision() + ", Recall: " + getRecall();
	}


}
