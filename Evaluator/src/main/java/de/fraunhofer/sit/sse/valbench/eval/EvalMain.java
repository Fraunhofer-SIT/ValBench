package de.fraunhofer.sit.sse.valbench.eval;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;

import de.fraunhofer.sit.sse.valbench.ExplicitLoggingPoint;
import de.fraunhofer.sit.sse.valbench.eval.blueseal.BlueSealEvaluator;
import de.fraunhofer.sit.sse.valbench.eval.coal.COALEvaluator;
import de.fraunhofer.sit.sse.valbench.eval.harvester.HarvesterEvaluator;
import de.fraunhofer.sit.sse.valbench.eval.jsa.JSAEvaluator;
import de.fraunhofer.sit.sse.valbench.eval.stringhound.StringHoundEvaluator;
import de.fraunhofer.sit.sse.valbench.eval.utils.CounterMap;
import de.fraunhofer.sit.sse.valbench.eval.valdroid.ValDroidEvaluator;
import de.fraunhofer.sit.sse.valbench.eval.violist.ViolistEvaluator;
import de.fraunhofer.sit.sse.valbench.metadata.StatisticResultsAggregator;
import de.fraunhofer.sit.sse.valbench.metadata.TestCaseManager;
import de.fraunhofer.sit.sse.valbench.metadata.impl.requirements.AndroidRequirement;
import de.fraunhofer.sit.sse.valbench.metadata.impl.requirements.CachedRequirements;
import de.fraunhofer.sit.sse.valbench.metadata.impl.requirements.ModelledAPIMethodRequirement;
import de.fraunhofer.sit.sse.valbench.metadata.interfaces.IRequirement;
import de.fraunhofer.sit.sse.valbench.metadata.interfaces.IResult;
import de.fraunhofer.sit.sse.valbench.metadata.interfaces.ITestCase;
import de.fraunhofer.sit.sse.valbench.metadata.interfaces.ITestSuite;
import soot.jimple.infoflow.data.SootMethodAndClass;
import soot.jimple.infoflow.util.SootMethodRepresentationParser;

public class EvalMain {

	private static final String JSA_REQUIREMENTS_FILENAME = "JSA-Testcase-Requirements.json";
	private static final IEvaluator[] EVALUATORS = new IEvaluator[] {
			new HarvesterEvaluator(), new COALEvaluator() , new ValDroidEvaluator(), new ViolistEvaluator(), new JSAEvaluator(), new StringHoundEvaluator(),
			new BlueSealEvaluator()
	};
	private static final Comparator<IEvaluator> SORT_EVALUATOR_ALPHABETICALLY = new Comparator<IEvaluator>() {

		@Override
		public int compare(IEvaluator o1, IEvaluator o2) {
			return o1.getName().compareTo(o2.getName());
		}
		
	};

	public static File basedir = new File("");

	public static void main(String[] args) throws Exception {
		try {
			if (!new File(JSA_REQUIREMENTS_FILENAME).exists()) {
				basedir = new File("Evaluator");
				if (!new File(basedir, JSA_REQUIREMENTS_FILENAME).exists()) {
					System.err.println(String.format("Could not find %s", JSA_REQUIREMENTS_FILENAME));
					System.err.println("Wrong working directory?");
					System.exit(1);
				}
			}
			
			for (int i = 0; i < args.length; i++) {
				switch (args[i]) {
				case "-help":
				case "--help":
					System.out.println("--papereval <DIR>");
					System.out.println("Print the LaTeX tables from the paper");
					return;
				case "--papereval":
				case "-papereval":
					i++;
					PaperEvaluation.PRINT_PAPER_DIR = args[i];
					break;
				}
			}
			
			CounterMap<IRequirement> jsa = new CounterMap<>();
			for (CachedRequirements i : CachedRequirements.createGSON().fromJson(IOUtils.toString(new FileInputStream(new File(basedir, JSA_REQUIREMENTS_FILENAME))), CachedRequirements[].class))
				jsa.countAll(i.getAllRequirements());
			printTop10Requirements("Top10Req-JSA.tex", jsa, 328);
			Arrays.sort(EVALUATORS, SORT_EVALUATOR_ALPHABETICALLY);
			ExplicitLoggingPoint.OUTPUT = false;
			
			Inputs inputs = null;
			
			File res = new File(basedir, "Results");
			
			
			printTestCaseStats();
			
			//First make sure we got every result
			for (IEvaluator eval : EVALUATORS) {
				File resFile = new File(res, eval.getName());
				if (resFile.exists())
					eval.readIn(resFile);
				else {
					if (inputs == null)
						inputs = new Inputs();
					eval.run(resFile, inputs);
					if (!resFile.exists())
						throw new RuntimeException("Did not find " + resFile.getAbsolutePath());
					eval.readIn(resFile);
				}
			}
			
			runEvaluation("Android", TestCaseManager.getAllTestCases().stream().filter(x -> x.hasRequirement(AndroidRequirement.INSTANCE)).collect(Collectors.toList()),
					Arrays.asList(EVALUATORS).stream().filter(x -> x instanceof IAndroidEvaluator).collect(Collectors.toList()));
			runEvaluation("JVM", TestCaseManager.getAllTestCases().stream().filter(x -> !x.hasRequirement(AndroidRequirement.INSTANCE)).collect(Collectors.toList()), Arrays.asList(EVALUATORS));
			
			printTable();
		} finally {
			PaperEvaluation.close();
		}
	}

	private static void printTable() {
		Set<ITestSuite> crypto = new HashSet<>();
		Set<ITestSuite> android = new HashSet<>();
		Map<String, Set<ITestSuite>> tmAllTests = new TreeMap<>();
		for (ITestSuite i : TestCaseManager.getAllTestSuites()) {
			String name = i.getTestSuiteClass().getSimpleName();
			if (i.getTestSuiteClass().getName().startsWith("de.fraunhofer.sit.sse.valbench.crypto")) {
				//we want to combine the different crypto test cases into one, since
				//we do not have that much space
				crypto.add(i);
				tmAllTests.putIfAbsent("Crypto-JavaImpl", crypto);
				continue;
			}
			if (i.getTestCases().iterator().next().getRequirements().contains(AndroidRequirement.INSTANCE)) {
				android.add(i);
				tmAllTests.putIfAbsent("Android", android);
			} else {
				if (name.endsWith("Tests"))
					name = name.substring(0, name.length() - 5);
				tmAllTests.put(name, Collections.singleton(i));
			}
		}
		boolean first = true;
		try (PrintStream out = PaperEvaluation.getOutput("AllTests.tex")) {
			for (Entry<String, Set<ITestSuite>> entry : tmAllTests.entrySet()) {
				if (first) {
					first=false;
				} else
					out.print("\\\\");
				String name = entry.getKey();
				List<ITestCase> tc = new ArrayList<>();
				for (ITestSuite ts : entry.getValue()) {
					tc.addAll(ts.getTestCases());
				}
				CounterMap<IEvaluator> wildcardResults = new CounterMap<>();
				Map<IEvaluator, EvalResult> resultsPerEvaluator = getResultsPerEvaluator(tc, Arrays.asList(EVALUATORS), wildcardResults);
				String res = "";
				for (EvalResult d : resultsPerEvaluator.values()) {
					double prec = d.getStatistics().getPrecision();
					String p = PaperEvaluation.roundInPercent(prec);
					if (Double.isNaN(prec))
						p = "-";
					res += " & " + p + "/" + PaperEvaluation.roundInPercent(d.getStatistics().getRecall());
				}
				out.println(name + "& " + tc.size()  + res );
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private static void runEvaluation(String name, List<ITestCase> list, List<IEvaluator> evaluators) throws IOException, FileNotFoundException {
		CounterMap<IEvaluator> wildcardResults = new CounterMap<>();
		Map<IEvaluator, EvalResult> resultsPerEvaluator = getResultsPerEvaluator(list, evaluators, wildcardResults);
		
		for (Entry<IEvaluator, Integer> i : wildcardResults) {
			PaperEvaluation.printPaperResult(name + "Wildcard" + i.getKey().getName(), i.getValue());
		}
		

		try (PrintStream ps = PaperEvaluation.getOutput(name + "-PrecisionRecall.tex")) {
			boolean first = true;
			for (Entry<IEvaluator, EvalResult> d : resultsPerEvaluator.entrySet()) {
				if (first)
					first = false;
				else
					ps.println("\\\\");
				String approachName = d.getKey().getName();
				StatisticResultsAggregator v = d.getValue().getStatistics();
				String prec = PaperEvaluation.roundInPercent(v.getPrecision());
				String recall = PaperEvaluation.roundInPercent(v.getRecall());
				ps.print(approachName + " & " + prec + " & " + recall);
				
				System.out.println(name + ": " + approachName + ": Precision: " + prec + ", Recall: " + recall);
			}
		}
	}

	private static Map<IEvaluator, EvalResult> getResultsPerEvaluator(Collection<ITestCase> list, List<IEvaluator> evaluators,
			CounterMap<IEvaluator> wildcardResults) {
		Map<IEvaluator, EvalResult> resultsPerEvaluator = new LinkedHashMap<>();
		for (IEvaluator eval : evaluators) {
			List<IResult> results = new ArrayList<>();
			for (ITestCase t : list) {
				Object actualRes = eval.evaluate(t);
				if (actualRes instanceof Iterable) {
					Iterator it = ((Iterable) actualRes).iterator();
					while (it.hasNext()) {
						String r = it.next().toString();
						if (isOnlyWildCard(r)) {
							wildcardResults.count(eval);
							it.remove(); //remove result without any information
							break;
						}
					}
				}
				IResult result = t.getResultVerifier().verifyResult(actualRes);
				results.add(result);
			}
			resultsPerEvaluator.put(eval, new EvalResult(eval, results));
		}
		return resultsPerEvaluator;
	}

	private static boolean isOnlyWildCard(String r) {
		boolean onlyWildCard = false;
		//We also get
		//(.*)(.*)(.*)(.*)(.*)(.*)(.*)
		//which is pointless
		while (!r.isEmpty()) {
			if (r.startsWith(".*"))
			{
				onlyWildCard = true;
				r = r.substring(2);
				continue;
			}
			if ( r.startsWith("(.*)"))
			{
				onlyWildCard = true;
				r = r.substring(4);
				continue;
			}
			return false;
		}
		return onlyWildCard;
	}

	private static void printTestCaseStats() throws IOException {
		printTop10Requirements();
		int android = getNumberOfAndroidTestcases();
		PaperEvaluation.printPaperResult("numberOfAndroidTestcases", android);
		PaperEvaluation.printPaperResult("numberOfJVMTestcases", TestCaseManager.getAllTestCases().size() - android);
		PaperEvaluation.printPaperResult("numberOfTestcases", TestCaseManager.getAllTestCases().size());
	}

	private static void printTop10Requirements() throws FileNotFoundException {
		CounterMap<IRequirement> requirements = new CounterMap<>();
		for (ITestCase d : TestCaseManager.getAllTestCases()) {
			for (IRequirement req : d.getRequirements())
				requirements.count(req);
		}
		printTop10Requirements("Top10Req.tex", requirements, TestCaseManager.getAllTestCases().size());
		
	}

	private static void printTop10Requirements(String file, CounterMap<IRequirement> requirements, int totalTestCases) throws FileNotFoundException {
		try (PrintStream ps = PaperEvaluation.getOutput(file)) {
			int x = 0;
			for (Entry<IRequirement, Integer> d : requirements.sortedValues()) {
				String reqName = d.getKey().toString();
				boolean isCode = false;
				if (d.getKey() instanceof ModelledAPIMethodRequirement) {
					ModelledAPIMethodRequirement r = (ModelledAPIMethodRequirement) d.getKey();
					SootMethodAndClass mc = SootMethodRepresentationParser.v().parseSootMethodString(r.getMethodSignature());
					reqName = PaperEvaluation.getShortenedMethodName(mc);
					isCode = true;
				}
				String r = PaperEvaluation.escapeLaTeX(reqName);
				if (isCode)
					r = "\\lstinline|" + r + "|";
				String relative = PaperEvaluation.roundInPercent((double)d.getValue() / totalTestCases);
				if (x >= 10)
					ps.print("% ");
				ps.println(r + " & " + d.getValue() + " (" + relative + " \\%)" + (x != 9 ? "\\\\" : ""));
				x++;
			}
		}
	}

	private static int getNumberOfAndroidTestcases() {
		return (int) TestCaseManager.getAllTestCases().stream().filter(x -> x.hasRequirement(AndroidRequirement.INSTANCE)).count();
	}

}
