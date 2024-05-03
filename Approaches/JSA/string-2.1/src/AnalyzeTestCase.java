import dk.brics.automaton.Automaton;
import dk.brics.string.Misc;
import dk.brics.string.StringAnalysis;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import soot.PhaseOptions;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Unit;
import soot.ValueBox;
import soot.jimple.ReturnStmt;
import soot.options.Options;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AnalyzeTestCase {
    private static Logger log = Logger.getLogger(AnalyzeTestCase.class);

    public static void main(String[] args) throws FileNotFoundException {
        Logger.getRootLogger().setLevel(Level.INFO);

        String program_name = null;

        long time0 = System.currentTimeMillis();
        Options.v().set_process_dir(Arrays.asList(args[1]));
        Options.v().set_soot_classpath(args[0]);
        Options.v().set_allow_phantom_refs(true);
        Scene.v().loadNecessaryClasses();

        //This would modify the hierarchy during runtime; JSA does not support it!
        PhaseOptions.v().setPhaseOption("jb", "model-lambdametafactory:false");

        System.out.println("Loading classes...");
        for (SootClass d : new ArrayList<>(Scene.v().getApplicationClasses())) {
            StringAnalysis.loadClass(d.getName());
        	
        }
        boolean changed = true;
        while (changed) {
        	changed = false;
	        for (SootClass i : new ArrayList<>(Scene.v().getClasses())) {
	        	if (i.isPhantom())
	        		continue;
	        	if (i.getName().startsWith("valbench.metadata")) {
	        		Scene.v().removeClass(i);
	        	}
	        	if (i.resolvingLevel() < SootClass.SIGNATURES) {
		        	Scene.v().forceResolve(i.getName(), SootClass.SIGNATURES);
		        	changed = true;
	        	}
	        }
        }

        long time1 = System.currentTimeMillis();

        System.out.println("Loading time: " + time(time1 - time0));

        Set<ValueBox> hotspots = new HashSet<ValueBox>();
        Map<ValueBox, String> hotspotToMethod = new HashMap<>();
        hotspots.addAll(StringAnalysis.getArgumentExpressions(hotspotToMethod, "<valbench.ExplicitLoggingPoint: void explicitLoggingPoint(java.lang.Object)>", 0));
//        SootMethod m = Scene.v().grabMethod("<valbench.basictests.BasicStringTests: java.lang.String test()>");
        
        int places = hotspots.size();
        System.out.println("Number of calls: " + places);

        System.out.println("Analyzing...");
        StringAnalysis sa = new StringAnalysis(hotspots);

        long time2 = System.currentTimeMillis();

        System.out.println("Analysis time: " + time(time2 - time1));
        List<Result> results = new ArrayList<>();

        for (ValueBox e : hotspots) {
            String sf = sa.getSourceFile(e);
            int line = sa.getLineNumber(e);
            System.out.println("Checking call at line " + line + " in " + sf + "...");
            Automaton a = sa.getAutomaton(e);
            if (a.isFinite()) {
                System.out.println("A finite number of strings:");
                for (String s : a.getFiniteStrings()) {
                    Result res = new Result();
                    res.method = hotspotToMethod.get(e);
                    res.value = s;
                    results.add(res);
                    System.out.println("\"" + Misc.escape(s) + "\"");
                }
            } else if (a.complement().isEmpty()) {
                System.out.println("All possible strings.");
            } else {
                System.out.println("Infinitely many strings with common prefix:");
                System.out.println(a.getCommonPrefix());
                Result res = new Result();
                res.method = hotspotToMethod.get(e);
                res.value = a.getCommonPrefix() + "(.*)";
                results.add(res);
            }

        }

        long time3 = System.currentTimeMillis();

        System.out.println("Extraction time: " + time(time3 - time2));
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (PrintWriter pw = new PrintWriter(new java.io.File(args[2]))) {
        	pw.print(gson.toJson(results));
        }
        int num_exps = sa.getNumExps();
        int num_hs = hotspots.size();
        String atime = time(time2 - time1);
        String ttime = time(time3 - time1);

        System.out.println("\\texttt{" + program_name + "} & lll & " + num_exps + " & " + num_hs + " & " +
                atime + " & " + ttime + " & mmm\\\\\\hline");
    }

    
    public static class Result {
  	  public String method;
  	  public String value;
    }

    private static String time(long t) {
        return t / 1000 + "." + String.valueOf(1000 + (t % 1000)).substring(1);
    }
}
