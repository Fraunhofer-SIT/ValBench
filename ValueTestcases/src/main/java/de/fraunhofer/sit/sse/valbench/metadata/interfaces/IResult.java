package de.fraunhofer.sit.sse.valbench.metadata.interfaces;

public interface IResult extends IStatisticResult {
	public Object getExpected();

	public Object getActual();
	
	public ITestCase getTestCase();

	public default boolean isCompletelyCorrect() {
		return falsePositives() == 0 && falseNegatives() == 0;
	}
}
