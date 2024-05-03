package de.fraunhofer.sit.sse.valbench;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.google.gson.Gson;

import de.fraunhofer.sit.sse.valbench.SootUtils;
import de.fraunhofer.sit.sse.valbench.metadata.Requires;
import de.fraunhofer.sit.sse.valbench.metadata.TestCaseManager;
import de.fraunhofer.sit.sse.valbench.metadata.TestEntrypoint;
import de.fraunhofer.sit.sse.valbench.metadata.ValueComputationTestCase;
import de.fraunhofer.sit.sse.valbench.metadata.impl.requirements.ArithmeticRequirement;
import de.fraunhofer.sit.sse.valbench.metadata.impl.requirements.ArrayRequirement;
import de.fraunhofer.sit.sse.valbench.metadata.impl.requirements.CachedRequirements;
import de.fraunhofer.sit.sse.valbench.metadata.impl.requirements.DynamicInvokeRequirement;
import de.fraunhofer.sit.sse.valbench.metadata.impl.requirements.FieldHandlingRequirement;
import de.fraunhofer.sit.sse.valbench.metadata.impl.requirements.IfRequirement;
import de.fraunhofer.sit.sse.valbench.metadata.impl.requirements.InterproceduralRequirement;
import de.fraunhofer.sit.sse.valbench.metadata.impl.requirements.LoopRequirement;
import de.fraunhofer.sit.sse.valbench.metadata.impl.requirements.ModelledAPIFieldRequirement;
import de.fraunhofer.sit.sse.valbench.metadata.impl.requirements.ModelledAPIMethodRequirement;
import de.fraunhofer.sit.sse.valbench.metadata.impl.requirements.PrimitiveTypeRequirement;
import de.fraunhofer.sit.sse.valbench.metadata.impl.requirements.RecursionRequirement;
import de.fraunhofer.sit.sse.valbench.metadata.impl.requirements.ReflectionRequirement;
import de.fraunhofer.sit.sse.valbench.metadata.interfaces.IRequirement;
import de.fraunhofer.sit.sse.valbench.metadata.interfaces.ITestCase;
import de.fraunhofer.sit.sse.valbench.metadata.interfaces.ITestSuite;
import soot.ArrayType;
import soot.PackManager;
import soot.PhaseOptions;
import soot.PrimType;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Value;
import soot.VoidType;
import soot.jimple.AssignStmt;
import soot.jimple.BinopExpr;
import soot.jimple.ConditionExpr;
import soot.jimple.DynamicInvokeExpr;
import soot.jimple.FieldRef;
import soot.jimple.IfStmt;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.SpecialInvokeExpr;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.toolkits.annotation.logic.Loop;
import soot.jimple.toolkits.annotation.logic.LoopFinder;
import soot.options.Options;

public class CreateJavaEntryPoint {
	
	@Test
	public void createJavaEntryPoint() throws FileNotFoundException {
		Options.v().set_allow_phantom_refs(true);
		Options.v().set_prepend_classpath(true);
		Options.v().set_process_dir(Arrays.asList("target/classes"));
		Options.v().set_output_format(Options.output_format_class);
		Options.v().set_output_dir("target/classes");
        PhaseOptions.v().setPhaseOption("jb", "model-lambdametafactory:false");
		
		for (ITestSuite t : TestCaseManager.getAllTestSuites()) {
			Scene.v().addBasicClass(t.getTestSuiteClass().getName(),SootClass.BODIES);
		}
		Scene.v().addBasicClass("de.fraunhofer.sit.sse.valbench.ExplicitLoggingPoint", SootClass.BODIES);
		Scene.v().loadNecessaryClasses();
		boolean changed = true;
		while (changed) {
			changed = false;
			for (SootClass c : new ArrayList<>( Scene.v().getLibraryClasses())) {
				if (c.resolvingLevel() < SootClass.SIGNATURES) {
					changed = true;
					Scene.v().forceResolve(c.getName(), SootClass.SIGNATURES);
				}
			}
		}
		
		SootMethod mEntryPoint = Scene.v().getMethod("<de.fraunhofer.sit.sse.valbench.MainClass: void main(java.lang.String[])>");
		EntryPointCreator javaCreator = new EntryPointCreator(mEntryPoint);

		Set<String> testcaseclasses = new HashSet<>();
		Set<SootClass> doNotModify = new HashSet<>();
		for (Class<?> t : TestCaseManager.ALL_CLASSES) {
			testcaseclasses.add(t.getName());

			if (t.getDeclaredAnnotation(Requires.class) != null) {
				Requires req = t.getDeclaredAnnotation(Requires.class);
				if (req.doNotModify()) {
					doNotModify.add(Scene.v().getSootClass(t.getName()));
				}
			}
			for (Method m : t.getDeclaredMethods()) {
				boolean isEntryPoint = false;
				ValueComputationTestCase d = m.getDeclaredAnnotation(ValueComputationTestCase.class);
				if (d != null)
				{
					if (d.noEntryPoint()) {
						continue;
					}
					isEntryPoint= true;
				}
				if (m.getDeclaredAnnotation(TestEntrypoint.class) != null) {
					isEntryPoint= true;
				}
				
				if (isEntryPoint) {
					if (m.getParameterCount() > 0)
						throw new RuntimeException("Did expect no parameter: " + m);
					String sootMethod = SootUtils.getSootSignature(m);
					boolean canRunOnJVM = true;
					if (d != null)
						canRunOnJVM = !d.jvmFails();
					javaCreator.addEntryPoint(sootMethod, canRunOnJVM);
					SootMethod sm = Scene.v().grabMethod(sootMethod);
					ExplicitLoggingPointCreator.createExplicitLP(sm);
				}
			}
			
		}
		javaCreator.finish();
		for (SootClass cl : new ArrayList<>( Scene.v().getApplicationClasses())) {
			if (doNotModify.contains(cl)) {
				cl.setLibraryClass();
				continue;
			}
			if (cl != mEntryPoint.getDeclaringClass() && !testcaseclasses.contains(cl.getName()))
				cl.setLibraryClass(); //we do not want to writeout the other classes
			else
				for (SootMethod method : cl.getMethods())
					if (method.isConcrete())
						method.retrieveActiveBody();
		}
		for (SootMethod m : mEntryPoint.getDeclaringClass().getMethods()) {
			m.retrieveActiveBody();
		}
		
		PackManager.v().writeOutput();
	}
}
