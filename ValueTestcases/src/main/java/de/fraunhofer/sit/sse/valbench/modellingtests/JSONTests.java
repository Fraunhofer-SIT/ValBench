package de.fraunhofer.sit.sse.valbench.modellingtests;

import org.json.JSONException;
import org.json.JSONObject;

import de.fraunhofer.sit.sse.valbench.metadata.ValueComputationTestCase;

public class JSONTests {

	@ValueComputationTestCase
	public static String testSimple() throws JSONException {
		return new JSONObject("{\n" + "    \"test\": \"b\"" + "}").getString("test");
	}

	@ValueComputationTestCase
	public static String testSimple2() throws JSONException {

		return new JSONObject("{\n" + " \"parent\": {" + "    \"test\": \"b\"" + "} }").getJSONObject("parent")
				.getString("test");
	}

	@ValueComputationTestCase
	public static String testSimple3() throws JSONException {
		return new JSONObject("{\n" + " \"parent\": [ { \"test\": \"b\"} , { \"Foo\": \"a\"} ] \n" + "}")
				.getJSONArray("parent").get(1).toString();
	}

	@ValueComputationTestCase
	public static String testSimple4() throws JSONException {
		return new JSONObject("{\n" + " \"parent\": [ \"test\", \"a\" ] \n" + "}").getJSONArray("parent").getString(1);
	}

	public enum Enum {
		a, b
	}

	@ValueComputationTestCase
	public static String testEnum() throws JSONException {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("Foo", Enum.a);
		return jsonObject.toString();
	}
}
