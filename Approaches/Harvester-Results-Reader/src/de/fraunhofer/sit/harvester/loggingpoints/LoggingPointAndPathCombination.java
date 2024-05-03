package de.fraunhofer.sit.harvester.loggingpoints;
/**
 * Contains the tuple (LoggingPoint, Path)
 * @author Marc Miltenberger
 */
public class LoggingPointAndPathCombination {
	private LoggingPointInfo loggingPoint;
	private int path;
	
	
	
	public LoggingPointAndPathCombination(LoggingPointInfo loggingPoint, int path) {
		this.loggingPoint = loggingPoint;
		this.path = path;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((loggingPoint == null) ? 0 : loggingPoint.hashCode());
		result = prime * result + path;
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
		LoggingPointAndPathCombination other = (LoggingPointAndPathCombination) obj;
		if (loggingPoint == null) {
			if (other.loggingPoint != null)
				return false;
		} else if (!loggingPoint.equals(other.loggingPoint))
			return false;
		if (path != other.path)
			return false;
		return true;
	}

	/**
	 * Returns information about the logging point
	 * @return information about the logging point
	 */
	public LoggingPointInfo getLoggingPoint() {
		return loggingPoint;
	}
	
	/**
	 * Returns the path number
	 * @return the path number
	 */
	public int getPath() {
		return path;
	}
	

	@Override
	public String toString() {
		return "loggingPoint " + loggingPoint + ", path " + path;
	}
	
	
}