package de.fraunhofer.sit.sse.valbench.modellingtests;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.apache.commons.io.IOUtils;

import de.fraunhofer.sit.sse.valbench.metadata.ValueComputationTestCase;

public class IOCommonsTests {
	@ValueComputationTestCase
	public static String test() throws IOException {
		return IOUtils.toString(getInputStream());
	}

	@ValueComputationTestCase
	public static String test2() throws IOException {
		return IOUtils.toString(getByteArray());
	}

	@ValueComputationTestCase
	public static String test3() throws IOException {
		return Arrays.toString(IOUtils.toByteArray(getInputStream()));
	}

	@ValueComputationTestCase
	public static String test4() throws IOException {
		return Arrays.toString(IOUtils.resourceToByteArray(
				IOCommonsTests.class.getPackage().getName().replace(".", "/") + "/TestResource",
				IOCommonsTests.class.getClassLoader()));
	}

	@ValueComputationTestCase
	public static String test5() throws IOException {
		return IOUtils.resourceToString(IOCommonsTests.class.getPackage().getName().replace(".", "/") + "/TestResource",
				StandardCharsets.UTF_16, IOCommonsTests.class.getClassLoader());
	}

	@ValueComputationTestCase
	public static String test6() throws IOException {
		return IOUtils.readLines(getInputStream()).toString();
	}

	@ValueComputationTestCase
	public static String test7() throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		os.write(2);
		IOUtils.copy(getInputStream(), os);
		return os.toString();
	}

	@ValueComputationTestCase
	public static String test8() throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		Writer w = new OutputStreamWriter(os);
		os.write(2);
		IOUtils.copy(new InputStreamReader(getInputStream()), w);
		w.close();
		return os.toString();
	}

	private static byte[] getByteArray() {
		return "Foo1\n23\r\n4567".getBytes();
	}

	private static Reader getReader() {
		return new InputStreamReader(getInputStream());
	}

	private static InputStream getInputStream() {
		ByteArrayInputStream i = new ByteArrayInputStream(getByteArray());
		i.read();
		return i;
	}

}
