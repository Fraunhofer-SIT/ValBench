package de.fraunhofer.sit.harvester.loggingpoints;

public class RuntimeInformation {
	private int msTook;
	private boolean timeoutHitted;
	
	public RuntimeInformation(int msTook, boolean timeoutHitted) {
		this.msTook = msTook;
		this.timeoutHitted = timeoutHitted;
	}
	
	public int getMsTook() {
		return msTook;
	}

	public boolean isTimeoutHitted() {
		return timeoutHitted;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + msTook;
		result = prime * result + (timeoutHitted ? 1231 : 1237);
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
		RuntimeInformation other = (RuntimeInformation) obj;
		if (msTook != other.msTook)
			return false;
		if (timeoutHitted != other.timeoutHitted)
			return false;
		return true;
	}
	
	
}

