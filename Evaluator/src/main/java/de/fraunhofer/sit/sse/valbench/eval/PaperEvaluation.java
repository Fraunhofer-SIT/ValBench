package de.fraunhofer.sit.sse.valbench.eval;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import org.apache.commons.io.output.NullPrintStream;

import soot.jimple.infoflow.data.SootMethodAndClass;

public class PaperEvaluation {
	public static String PRINT_PAPER_DIR = System.getenv("PRINT_PAPER_DIR");
	private static PrintStream ps;
	private static final DecimalFormat df = new DecimalFormat("0.00", DecimalFormatSymbols.getInstance(Locale.US));

	public static void printPaperResult(String var, Object value) throws IOException {
		getNumbersOutput().println("\\newcommand{\\" + var + "}{" + value + "\\xspace}");
	}
	
	public static void close() {
		try {
			getNumbersOutput().close();
		} catch (IOException e) {
		}
	}
	
	private static PrintStream getNumbersOutput() throws IOException {
		if (ps != null)
			return ps;
		PrintStream ps = getOutput("numbers.tex");
		return PaperEvaluation.ps = ps;
	}

	public static PrintStream getOutput(String filename) throws FileNotFoundException {
		if (PRINT_PAPER_DIR == null)
			return NullPrintStream.NULL_PRINT_STREAM;
		
		File ppdir = new File(PRINT_PAPER_DIR);
		ppdir.mkdirs();
		
		File f = new File(ppdir, filename);
		return new PrintStream(f);
	}

	public static String escapeLaTeX(String s) {
		s = s.replace("\\", "\\textbnackslash ");
		for (String r : new String[] { "_", "^", "~", "$", "%", "#", "&", "{", "}" }) {
			s = s.replace(r, "\\" + r);
		}
		return s;
	}

	public static String getShortenedMethodName(SootMethodAndClass mc) {
		String s = getShortenedClassName(mc.getClassName());
		return s + "." + mc.getMethodName();
	}

	private static String getShortenedClassName(String className) {
		int idx = className.lastIndexOf('.');
		if (idx == -1)
			return className;
		return className.substring(idx + 1);
	}
	 
	public static String round(double p) {
		return df.format(p);
	}

	public static String roundInPercent(double p) {
		if (Double.isNaN(p))
			return "-";
		double r = p * 100D;
		if (r == (double)(int)r)
			return String.valueOf((int)r);
		return round(r);
	}	
	
	
}
