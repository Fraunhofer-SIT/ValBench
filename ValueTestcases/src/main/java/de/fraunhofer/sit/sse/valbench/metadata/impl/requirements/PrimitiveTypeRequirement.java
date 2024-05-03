package de.fraunhofer.sit.sse.valbench.metadata.impl.requirements;

import java.util.Objects;

import de.fraunhofer.sit.sse.valbench.metadata.interfaces.IRequirement;

public class PrimitiveTypeRequirement implements IRequirement {

	private String type;

	public PrimitiveTypeRequirement(Class<?> javaPrimitiveType) {
		this.type = javaPrimitiveType.getName();
	}

	@Override
	public int hashCode() {
		return Objects.hash(type);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PrimitiveTypeRequirement other = (PrimitiveTypeRequirement) obj;
		return Objects.equals(type, other.type);
	}
	
	@Override
	public String toString() {
		return "Primitive type - " + type;
	}
	
	

}
