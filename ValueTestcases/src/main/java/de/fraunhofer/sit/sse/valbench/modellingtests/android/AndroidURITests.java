package de.fraunhofer.sit.sse.valbench.modellingtests.android;

import java.io.File;

import android.content.ContentUris;
import android.net.Uri;
import de.fraunhofer.sit.sse.valbench.metadata.ValueComputationTestCase;

public class AndroidURITests {

	private static Uri a, b, c;

	static {

		a = Uri.parse("http://foo.com/test/?foo=bar#tee");

		b = new Uri.Builder().scheme("http").authority("foo.com").path("/test/").encodedQuery("foo=bar").fragment("tee")
				.build();

		// Try alternate builder methods.
		c = new Uri.Builder().scheme("http").encodedAuthority("foo.com").encodedPath("/test/").encodedQuery("foo=bar")
				.encodedFragment("tee").build();

	}

	@ValueComputationTestCase(jvmFails = true, expectedValues = { "robert" })
	public static String test1() {

		Uri u = Uri.parse("bob:lee").buildUpon().scheme("robert").build();
		return u.getScheme();
	}

	@ValueComputationTestCase(jvmFails = true, expectedValues = { "lee" })
	public static String test2() {

		Uri u = Uri.parse("bob:lee").buildUpon().scheme("robert").build();
		return u.getEncodedSchemeSpecificPart();
	}

	@ValueComputationTestCase(jvmFails = true, expectedValues = { "bob lee" })
	public static String test3() {

		return Uri.parse("foo:bob%20lee").getSchemeSpecificPart();
	}

	@ValueComputationTestCase(jvmFails = true, expectedValues = { "bob%20lee" })
	public static String test4() {

		return Uri.parse("foo:bob%20lee").getEncodedSchemeSpecificPart();
	}

	@ValueComputationTestCase(jvmFails = true, expectedValues = { "bob%20lee" })
	public static String test() {

		return Uri.parse("foo:?bob%20lee").getEncodedQuery();
	}

	@ValueComputationTestCase(jvmFails = true, expectedValues = { "bob%20lee" })
	public static String test5() {
		return Uri.parse("foo:#bob%20lee").getEncodedFragment();
	}

	@ValueComputationTestCase(jvmFails = true, expectedValues = { "true" })
	public static String testHierachical() {
		return String.valueOf(Uri.parse("bob").isHierarchical());
	}

	@ValueComputationTestCase(jvmFails = true, expectedValues = { "false" })
	public static String testNotHierachical() {
		return String.valueOf(Uri.parse("bob:").isHierarchical());
	}

	@ValueComputationTestCase(jvmFails = true, expectedValues = { "localhost_42" })
	public static String testAuthority1() {
		Uri u = Uri.parse("http://localhost:42");
		return u.getHost() + "_" + u.getPort();
	}

	@ValueComputationTestCase(jvmFails = true, expectedValues = { "bob lee_bob%20lee" })
	public static String testAuthority2() {
		Uri u = Uri.parse("http://bob%20lee@localhost:42");
		return u.getUserInfo() + "_" + u.getEncodedUserInfo();
	}

	@ValueComputationTestCase(jvmFails = true, expectedValues = { "bob@lee:jr" })
	public static String testAuthority3() {
		Uri u = Uri.parse("http://bob%40lee%3ajr@local%68ost:4%32");
		return u.getUserInfo();
	}

	@ValueComputationTestCase(jvmFails = true, expectedValues = { "-1" })
	public static int testPort() {
		Uri u = Uri.parse("http://foo");
		return u.getPort();
	}

	@ValueComputationTestCase(jvmFails = true, expectedValues = { "false" })
	public static String testNotEquals() {
		return String.valueOf(Uri.EMPTY.equals(null));
	}

	@ValueComputationTestCase(jvmFails = true, expectedValues = { "true" })
	public static String testEquals() {
		return String.valueOf(a.equals(b));
	}

	@ValueComputationTestCase(jvmFails = true, expectedValues = { "true" })
	public static String testEquals2() {
		return String.valueOf(b.equals(c));
	}

	@ValueComputationTestCase(jvmFails = true, expectedValues = { "true" })
	public static String testEquals3() {
		return String.valueOf(a.equals(c));
	}

	@ValueComputationTestCase(jvmFails = true, expectedValues = { "0" })
	public static String testHC() {
		return String.valueOf(a.hashCode() - c.hashCode());
	}

	@ValueComputationTestCase(jvmFails = true, expectedValues = { "0" })
	public static String testHC2() {
		return String.valueOf(b.hashCode() - c.hashCode());
	}

	@ValueComputationTestCase(jvmFails = true, expectedValues = { "new" })
	public static String testNew() {
		Uri a = Uri.fromParts("foo", "bar", "tee");
		Uri b = a.buildUpon().fragment("new").build();
		return b.getFragment();
	}

	@ValueComputationTestCase(jvmFails = true, expectedValues = { "bar" })
	public static String testBar() {
		Uri a = Uri.fromParts("foo", "bar", "tee");
		Uri b = a.buildUpon().fragment("new").build();
		return b.getSchemeSpecificPart();
	}

	@ValueComputationTestCase(jvmFails = true, expectedValues = { "foo" })
	public static String testBasr() {
		Uri a = Uri.fromParts("foo", "bar", "tee");
		Uri b = a.buildUpon().fragment("new").build();
		return b.getScheme();
	}

	@ValueComputationTestCase(jvmFails = true, expectedValues = { "new" })
	public static String testBuildUponEncodedOpaqueUri1() {

		Uri a = new Uri.Builder().scheme("foo").encodedOpaquePart("bar").fragment("tee").build();
		Uri b = a.buildUpon().fragment("new").build();
		return b.getFragment();
	}

	@ValueComputationTestCase(jvmFails = true, expectedValues = { "bar" })
	public static String testBuildUponEncodedOpaqueUri2() {

		Uri a = new Uri.Builder().scheme("foo").encodedOpaquePart("bar").fragment("tee").build();
		Uri b = a.buildUpon().fragment("new").build();
		return b.getSchemeSpecificPart();
	}

	@ValueComputationTestCase(jvmFails = true, expectedValues = { "foo" })
	public static String testBuildUponEncodedOpaqueUri3() {

		Uri a = new Uri.Builder().scheme("foo").encodedOpaquePart("bar").fragment("tee").build();
		Uri b = a.buildUpon().fragment("new").build();
		return b.getScheme();
	}

	@ValueComputationTestCase(jvmFails = true, expectedValues = { "a a" })
	public static String testPathSegmentDecoding1() {
		Uri uri = Uri.parse("foo://bar/a%20a/b%20b");
		return uri.getPathSegments().get(0);
	}

	@ValueComputationTestCase(jvmFails = true, expectedValues = { "b b" })
	public static String testPathSegmentDecoding2() {
		Uri uri = Uri.parse("foo://bar/a%20a/b%20b");
		return uri.getPathSegments().get(1);
	}

	@ValueComputationTestCase(jvmFails = true, expectedValues = { "2" })
	public static String testPathSegmentNum() {
		Uri uri = Uri.parse("foo://bar/a%20a/b%20b/");
		return String.valueOf(uri.getPathSegments().size());
	}

	@ValueComputationTestCase(jvmFails = true, expectedValues = { "content://sms/conversations/addr=555-1212" })
	public static String testSMS1() {
		Uri base = Uri.parse("content://sms");
		Uri appended = base.buildUpon().appendEncodedPath("conversations/addr=555-1212").build();
		return appended.toString();
	}

	@ValueComputationTestCase(jvmFails = true, expectedValues = { "2" })
	public static String testSMS2() {
		Uri base = Uri.parse("content://sms");
		Uri appended = base.buildUpon().appendEncodedPath("conversations/addr=555-1212").build();
		return String.valueOf(appended.getPathSegments().size());
	}

	@ValueComputationTestCase(jvmFails = true, expectedValues = { "conversations" })
	public static String testSMS3() {
		Uri base = Uri.parse("content://sms");
		Uri appended = base.buildUpon().appendEncodedPath("conversations/addr=555-1212").build();
		return String.valueOf(appended.getPathSegments().get(0));
	}

	@ValueComputationTestCase(jvmFails = true, expectedValues = { "addr=555-1212" })
	public static String testSMS4() {
		Uri base = Uri.parse("content://sms");
		Uri appended = base.buildUpon().appendEncodedPath("conversations/addr=555-1212").build();
		return String.valueOf(appended.getPathSegments().get(1));
	}

	@ValueComputationTestCase(jvmFails = true, expectedValues = { "-1" })
	public static String testEncodeWithAllowedChars() {
		String encoded = Uri.encode("Bob:/", "/");
		return String.valueOf(encoded.indexOf(':'));
	}

	@ValueComputationTestCase(jvmFails = true, expectedValues = {
			"null\n\nBob\n:Bob\n::Bob\nBob::Lee\nBob:Lee\nBob::\nBob:\n::Bob::\n" })
	public static String testEncodeDecode() {
		String s = code(null);
		s += code("");
		s += code("Bob");
		s += code(":Bob");
		s += code("::Bob");
		s += code("Bob::Lee");
		s += code("Bob:Lee");
		s += code("Bob::");
		s += code("Bob:");
		s += code("::Bob::");
		return s;

	}

	private static String code(String s) {
		return Uri.decode(Uri.encode(s, null)) + "\n";
	}

	@ValueComputationTestCase(jvmFails = true, expectedValues = { "file:///tmp/bob" })
	public static String testFile() {
		File f = new File("/tmp/bob");

		Uri uri = Uri.fromFile(f);

		return uri.toString();

	}

	@ValueComputationTestCase(jvmFails = true, expectedValues = { "null" })
	public static String testQuery1() {
		Uri uri = Uri.parse("content://user");
		return String.valueOf(uri.getQueryParameter("a"));
	}

	@ValueComputationTestCase(jvmFails = true, expectedValues = { "b" })
	public static String testQuery2() {
		Uri uri = Uri.parse("content://user");
		uri = uri.buildUpon().appendQueryParameter("a", "b").build();
		return String.valueOf(uri.getQueryParameter("a"));
	}

	@ValueComputationTestCase(jvmFails = true, expectedValues = { "[b, b2]" })
	public static String testQuery3() {
		Uri uri = Uri.parse("content://user");
		uri = uri.buildUpon().appendQueryParameter("a", "b").build();
		uri = uri.buildUpon().appendQueryParameter("a", "b2").build();
		return String.valueOf(uri.getQueryParameters("a"));
	}

	@ValueComputationTestCase(jvmFails = true, expectedValues = { "[b, b2]" })
	public static String testQuery4() {
		Uri uri = Uri.parse("content://user");
		uri = uri.buildUpon().appendQueryParameter("a", "b").build();
		uri = uri.buildUpon().appendQueryParameter("a", "b2").build();
		uri = uri.buildUpon().appendQueryParameter("c", "d").build();
		return String.valueOf(uri.getQueryParameters("a"));
	}

	@ValueComputationTestCase(jvmFails = true, expectedValues = { "d" })
	public static String testQuery5() {
		Uri uri = Uri.parse("content://user");
		uri = uri.buildUpon().appendQueryParameter("a", "b").build();
		uri = uri.buildUpon().appendQueryParameter("a", "b2").build();
		uri = uri.buildUpon().appendQueryParameter("c", "d").build();
		return String.valueOf(uri.getQueryParameter("c"));
	}

	@ValueComputationTestCase(jvmFails = true, expectedValues = { "empty" })
	public static String testScheme1() {
		Uri uri = Uri.parse("empty:");
		return uri.getScheme();
	}

	@ValueComputationTestCase(jvmFails = true, expectedValues = { "true" })
	public static String testScheme2() {
		Uri uri = Uri.parse("empty:");
		return String.valueOf(uri.isAbsolute());
	}

	@ValueComputationTestCase(jvmFails = true, expectedValues = { "null" })
	public static String testScheme3() {
		Uri uri = Uri.parse("empty:");
		return String.valueOf(uri.getPath());
	}

	@ValueComputationTestCase(jvmFails = true, expectedValues = { "0" })
	public static String testEmptyPath() {
		Uri uri = Uri.parse("content://user");
		return String.valueOf(uri.getPathSegments().size());
	}

	@ValueComputationTestCase(jvmFails = true, expectedValues = { "2_b" })
	public static String testPathOperations1() {
		Uri uri = Uri.parse("content://user/a/b");

		return uri.getPathSegments().size() + "_" + uri.getLastPathSegment();
	}

	@ValueComputationTestCase(jvmFails = true, expectedValues = { "3_c_content://user/a/b/c" })
	public static String testPathOperations2() {
		Uri uri = Uri.parse("content://user/a/b");
		Uri first = uri;
		uri = uri.buildUpon().appendPath("c").build();

		return uri.getPathSegments().size() + "_" + uri.getLastPathSegment() + "_" + uri.toString();
	}

	@ValueComputationTestCase(jvmFails = true, expectedValues = { "4_100_100_content://user/a/b/c/100" })
	public static String testPathOperations3() {
		Uri uri = Uri.parse("content://user/a/b");
		uri = uri.buildUpon().appendPath("c").build();
		uri = ContentUris.withAppendedId(uri, 100);

		return uri.getPathSegments().size() + "_" + uri.getLastPathSegment() + "_" + ContentUris.parseId(uri) + "_"
				+ uri.toString();
	}

	@ValueComputationTestCase(jvmFails = true, expectedValues = { "/x/y/z" })
	public static String testPathOperations4() {
		return Uri.parse("xyz:/x/y/").buildUpon().appendPath("z").build().getPath();
	}

	@ValueComputationTestCase(jvmFails = true, expectedValues = { "http://foo.com/?a=1&b=2_http://foo.com/?a=1&b=2" })
	public String testQueryParam() {
		String nestedUrl = "http://foo.com/?a=1&b=2";
		Uri uri = Uri.parse("http://test/").buildUpon().appendQueryParameter("foo", "bar")
				.appendQueryParameter("nested", nestedUrl).build();
		return uri.getQueryParameter("nested") + "_" + uri.getQueryParameters("nested").get(0);
	}

}