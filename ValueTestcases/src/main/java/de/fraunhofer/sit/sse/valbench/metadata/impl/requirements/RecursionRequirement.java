package de.fraunhofer.sit.sse.valbench.metadata.impl.requirements;

import de.fraunhofer.sit.sse.valbench.metadata.interfaces.IRequirement;

public class RecursionRequirement implements IRequirement {

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
		return "Recursion";
	}
}