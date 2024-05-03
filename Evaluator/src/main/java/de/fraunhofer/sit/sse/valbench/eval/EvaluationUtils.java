package de.fraunhofer.sit.sse.valbench.eval;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.ProcessBuilder.Redirect;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class EvaluationUtils {
	public static class Result {
		public String method;
		public String value;

		public Result() {
			
		}
		
		public Result(String method, String value) {
			this.method = method;
			this.value = value;
		}
	}
	
	public static void run(String... args) throws Exception {
		runWithWorkingDir(null, args);
	}

	public static void runWithWorkingDir(File workingDir, String... args) throws Exception {
		ProcessBuilder pb = new ProcessBuilder(args);
		if (workingDir != null)
			pb.directory(workingDir);
		pb.redirectError(Redirect.INHERIT);
		pb.redirectOutput(Redirect.INHERIT);
		Process pc = pb.start();
		pc.waitFor();
		if (pc.exitValue() != 0)
			throw new RuntimeException("Could not run");
	}

	public static List<Result> readStandardFormat(File f) throws FileNotFoundException, IOException {
		try (FileReader fr = new FileReader(f)) {
			return new ArrayList<Result>(Arrays.asList(new Gson().fromJson(fr, EvaluationUtils.Result[].class)));
		}
	}

	public static String getEnvironmentVariable(String env) {
		String s = System.getenv(env);
		if (s == null)
			throw new RuntimeException(String.format("Environment variable %s not set", env));
		return s;
	}

	public static File checkConfigurablePath(String env, String binary) {
		env = getEnvironmentVariable(env);
		File f = new File(env, binary);
		if (!f.exists())
			throw new RuntimeException(String.format("Not found: %s", f.getAbsolutePath()));
		return f;
	}

	public static String getAndroidSDKPath() {
		String s = getEnvironmentVariable("ANDROID_SDK");
		File f = new File(s);
		if (!f.exists() || !f.isDirectory())
			throw new RuntimeException(String.format("Not found: %s", f.getAbsolutePath()));
		File p = new File(f, "platforms");
		if (!p.exists() || !p.isDirectory())
			throw new RuntimeException(String.format("Not found: %s", p.getAbsolutePath()));

		for (File d : p.listFiles()) {
			if (d.getName().startsWith("android-"))
			{
				if (new File(d, "android.jar").exists())
					return s;
			}
		}
		throw new RuntimeException(String.format("No platform jar found: %s", p.getAbsolutePath()));
	}

	public static File createTempDirectory() throws IOException {
		File f = File.createTempFile("tmp", "dir");
		f.delete();
		f.mkdirs();
		return f;
	}

	public static void splitRegEx(List<Result> r) {
		for (Result d : new ArrayList<>(r)) {
			if (d.value.contains("|"))
			{
				String[] spl = d.value.split("\\|");
				r.remove(d);
				for (String s : spl) {
					Result res = new Result(d.method, s);
					r.add(res);
				}
			}
		}
	}

	public static void combineAll(File folder, File res) throws IOException {
		List<Result> allres = new ArrayList<>();
		for (File f : folder.listFiles()) {
			List<Result> r = readStandardFormat(f);
			for (Result d : r) {
				if (d.method.startsWith("<valbench.base"))
					continue;
				allres.add(d);
			}
		}
		
		String s = new GsonBuilder().setPrettyPrinting().create().toJson(allres);
		try (PrintWriter pw = new PrintWriter(res)) {
			pw.print(s);
		}
		
	}
}
