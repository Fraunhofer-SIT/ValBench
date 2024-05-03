package de.fraunhofer.sit.sse.valbench;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.GsonBuilder;

public class ExplicitLoggingPoint {
	static class ExpectedResult {
		public final Throwable location;
		public final Object[] expected;

		public ExpectedResult(Throwable location, Object[] expected) {
			this.location = location;
			this.expected = expected;
		}

		public String getClassName() {
			return getTop().getClassName();
		}

		public String getMethodName() {
			return getTop().getMethodName();
		}

		public int getLineNumber() {
			return getTop().getLineNumber();
		}

		@Override
		public String toString() {
			StackTraceElement top = getTop();
			return "Location: " + top.getClassName() + ": " + top.getMethodName() + ":" + top.getLineNumber()
					+ "; Expected: " + Arrays.toString(expected);
		}

		private StackTraceElement getTop() {
			return location.getStackTrace()[1];
		}

	}

	static ThreadLocal<List<ExpectedResult>> results = new ThreadLocal<List<ExpectedResult>>();
	
	public static boolean OUTPUT = true;

	public static void explicitLoggingPoint(Object o) {
		explicitLoggingPoint(new Object[] { o } );
	}
	public static void explicitLoggingPoint(Object... o) {
		List<ExpectedResult> r = getExpectedResults();
		r.add(new ExpectedResult(new Throwable(), o));
		if (OUTPUT)
			System.out.println("Reporting value: " + o[0]);
	}

	public static void clearResults() {
		results.remove();
	}

	public static boolean hasResults() {
		List<ExpectedResult> r = results.get();
		return r != null && !r.isEmpty();
	}

	public static List<ExpectedResult> getExpectedResults() {
		List<ExpectedResult> r = results.get();
		if (r == null) {
			r = new ArrayList<>();
			results.set(r);
		}
		return r;
	}
	
}
