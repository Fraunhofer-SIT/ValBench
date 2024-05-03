package de.fraunhofer.sit.harvester.loggingpoints;

import java.io.IOException;
import java.io.StringReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * The main class used to read the results of Harvester
 * @author Marc Miltenberger
 */
public final class ExtractionResults {
	public static final String TOOK_ANALYSIS_MS = "TookAnalysisMs";
	public static final String ANALYSIS_DATE = "AnalysisDate";
	public static final String MD5 = "md5";
	public static final String NUMBER_OF_PATHES = "NumberOfPathes";
	public static final String RUNTIME = "Runtime";
	public static final String AVGEXECUTORVARIABLES = "AvgExecutorVariables";
	public static final String NOVALUE = "NO_VALUE";
	public static final String NUMBER_OF_FILLED_PARAMS = "NumberOfFilledParams";
	public static final String NUMBER_OF_STATIC_VALUES_NEEDED = "NumberOfStaticValuesStillNeeded";
	public static final String PARAMETER = "Parameter";
	public static final String STARTUP_TIME = "StartupTime";
	
	Set<LoggingPointResult> allResults = new LinkedHashSet<LoggingPointResult>();
	

	Map<LoggingPointAndPathCombination, RuntimeInformation> runtimeInformation = new LinkedHashMap<LoggingPointAndPathCombination, RuntimeInformation>();
	final Set<ExceptionResult> resultsExceptions = new LinkedHashSet<ExceptionResult>();
	Map<Integer, ArrayList<LoggingPointResult>> resultsPerId = new LinkedHashMap<Integer, ArrayList<LoggingPointResult>>();
	int tookAnalysisMs;
	String analysisDate;
	String md5Sum;
	Map<Integer, LoggingPointInfo> allLoggingPoints = new LinkedHashMap<Integer, LoggingPointInfo>();
	int runTime, numberOfPathes;
	double avgExecutorVariables;
	int numberOfFilledParameters;
	//private int numberofStaticValuesNeeded;
	String parameters;
	int startupTime;
	
	
	ExtractionResults() {
	}
	
	public ExtractionResults(String parameters, String analysisDate,
			double avgExecutorVariables, String md5,
			int numberOfFilledParameters, int numberOfPathes,
			int tookAnalysisMs, int startupTime) {
		this.parameters = parameters;
		this.analysisDate = analysisDate;
		this.avgExecutorVariables = avgExecutorVariables;
		this.md5Sum = md5;
		this.numberOfFilledParameters = numberOfFilledParameters;
		this.numberOfPathes = numberOfPathes;
		this.tookAnalysisMs = tookAnalysisMs;
		this.startupTime = startupTime;
	}

	public void setRuntimeMs(int ms) {
		this.runTime = ms;
	}
	
	public Map<LoggingPointAndPathCombination, RuntimeInformation> getRuntimeInformation() {
		return runtimeInformation;
	}
	
	public String getParameters() {
		return parameters;
	}

	public String getAnalysisDate() {
		return analysisDate;
	}
	
	public int getRuntimeMs() {
		return runTime;
	}
	
	public int getStartupTime() {
		return startupTime;
	}
	
	public int getStaticAnalysisMs() {
		return tookAnalysisMs;
	}
	
	public String getMD5Sum() {
		return md5Sum;
	}
	
	public int getNumberOfPathes() { 
		return numberOfPathes;
	}
	
	public double getAvgExecutorVariables() {
		return avgExecutorVariables;
	}
	
	
	public int getNumberOfFilledParameters() {
		return numberOfFilledParameters;
	}
	
	
	public Collection<LoggingPointInfo> getAllLoggingPoints() {
		return allLoggingPoints.values();
	}
	
	public Map<Integer, ArrayList<LoggingPointResult>> getResultsPerLoggingPoint() {
		return resultsPerId;
	}
	
	/**
	 * Returns all successful results
	 * @return all successful results
	 */
	public Collection<LoggingPointResult> getResults() {
		return allResults;
	}
	
	/**
	 * Returns all exceptional results
	 * @return all exceptional results
	 */
	public Set<ExceptionResult> getExceptionResults() {
		return resultsExceptions;
	}
	
	public static String[] split(String s) {
		StringReader r = new StringReader(s);
		List<String> results = new LinkedList<String>();
		String chunkSize = "0";
		while (true) {
			int c = -1;
			try {
				c = r.read();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (c == -1)
				break;
			
			char character = (char)c;
			switch (character) {
				case '|':
					char[] currentChunk = new char[Integer.parseInt(chunkSize)];
					try {
						r.read(currentChunk);
					} catch (IOException e) {
						e.printStackTrace();
					}
					results.add(new String(currentChunk));
					chunkSize = "0";
					break;
					
				default:
					chunkSize += character;
					break;
			}
		}
		
		return results.toArray(new String[results.size()]);
	}
	
	

	static String readMetadata(Statement stmt, String headerValue, String defValue) throws SQLException {
   		ResultSet set = stmt.executeQuery("SELECT value FROM Metadata WHERE name = '" + headerValue + "';");
		if (!set.next())
		{
			set.close();
			return defValue;
		}
		
		String res = set.getString(1);
		set.close();
		return res;
	}
	public Set<LoggingPointInfo> getMissingLoggingPoints() {
		List<Integer> missingLoggingPointIds = new ArrayList<Integer>();
		missingLoggingPointIds.addAll(allLoggingPoints.keySet());
		for (LoggingPointResult point : this.getResults()) {
			int index = missingLoggingPointIds.indexOf(point.id);
			if (index != -1)
				missingLoggingPointIds.remove(index);
		}
		
		Set<LoggingPointInfo> missingLoggingPoints = new LinkedHashSet<LoggingPointInfo>();
		for (int id : missingLoggingPointIds)
			missingLoggingPoints.add(this.allLoggingPoints.get(id));
		return missingLoggingPoints;
	}
	
	@Override
	public String toString() {
		String toString = "Parameters: " + parameters + "\n" +  
		"Distinct results: " + getResults().size() + "\n";
		toString += "Distinct exceptions: " + getExceptionResults().size() + "\n";
		toString += "Static analysis date: " + getAnalysisDate() + "\n";
		toString += "Static analysis took: " + getStaticAnalysisMs() + " ms\n";
		toString += "Dynamic analysis took: " + getRuntimeMs() + " ms\n";
		toString += "Startup time: " + startupTime + "ms \n";
		toString += "Avg. executor variables: " + getAvgExecutorVariables() + "\n";
		toString += "Number of filled parameters: " + getNumberOfFilledParameters() + "\n";
		toString += "MD5 sum of sample: " + getMD5Sum() + "\n";
		toString += "Number of pathes: " + getNumberOfPathes() + "\n";
		toString += "Number of exceptions: " + getExceptionResults().size() + "\n";
		toString += "Number of declared Logging points: " + getAllLoggingPoints().size() + "\n";
		toString += "Number of Logging points with results: " + getResultsPerLoggingPoint().size() + "\n";
		int all = 0, timeouts = 0;
		for (RuntimeInformation r : runtimeInformation.values()) {
			if (r.isTimeoutHitted())
				timeouts++;
			all++;
		}
		toString += "Runtime timeouts: " + timeouts + "/" + all + "\n";
		return toString;
	}

	public void addExceptionResults(ExceptionResult exceptionResult) {
		resultsExceptions.add(exceptionResult);
	}

	public void addLoggingPoint(LoggingPointInfo loggingPointInformation) {
		if (!allLoggingPoints.containsKey(loggingPointInformation.getId()))
			allLoggingPoints.put(loggingPointInformation.getId(), loggingPointInformation);
	}

	public LoggingPointInfo getLoggingPointInformation(int loggingPointId) {
		return allLoggingPoints.get(loggingPointId);
	}

	public void addResult(LoggingPointResult result) {
		synchronized (this) {
			allResults.add(result);
			ArrayList<LoggingPointResult> lresult = resultsPerId.get(result.getId());
			if (lresult == null)
			{
				lresult  = new ArrayList<LoggingPointResult>();
				resultsPerId.put(result.getId(), lresult);
			}
			lresult.add(result);
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((allLoggingPoints == null) ? 0 : allLoggingPoints.hashCode());
		result = prime * result
				+ ((allResults == null) ? 0 : allResults.hashCode());
		result = prime * result
				+ ((analysisDate == null) ? 0 : analysisDate.hashCode());
		long temp;
		temp = Double.doubleToLongBits(avgExecutorVariables);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((md5Sum == null) ? 0 : md5Sum.hashCode());
		result = prime * result + numberOfFilledParameters;
		result = prime * result + numberOfPathes;
		result = prime * result
				+ ((parameters == null) ? 0 : parameters.hashCode());
		result = prime
				* result
				+ ((resultsExceptions == null) ? 0 : resultsExceptions
						.hashCode());
		result = prime * result
				+ ((resultsPerId == null) ? 0 : resultsPerId.hashCode());
		result = prime * result + runTime;
		result = prime
				* result
				+ ((runtimeInformation == null) ? 0 : runtimeInformation
						.hashCode());
		result = prime * result + tookAnalysisMs;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ExtractionResults other = (ExtractionResults) obj;
		if (allLoggingPoints == null) {
			if (other.allLoggingPoints != null)
				return false;
		} else if (!allLoggingPoints.equals(other.allLoggingPoints))
			return false;
		if (allResults == null) {
			if (other.allResults != null)
				return false;
		} else if (!allResults.equals(other.allResults))
			return false;
		if (analysisDate == null) {
			if (other.analysisDate != null)
				return false;
		} else if (!analysisDate.equals(other.analysisDate))
			return false;
		if (Double.doubleToLongBits(avgExecutorVariables) != Double
				.doubleToLongBits(other.avgExecutorVariables))
			return false;
		if (md5Sum == null) {
			if (other.md5Sum != null)
				return false;
		} else if (!md5Sum.equals(other.md5Sum))
			return false;
		if (numberOfFilledParameters != other.numberOfFilledParameters)
			return false;
		if (numberOfPathes != other.numberOfPathes)
			return false;
		if (parameters == null) {
			if (other.parameters != null)
				return false;
		} else if (!parameters.equals(other.parameters))
			return false;
		if (resultsExceptions == null) {
			if (other.resultsExceptions != null)
				return false;
		} else if (!resultsExceptions.equals(other.resultsExceptions))
			return false;
		if (resultsPerId == null) {
			if (other.resultsPerId != null)
				return false;
		} else if (!resultsPerId.equals(other.resultsPerId))
			return false;
		if (runTime != other.runTime)
			return false;
		if (runtimeInformation == null) {
			if (other.runtimeInformation != null)
				return false;
		} else if (!runtimeInformation.equals(other.runtimeInformation))
			return false;
		if (tookAnalysisMs != other.tookAnalysisMs)
			return false;
		return true;
	}

	public String toVerboseString() {
		StringBuilder builder = new StringBuilder(1024 * 256);
		builder.append(toString()).append("\n");
		for (ExceptionResult result : getExceptionResults()) {
			builder.append(result.toString()).append("\n");
		}
		builder.append("-----------------------------------\n");
		for (LoggingPointResult result : getResults()) {
			builder.append(result.toString()).append("\n");
		}
		return builder.toString();
	}
	
	
}
