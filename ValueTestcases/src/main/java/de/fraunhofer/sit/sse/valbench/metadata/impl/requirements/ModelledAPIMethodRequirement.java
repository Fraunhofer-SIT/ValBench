package de.fraunhofer.sit.sse.valbench.metadata.impl.requirements;

import java.util.Objects;

import de.fraunhofer.sit.sse.valbench.metadata.interfaces.IAPIModelRequirement;
import de.fraunhofer.sit.sse.valbench.metadata.interfaces.IRequirement;

public class ModelledAPIMethodRequirement implements IAPIModelRequirement {
	private String methodSignature, declaringClass;

	public ModelledAPIMethodRequirement(String declaringClass, String signature) {
		this.declaringClass = declaringClass;
		methodSignature = signature;
	}

	@Override
	public int hashCode() {
		return Objects.hash(methodSignature);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ModelledAPIMethodRequirement other = (ModelledAPIMethodRequirement) obj;
		return Objects.equals(methodSignature, other.methodSignature);
	}
	
	public String getMethodSignature() {
		return methodSignature;
	}
	
	@Override
	public String toString() {
		return "Modelled method: " + methodSignature;
	}

	@Override
	public String getDeclaringClass() {
		return declaringClass;
	}
}
