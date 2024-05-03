package de.fraunhofer.sit.harvester.loggingpoints;



/**
 * Contains information about an exception which has
 * been logged at runtime
 * @author Marc Miltenberger
 */
public class ExceptionResult extends LoggingPointInfo {
	private int path;
	private String stackTrace;
	private String message;

	/**
	 * Creates a new exception result
	 * @param info the logging point
	 * @param path the path
	 * @param message the message
	 * @param stackTrace the stacktrace
	 */
	public ExceptionResult(LoggingPointInfo info, int path, String message, String stackTrace) {
		super(info.getId(), info.getMethodSignature(), info.getLoggingPointRightBefore(), info.getAdditionalInformation(), info.getCodeLine(), info.getCodeColumn(), info.getLoggedValues());
		this.path = path;			
		this.message = message;
		this.stackTrace = stackTrace;
	}
	public ExceptionResult(int path, String message, String stackTrace) {
		super(-1, null, null, null, 0, 0, null);
		this.path = path;			
		this.message = message;
		this.stackTrace = stackTrace;
	}
	
	/**
	 * Returns the path number
	 * @return the path
	 */
	public int getPath() {
		return path;
	}
	
	/**
	 * Returns the message
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	
	/**
	 * Returns the stacktrace
	 * @return stacktrace
	 */
	public String getStackTrace() {
		return stackTrace;
	}

	/**
	 * Returns additional information
	 * @return additional information
	 */
	public String getAdditionalInformation() {
		return additionalInformation;
	}

	@Override
	public String toString() {
        StringBuilder sb = new StringBuilder("Exception (LP " + id + "): At path " + path + " to Method " + methodSignature + "\n @ statement " + stmt);
        if (row != -1)
        	sb.append(" @ Line " + row + ", Column " + col);
        sb.append("\n" + additionalInformation);
        sb.append("\nMessage: " + message);
        sb.append("\n" + stackTrace);
        return sb.toString();
	}
}
