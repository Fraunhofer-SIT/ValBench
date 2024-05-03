package de.fraunhofer.sit.sse.valbench.metadata.impl.requirements;

import de.fraunhofer.sit.sse.valbench.metadata.interfaces.IRequirement;

public class AndroidRequirement implements IRequirement {

	public static final AndroidRequirement INSTANCE = new AndroidRequirement();

	@Override
	public int hashCode() {
		return 1338;
	}

	@Override
	public boolean equals(Object obj) {
		return getClass() == obj.getClass();
	}
	
	@Override
	public String toString() {
		return "Android";
	}
}
