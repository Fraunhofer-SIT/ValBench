package de.fraunhofer.sit.sse.valbench.modellingtests;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.fraunhofer.sit.sse.valbench.metadata.ValueComputationTestCase;

public class PatternTest {

	@ValueComputationTestCase
	public static String testPattern() throws UnsupportedEncodingException {
		Pattern p = Pattern.compile("a.*b");
		Matcher m = p.matcher("atestbalab");
		return "Matches: " + m.matches() + "\nGroup: " + m.group() + "\nGroup(0): " + m.group(0) + "\nStart: "
				+ m.start() + "\nEnd: " + m.end() + "\nRegionStart: " + m.regionStart() + "\nPattern: "
				+ m.pattern().pattern() + "\nFlags: " + m.pattern().flags() + "\nStart: " + m.toMatchResult().start()
				+ "\nStart(0): " + m.toMatchResult().start(0) + "\nRep" + Pattern.quote("foo'\\$(I\")!Q)");

	}

	@ValueComputationTestCase
	public static String testPattern2() throws UnsupportedEncodingException {
		Pattern p = Pattern.compile("a.*b");
		Matcher m = p.matcher("atestbalab");
		return "replaceAll: " + m.replaceAll("wrt") + "\nreplaceFirst:" + m.replaceFirst("x");

	}

}