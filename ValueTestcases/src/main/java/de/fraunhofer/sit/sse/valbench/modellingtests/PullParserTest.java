package de.fraunhofer.sit.sse.valbench.modellingtests;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.Random;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import de.fraunhofer.sit.sse.valbench.metadata.Requires;
import de.fraunhofer.sit.sse.valbench.metadata.ValueComputationTestCase;

@Requires(requiredClasses = { "org.xmlpull.v1.XmlSerializer" })
public class PullParserTest {
	static String str = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n" + "<rootelement1>\r\n" + "    <subelement>\r\n"
			+ "        Hello XML Sub-Element 1\r\n" + "    </subelement>\r\n" + "    <subelement>\r\n"
			+ "        Hello XML Sub-Element 2\r\n" + "        <subsubelement>Sub Sub Element</subsubelement>\r\n"
			+ "    </subelement>\r\n" + "    <subelement>\r\n" + "        Hello XML Sub-Element 2\r\n"
			+ "        <subsubelement>Sub Sub Element</subsubelement>\r\n" + "    </subelement>\r\n"
			+ "    <subelement>\r\n" + "        Hello XML Sub-Element 2\r\n"
			+ "        <subsubelement>Sub Sub Element</subsubelement>\r\n" + "    </subelement>\r\n"
			+ "</rootelement1>";

	@ValueComputationTestCase
	public static String testSimple() throws IOException, XmlPullParserException {
		XmlPullParser p = XmlPullParserFactory.newInstance().newPullParser();
		p.setInput(new StringReader(str));
		String s = "" + p.nextTag();
		s += p.nextTag();
		s += p.nextText();
		return s;

	}

	@ValueComputationTestCase
	public static String testSimple2() throws IOException, XmlPullParserException {
		XmlPullParser p = XmlPullParserFactory.newInstance().newPullParser();
		p.setInput(new ByteArrayInputStream(str.getBytes("UTF-8")), "UTF-8");
		String s = "" + p.nextTag();
		s += p.nextTag();
		s += p.nextText();
		return s;

	}

	@ValueComputationTestCase
	public static String testSimple3() throws IOException, XmlPullParserException {
		XmlPullParser p = XmlPullParserFactory.newInstance().newPullParser();
		p.setInput(new ByteArrayInputStream(str.getBytes("UTF-8")), "UTF-8");
		String s = "" + p.nextTag();
		if (new Random().nextBoolean())
			s += p.nextTag();
		else
			s += p.nextTag();
		s += p.nextText();
		return s;

	}


}
