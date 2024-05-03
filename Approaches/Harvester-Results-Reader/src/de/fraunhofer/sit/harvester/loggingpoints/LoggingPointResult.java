package de.fraunhofer.sit.harvester.loggingpoints;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * As opposed to LoggingPoint, this class contains the resulting variable values.
 * Therefore it can be used after a program run to obtain the actual variable values.<p>
 * Pleasen note that due to the design all variables are stored as Strings (toString() of
 * the variables has been called).
 * @author Marc Miltenberger
 */
public class LoggingPointResult extends LoggingPointInfo {

	public int path;
	Map<String, String> variables = new LinkedHashMap<String, String>();
	public long dateTriggered;

	/**
	 * Creates a new logging point result
	 * @param info the logging point
	 * @param path the path
	 * @param values the values of the variables at the logging point
	 */
	 public LoggingPointResult(LoggingPointInfo info, int path, String[] values, long triggerDate) {
		super(info.getId(), info.getMethodSignature(), info.getLoggingPointRightBefore(), info.getAdditionalInformation(), info.getCodeLine(), info.getCodeColumn(), info.getLoggedValues());
		this.path = path;
		this.dateTriggered = triggerDate;
		for (int i = 0; i < values.length; i++)
		{
			variables.put(info.loggedValues[i], values[i]);
		}
			
	}

	
	/**
	 * Returns the path number
	 * @return the path number
	 */
	public int getPath() {
		return path;
	}
	
	/**
	 * Returns a map of variable names -> variable values at this logging point.
	 * @return the map of variable values
	 */
	public Map<String, String> getVariables() {
		return variables;
	}
	
	/**
	 * Returns the set of variable names at this logging point
	 * @return the set of variable names at this logging point
	 */
	public Set<String> getVariableNames() {
		return variables.keySet();
	}
	
	/**
	 * Returns all variable values at this logging point
	 * @return all variable values at this logging point
	 */
	public Collection<String> getVariableValues() {
		return variables.values();
	}
	
	/**
	 * Returns the variable value of the given variable name.<br>
	 * If the variable could not be found, null is being returned.
	 * @param varName the variable name
	 * @return the variables value or null
	 */
	public String getVariableValue(String varName) {
		return variables.get(varName);
	}

	@Override
	public String toString() {
        StringBuilder sb = new StringBuilder("LoggingPoint " + id + " - Method " + methodSignature + "\n @ statement " + stmt + "; path " + path);
        if (row != -1)
        	sb.append(" @ Line " + row + ", Column " + col);
        sb.append("\n" + additionalInformation);
        sb.append("\n");
        Iterator<Entry<String, String>> iter = variables.entrySet().iterator();
        while (iter.hasNext()) {
            Entry<String, String> entry = iter.next();
            sb.append(entry.getKey());
            sb.append('=').append('"');
            sb.append(entry.getValue());
            sb.append('"');
            sb.append('\n');
        }
        return sb.toString();
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + path;
		result = prime * result
				+ ((variables == null) ? 0 : variables.hashCode());
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
		LoggingPointResult other = (LoggingPointResult) obj;
		if (path != other.path)
			return false;
		if (variables == null) {
			if (other.variables != null)
				return false;
		} else if (!variables.equals(other.variables))
			return false;
		return true;
	}
	
	
}
