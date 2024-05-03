package de.fraunhofer.sit.sse.valbench.modellingtests;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;

import de.fraunhofer.sit.sse.valbench.metadata.ValueComputationTestCase;

public class PropertiesTests {

	@ValueComputationTestCase
	public static String atestPropertiesLoad() throws IOException {
		Properties prop = new Properties();
		prop.put("a", "aVal");
		ByteArrayOutputStream o = new ByteArrayOutputStream();
		prop.save(o, "");

		Properties p3 = new Properties();
		p3.load(new ByteArrayInputStream(o.toByteArray()));
		return p3.getProperty("a");
	}

	@ValueComputationTestCase
	public static String testProperties() throws IOException {
		Properties prop = new Properties();
		prop.put("a", "aVal");
		prop.setProperty("Foo", "TT");
		Properties prop2 = new Properties(prop);
		prop2.putIfAbsent("a", "bb");
		ByteArrayOutputStream o = new ByteArrayOutputStream();
		prop2.save(o, "");
		ByteArrayOutputStream o2 = new ByteArrayOutputStream();
		prop2.storeToXML(o2, "", "UTF-8");

		Properties p3 = new Properties();
		p3.load(new ByteArrayInputStream(o.toByteArray()));
		Properties p4 = new Properties();
		p4.loadFromXML(new ByteArrayInputStream(o2.toByteArray()));
		return prop2.getProperty("a") + "_" + prop2.getProperty("foo", "xx") + "_" + p3.getProperty("a") + "_"
				+ prop.getProperty("Foo") + "_" + p4.toString();
	}

}
