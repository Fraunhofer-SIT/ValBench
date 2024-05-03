package de.fraunhofer.sit.sse.valbench.eval;

import java.util.List;

import de.fraunhofer.sit.sse.valbench.metadata.StatisticResultsAggregator;
import de.fraunhofer.sit.sse.valbench.metadata.interfaces.IResult;

public class EvalResult {
	private final StatisticResultsAggregator aggregated;
	private IEvaluator evaluator;
	private List<IResult> results;
	
	public EvalResult(IEvaluator eval, List<IResult> results) {
		this.evaluator = eval;
		aggregated = new StatisticResultsAggregator(results);
		this.results = results;
	}

	public StatisticResultsAggregator getStatistics() {
		return aggregated;
	}

}
