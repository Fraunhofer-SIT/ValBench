package de.fraunhofer.sit.sse.valbench.metadata.interfaces;

public interface IStatisticResult {

	public int truePositives();

	public int falsePositives();
	
	public int falseNegatives();
	
	public default double getPrecision() {
		return (double) truePositives() / (truePositives() + falsePositives());
	}
	
	public default double getRecall() {
		return (double) truePositives() / (truePositives() + falseNegatives());
	}
	

}
