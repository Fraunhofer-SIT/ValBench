package de.fraunhofer.sit.sse.valbench.metadata.interfaces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import de.fraunhofer.sit.sse.valbench.metadata.impl.TestCase;
import de.fraunhofer.sit.sse.valbench.metadata.impl.results.Result;

public interface IResultComparisonStrategy {

	public boolean verifySingleResult(Object expected, Object actual);

	public default IResult verifyResult(TestCase testCase, Set<Object> expectedResults, Object res) {
		List<Object> actualResults = new ArrayList<>();
		if (res instanceof Iterable) {
			Iterable<Object> it = (Iterable<Object>) res;
			for (Object d : it)
				actualResults.add(d);
		} else
			actualResults.add(res);
		
		int tp = 0, fp = 0, fn = 0;
		
		List<Object> onlyIncorrect = new ArrayList<>(actualResults);
		List<Object> onlyMissingExpected = new ArrayList<>(expectedResults);
		Iterator<Object> it = onlyIncorrect.iterator();
		next:
		while (it.hasNext()) {
			Object foundSingle = it.next();
			Iterator<Object> itExp = onlyMissingExpected.iterator();
			while (itExp.hasNext()) {
				Object d = itExp.next();
				if (verifySingleResult(d, foundSingle)) {
					itExp.remove();
					it.remove();
					tp++;
					break;
				}
			}
		}
		fp = onlyIncorrect.size(); //unmatched actual results
		fn = onlyMissingExpected.size(); //missing expected results
		
		return new Result(testCase, unpack(expectedResults), unpack(actualResults), tp, fp, fn);

	}

	static Object unpack(Collection<Object> s) {
		if (s.size() == 1)
			return s.iterator().next();
		return s;
	}

}
