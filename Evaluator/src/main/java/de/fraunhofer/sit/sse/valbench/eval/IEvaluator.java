package de.fraunhofer.sit.sse.valbench.eval;

import java.io.File;

import de.fraunhofer.sit.sse.valbench.metadata.interfaces.ITestCase;

public interface IEvaluator {
	
	public void run(File resFile, Inputs inputs) throws Exception;
	
	public void readIn(File f) throws Exception;
	
	public Object evaluate(ITestCase testcase);
	
	public String getName();
}
