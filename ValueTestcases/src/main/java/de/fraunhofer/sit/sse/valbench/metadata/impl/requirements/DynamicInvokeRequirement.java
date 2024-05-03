package de.fraunhofer.sit.sse.valbench.metadata.impl.requirements;

import java.util.Objects;

import de.fraunhofer.sit.sse.valbench.metadata.interfaces.IRequirement;

public class DynamicInvokeRequirement implements IRequirement {
	@Override
	public int hashCode() {
		return Objects.hash(statement);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DynamicInvokeRequirement other = (DynamicInvokeRequirement) obj;
		return Objects.equals(statement, other.statement);
	}

	public DynamicInvokeRequirement(String statement) {
		this.statement = statement;
	}
	
	@Override
	public String toString() {
		return "Dynamic invoke: " + statement;
	}

	public String statement;
}
