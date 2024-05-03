package de.fraunhofer.sit.sse.valbench.modellingtests;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.fraunhofer.sit.sse.valbench.metadata.ValueComputationTestCase;

public class FileTests {

	static List<File> files = new ArrayList<>();
	static {
		for (File f : new File[] { new File("/"), new File("/a/b"), new File("/a/b/"), new File("/a/b/../"),
				new File(new File("a"), "b"), new File("/a/b/../"), new File("../"), new File("~/Foo"),
				new File(new File("a/r/.."), "b"), new File(new File("a\\b"), "c"),
				new File("a\\b/..\\g"), new File("a\\b\\../g") }) {
			files.add(f);
		}
	}

	@ValueComputationTestCase
	public static String testGetPath() throws IOException {
		String res = "";
		for (File s : files)
			res += s.getPath() + "\n";
		return res;
	}

	@ValueComputationTestCase
	public static String testIsAbsolute() throws IOException {
		String res = "";
		for (File s : files)
			res += s.getName() + ": " + s.isAbsolute() + "\n";
		return res;
	}

	@ValueComputationTestCase
	public static String testParent() throws IOException {
		String res = "";
		for (File s : files)
			res += s.getName() + ": " + s.getParent() + "\n";
		return res;
	}

	@ValueComputationTestCase
	public static String testParent2() throws IOException {
		String res = "";
		for (File s : files) {
			File x = s.getParentFile();
			if (x != null)
				res += s.getName() + ": " + x.getName() + "\n";
		}
		return res;
	}



	@ValueComputationTestCase
	public static String testPathsFile() throws IOException {
		String res = "";
		for (File s : files) {
			List<String> r = new ArrayList<>();
			File x = s;
			while (x != null) {
				r.add(x.getName());
				x = x.getParentFile();
			}
			Collections.reverse(r);
			String first = r.remove(0);
			File f = Paths.get("/" + first, r.toArray(new String[0])).toFile();
			res += s.getName() + ": " + f + "; " + f.equals(s) + "\n";
		}
		return res;
	}


}
