package de.fraunhofer.sit.sse.valbench.modellingtests;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.CharArrayReader;
import java.io.CharArrayWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.Buffer;
import java.nio.CharBuffer;
import java.util.Arrays;
import java.util.Locale;
import java.util.Random;

import de.fraunhofer.sit.sse.valbench.metadata.ValueComputationTestCase;

public class StreamTests {

	class a {
		PrintWriter w;
	}

	@ValueComputationTestCase
	public static String testCombinedField() throws IOException {
		a a = new StreamTests().new a();
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		a.w = new PrintWriter(os);

		os.write("Test".getBytes());
		DataOutputStream dos = new DataOutputStream(os);
		dos.write(new byte[] { 6, 6 });
		dos.close();

		PrintWriter pw = new PrintWriter(a.w);
		pw.append("xy");
		pw.close();

		return new String(os.toByteArray());
	}

	@ValueComputationTestCase
	public static String testCombined() throws IOException {

		ByteArrayOutputStream os = new ByteArrayOutputStream();
		os.write("Test".getBytes());
		DataOutputStream dos = new DataOutputStream(os);
		dos.write(new byte[] { 6, 6 });
		dos.close();

		PrintWriter pw = new PrintWriter(os);
		pw.append("xy");
		pw.close();

		OutputStreamWriter osw = new OutputStreamWriter(os);
		osw.append("xyz");
		osw.close();
		return new String(os.toByteArray());
	}

	@ValueComputationTestCase
	public static String testOutputStream() throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Random rand = new Random(2);
		out.write(2);
		byte[] b = new byte[20];
		rand.nextBytes(b);
		out.write(b);
		rand.nextBytes(b);
		out.write(b, 1, 4);
		out.close();
		return out.toString() + "_" + Arrays.toString(out.toByteArray());
	}

	@ValueComputationTestCase
	public static String testBufferedOutputStreamSimple() throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Random rand = new Random(2);
		out.write(2);
		BufferedOutputStream out2 = new BufferedOutputStream(out);
		byte[] b = new byte[20];
		rand.nextBytes(b);
		out2.write(b);
		rand.nextBytes(b);
		out2.write(b, 1, 4);
		out2.close();
		return out.toString() + "_" + Arrays.toString(out.toByteArray());
	}

	@ValueComputationTestCase
	public static String testBufferedOutputStream() throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Random rand = new Random(2);
		out.write(2);
		BufferedOutputStream out2 = new BufferedOutputStream(out);
		byte[] b = new byte[20];
		rand.nextBytes(b);
		out2.write(b);
		rand.nextBytes(b);
		out2.write(b, 1, 4);
		out2.close();
		return out.toString() + "_" + Arrays.toString(out.toByteArray());
	}

	@ValueComputationTestCase
	public static String testInputStream() throws IOException {
		byte[] b = new byte[1024];
		Random rand = new Random(2);
		rand.nextBytes(b);
		ByteArrayInputStream in2 = new ByteArrayInputStream(b, 1, 1000);
		BufferedInputStream in = new BufferedInputStream(in2);

		String s = String.valueOf(in.read());
		byte[] b2 = new byte[256];
		s += "_" + in.read(b2, 4, 20);
		byte[] b3 = new byte[256];
		s += "_" + in.read(b3) + Arrays.toString(b2) + Arrays.toString(b);
		return s;
	}

	@ValueComputationTestCase
	public static String testInputStream2() throws IOException {
		byte[] b = new byte[1024];
		Random rand = new Random(2);
		rand.nextBytes(b);
		ByteArrayInputStream in = new ByteArrayInputStream(b, 1, 1000);
		in.read();
		byte[] b2 = new byte[256];
		in.read(b2, 4, 20);
		byte[] b3 = new byte[256];
		in.read(b3);
		String s = Arrays.toString(b2);
		return s;
	}

	@ValueComputationTestCase(expectedValues = { "aa1", "aa2", "ba1", "ba2" })
	public static String testInputStream3() throws IOException {
		String s = "";
		if (new Random().nextBoolean()) {
			s += "a";
		} else {
			s += "b";
		}
		ByteArrayInputStream in = new ByteArrayInputStream("a".getBytes("UTF-8"));
		if (new Random().nextBoolean()) {
			return s + (char) in.read() + "1";
		} else {
			return s + (char) in.read() + "2";
		}
	}

	@ValueComputationTestCase(expectedValues = { "aa1", "aa2", "ba1", "ba2" })
	public static String testInputStream4() throws IOException {
		String s = "";
		if (new Random().nextBoolean()) {
			s += "a";
		} else {
			s += "b";
		}
		ByteArrayInputStream in = new ByteArrayInputStream("a".getBytes("UTF-8"));
		if (new Random().nextBoolean()) {
			return s + (char) in.read() + "1";
		} else {
			return s + (char) in.read() + "2";
		}
	}

	@ValueComputationTestCase
	public static String testInputStream5() throws IOException {
		Random rand = new Random(4);
		byte[] b = new byte[1000];
		rand.nextBytes(b);
		ByteArrayInputStream in = new ByteArrayInputStream(b);
		InputStreamReader reader = new InputStreamReader(in);
		String s = "";
		s += reader.read(); // 65533
		char[] c = new char[10];
		s += reader.read(); // 6553356
		s += Arrays.toString(c);
		return s;
	}

	@ValueComputationTestCase
	public static String testInputStream6() throws IOException {
		byte[] b = new byte[1000];
		Random rand = new Random(4);
		rand.nextBytes(b);
		ByteArrayInputStream in = new ByteArrayInputStream(b);
		InputStreamReader reader = new InputStreamReader(in);
		CharBuffer cb = CharBuffer.allocate(10);
		int read = reader.read(cb);
		((Buffer)cb).rewind();
		char[] o = new char[10];
		cb.get(o, 0, read);
		return Arrays.toString(o);
	}

	@ValueComputationTestCase
	public static String testOutputStream2() throws IOException {
		Random rand = new Random(4);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] b = new byte[500];
		rand.nextBytes(b);
		out.write(20);
		out.write(b, 1, 400);
		return Arrays.toString(out.toByteArray()) + "_" + out.toString() + "_" + out.toString(10);
	}

	@ValueComputationTestCase
	public static String testOutputStream3() throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		OutputStreamWriter w = new OutputStreamWriter(out);
		w.write(20);
		w.write("Pasdsadasf".toCharArray(), 1, 4);
		w.append('x');
		w.append("Test");
		w.write(new char[] { 'a', 'a' }, 1, 1);
		w.write("Test");

		// Bei append: Letzter Parameter ist end und nicht die Länge!
		w.append("Test", 1, 2);
		w.flush();
		return new String(out.toByteArray(), "UTF-8");
	}

	@ValueComputationTestCase
	public static String testOutputStream4() throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		OutputStreamWriter w2 = new OutputStreamWriter(out);
		BufferedWriter w = new BufferedWriter(w2, 1024);
		w.write(20);
		w.write("Pasdsadasf".toCharArray(), 1, 4);
		w.append('x');
		w.append("Test");
		w.write(new char[] { 'a', 'a' }, 1, 1);
		w.write("Test");

		// Bei append: Letzter Parameter ist end und nicht die Länge!
		w.append("Test", 1, 2);
		w.flush();
		return new String(out.toByteArray(), "UTF-8");
	}

	@ValueComputationTestCase
	public static String testStringWriter() throws IOException {
		StringWriter ww = new StringWriter();

		ww.append("Test");
		ww.append('x');
		ww.append("Testa", 1, 2);
		ww.getBuffer().append(true);
		return ww.getBuffer().toString() + ww.toString() + ww.append("xx");
	}

	@ValueComputationTestCase
	public static String testStringWriter2() throws IOException {
		StringWriter ww = new StringWriter();

		ww = ww.append("Test");
		ww.append('x');
		ww.append("Testa", 1, 2);
		return ww.getBuffer().toString() + ww.toString() + ww.append("xx");
	}

	@ValueComputationTestCase
	public static String testStringWriter3() throws IOException {
		CharArrayWriter w = new CharArrayWriter(10);
		w.append("Test", 1, 2);
		w.append((char) 4);
		w.flush();
		w.close();
		return w.toString() + "_" + new String(w.toCharArray());
	}

	@ValueComputationTestCase
	public static String testStringReader() throws IOException {
		StringReader str = new StringReader("Testabc");
		String s = "" + str.read();
		str.skip(1);
		char[] r = new char[12];
		s += str.read(r, 1, 2);
		return s;
	}

	@ValueComputationTestCase
	public static String testBufferedReader() throws IOException {
		StringReader str = new StringReader("Test\nLine2\nabc");
		// int c = str.read();
		BufferedReader r = new BufferedReader(str);
		char[] cbuf = new char[2];
		r.read(cbuf);
		return r.readLine() + "_" + r.readLine() + "_" + r.read() + "_" + Arrays.toString(cbuf);// + "_" + c;
	}

	@ValueComputationTestCase
	public static String testCharArrayReader() throws IOException {
		CharArrayReader str = new CharArrayReader("Test\nLine2\nabc".toCharArray());
		String s = "" + str.read();
		char[] cbuf = new char[4];
		str.read(cbuf);
		s += Arrays.toString(cbuf);
		str.read(cbuf, 1, 1);
		s += Arrays.toString(cbuf);
		return s;
	}

	@ValueComputationTestCase
	public static String testPrintWriter() throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		java.io.PrintWriter p = new java.io.PrintWriter(os);
		test(p);
		String s = Arrays.toString(os.toByteArray());
		return s;
	}

	private static void test(java.io.PrintWriter p) {
		p.append("Test");
		p.println();
		p.println("Foo");
		p.println("Foo".toCharArray());
		p.format("%s%d", "Test", 2);
		p.format(Locale.CANADA, "%f", 3.24f);
		p.printf("%s%d", "Test", 2);
		p.printf(Locale.CANADA, "%f", 3.24f);
		p.flush();
		p.append("Blablbal", 1, 2);
		p.close();
	}

	@ValueComputationTestCase
	public static String testPrintWriter2() throws IOException {
		StringWriter w = new StringWriter();
		java.io.PrintWriter p = new java.io.PrintWriter(w);
		test(p);
		return w.toString();
	}

	@ValueComputationTestCase
	public static String testPrintWriter3() throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		OutputStreamWriter w = new OutputStreamWriter(os);
		java.io.PrintWriter p = new java.io.PrintWriter(w);
		test(p);
		return Arrays.toString(os.toByteArray());
	}

	@ValueComputationTestCase
	public static String testPrintStream() throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		PrintStream p = new PrintStream(os, true, "UTF-8");
		p.append("Test");
		p.println();
		p.println("Foo");
		p.println("Foo".toCharArray());
		p.format("%s%d", "Test", 2);
		p.format(Locale.CANADA, "%f", 3.24f);
		p.printf("%s%d", "Test", 2);
		p.printf(Locale.CANADA, "%f", 3.24f);
		p.flush();
		p.append("Blablbal", 1, 2);
		p.close();
		return Arrays.toString(os.toByteArray());
	}

	@ValueComputationTestCase
	public static String testPrintStream2() throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		PrintStream p = new PrintStream(os, true, "UTF-8");
		p.append("Blablbal", 1, 2);
		p.close();
		return Arrays.toString(os.toByteArray());
	}

}
