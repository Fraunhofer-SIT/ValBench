package de.fraunhofer.sit.sse.valbench.metadata.impl.requirements;

import java.util.Objects;

import de.fraunhofer.sit.sse.valbench.metadata.interfaces.IRequirement;

public class ReflectionRequirement implements IRequirement {

	@Override
	public int hashCode() {
		return 1337;
	}

	@Override
	public boolean equals(Object obj) {
		return getClass() == obj.getClass();
	}
	
	@Override
	public String toString() {
		return "Reflection";
	}

}
