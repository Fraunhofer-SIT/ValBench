package de.fraunhofer.sit.sse.valbench.metadata.impl.requirements;

import java.util.Objects;

import de.fraunhofer.sit.sse.valbench.metadata.interfaces.IAPIModelRequirement;

public class ModelledAPIFieldRequirement implements IAPIModelRequirement {
	@Override
	public int hashCode() {
		return Objects.hash(fieldSignature);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ModelledAPIFieldRequirement other = (ModelledAPIFieldRequirement) obj;
		return Objects.equals(fieldSignature, other.fieldSignature);
	}

	private String fieldSignature;
	private String declaringClass;
	
	public ModelledAPIFieldRequirement(String declaringClass, String fieldSig) {
		this.declaringClass = declaringClass;
		this.fieldSignature = fieldSig;
	}
	
	public String getDeclaringClass() {
		return declaringClass;
	}
	
	@Override
	public String toString() {
		return "Modelled field: " + fieldSignature;
	}
}
