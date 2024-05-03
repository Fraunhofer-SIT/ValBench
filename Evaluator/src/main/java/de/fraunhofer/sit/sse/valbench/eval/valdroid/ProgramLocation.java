package de.fraunhofer.sit.sse.valbench.eval.valdroid;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProgramLocation {

	public String method;
	public String statement;
	public List<String> searchedFor = new ArrayList<>();
	public List<FindingMap> findings = new ArrayList<>();
	public int tookMs;

	public ProgramLocation(String method, String statement) {
		this.method = method;
		this.statement = statement.toString();
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getStatement() {
		return statement;
	}

	public void setStatement(String statement) {
		this.statement = statement;
	}

}
