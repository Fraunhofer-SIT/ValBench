package de.fraunhofer.sit.harvester.loggingpoints;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Contains information about the logging point.
 * As the unit of the logging point might not be loaded,
 * there is no direct relationship to the unit.
 * @author Marc Miltenberger
 */
public class LoggingPointInfo {

	protected String stmt;
	protected int col, row;
	protected String[] loggedValues;
	protected String additionalInformation = "";
	protected String methodSignature;
	protected int id;
	
	
	public LoggingPointInfo(int id, String methodSignature, String statement, String additionalInformation, int codeLine, int codeCol, String[] values) {
		this.id = id;
		this.methodSignature = methodSignature;
		this.stmt = statement;
		this.additionalInformation = additionalInformation;
		this.row = codeLine;
		this.col = codeCol;
		this.loggedValues = values;
	}
	
	/**
	 * Returns the java code line (or -1 if no information is available)
	 * @return the java code line (or -1 if no information is available)
	 */
	public int getCodeLine() {
		return row;
	}
	
	/**
	 * Returns the java code column (or -1 if no information is available)
	 * @return the java code column (or -1 if no information is available)
	 */
	public int getCodeColumn() {
		return col;
	}
	
	/**
	 * Returns some additional information you wish to store.
	 * @return the additionalInformation
	 */
	public String getAdditionalInformation() {
		return additionalInformation;
	}

	/**
	 * Sets some additional information you wish to store.
	 * @param additionalInformation the additionalInformation to set
	 */
	public void setAdditionalInformation(String additionalInformation) {
		this.additionalInformation = additionalInformation;
	}
	
	/**
	 * Returns the point in the program where the logging point should be placed.
	 * It will be placed right before the returned point.
	 * @return the point in program where the logging takes place.
	 */
	public String getLoggingPointRightBefore() {
		return stmt;
	}

	/**
	 * Returns an array of all variable names which should be logged
	 * @return an array of all variable names which should be logged
	 */
	public String[] getLoggedValues() {
		return loggedValues;
	}


	@Override
	public String toString() {
        StringBuilder sb = new StringBuilder("LoggingPoint ID " + id + "; Method " + methodSignature + "\n @ statement " + stmt);
        if (row != -1)
        	sb.append(" Line " + row + ", Column " + col);
        sb.append("\n" + additionalInformation);
        return sb.toString();
	}

	/**
	 * Returns the method signature where the logging point
	 * originally was.
	 * @return the method signature
	 */
	public String getMethodSignature() {
		return methodSignature;
	}


	/**
	 * Returns the id of the logging point
	 * @return the id of the logging point
	 */
	public int getId() {
		return id;
	}
	

	/**
	 * Tries to match the logging point with the result list.
	 * @param list returns a list of logging point results probably from this logging point
	 * @return the matching logging point results
	 */
	public List<LoggingPointResult> findMatchingLoggingPointResults(ExtractionResults results) {
		List<LoggingPointResult> matching = new ArrayList<LoggingPointResult>();
		for (LoggingPointResult element : results.getResults()) {
			if (element.getId() == id)
				matching.add(element);
		}
		return matching;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((additionalInformation == null) ? 0 : additionalInformation
						.hashCode());
		result = prime * result + col;
		result = prime * result + id;
		result = prime * result + Arrays.hashCode(loggedValues);
		result = prime * result
				+ ((methodSignature == null) ? 0 : methodSignature.hashCode());
		result = prime * result + row;
		result = prime * result + ((stmt == null) ? 0 : stmt.hashCode());
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
		LoggingPointInfo other = (LoggingPointInfo) obj;
		if (additionalInformation == null) {
			if (other.additionalInformation != null)
				return false;
		} else if (!additionalInformation.equals(other.additionalInformation))
			return false;
		if (col != other.col)
			return false;
		if (id != other.id)
			return false;
		if (!Arrays.equals(loggedValues, other.loggedValues))
			return false;
		if (methodSignature == null) {
			if (other.methodSignature != null)
				return false;
		} else if (!methodSignature.equals(other.methodSignature))
			return false;
		if (row != other.row)
			return false;
		if (stmt == null) {
			if (other.stmt != null)
				return false;
		} else if (!stmt.equals(other.stmt))
			return false;
		return true;
	}
	
	

}

