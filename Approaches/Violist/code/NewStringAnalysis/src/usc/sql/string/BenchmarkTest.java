package usc.sql.string;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.gson.GsonBuilder;

import SootEvironment.JavaApp;
import edu.usc.sql.graphs.Node;
import edu.usc.sql.graphs.NodeInterface;
import edu.usc.sql.graphs.cfg.CFGInterface;
import soot.Unit;
import soot.ValueBox;
import usc.sql.ir.ConstantInt;
import usc.sql.ir.ConstantString;
import usc.sql.ir.Expression;
import usc.sql.ir.ExternalPara;
import usc.sql.ir.InternalVar;
import usc.sql.ir.T;
import usc.sql.ir.Variable;

public class BenchmarkTest {


	
	
	
	private static void InterpretChecker(String arg0,String arg1,String summaryFolder) throws FileNotFoundException, IOException
	{
		//"/home/yingjun/Documents/StringAnalysis/MethodSummary/"
		//"Usage: rt.jar app_folder classlist.txt"
		JavaApp App=new JavaApp(arg0,arg1,"void main(java.lang.String[])");
		
		Map<String,Set<Variable>> targetMap = new HashMap<>();
    	Map<String,Set<NodeInterface>> paraMap = new HashMap<>();
    	Map<String,Set<String>> fieldMap = new HashMap<>();
		Map<String,Translator> tMap = new HashMap<>();
		long totalTranslate = 0,totalInterpret = 0;
		
		
		long t1,t2;
    	for(CFGInterface cfg:App.getCallgraph().getRTOInterface())
    	{
    		System.out.println(cfg.getSignature());
    		String signature=cfg.getSignature();
    		
    		
    		
    		if(signature.equals("<LoggerLib.Logger: void <clinit>()>")||signature.equals("<LoggerLib.Logger: void reportString(java.lang.String,java.lang.String)>"))
    			continue;
    		
    		//field																	def missing						
    		if(signature.contains("OverriddenToString"))
    			continue;
    		int loopCount = 0;
    		
    		
    		/*
    		String tempSig = signature.replaceAll("TestCases.", "");
    		int dot = tempSig.indexOf(".");
    		String tt = tempSig.substring(dot+1);
    		
    		if(tt.contains("Mix")||tt.contains("NestedLoop"))
    			loopCount = 2;
    		else
    			loopCount = 3;
    		*/
    		
      		//for(int i=1;i<=loopCount;i++)
    		//{  		
    		
    		
    		t1 = System.currentTimeMillis();
    		LayerRegion lll = new LayerRegion(null);
    		ReachingDefinition rd = new ReachingDefinition(cfg.getAllNodes(), cfg.getAllEdges(),lll.identifyBackEdges(cfg.getAllNodes(),cfg.getAllEdges(), cfg.getEntryNode()));	   		
    
    		
    		LayerRegion lr = new LayerRegion(cfg);
    	
    		//System.out.println(signature);
    		Translator t;
    		try {
	    		 t = new Translator(rd, lr,signature,summaryFolder,App);
	    	
	    		tMap.put(signature, t);
	    		paraMap.putAll(t.getParaMap());
	    		
	    		for(Entry<String,Set<String>> en: t.getFieldMap().entrySet())
	    		{
	    			if(fieldMap.containsKey(en.getKey()))
	    				fieldMap.get(en.getKey()).addAll(en.getValue());
	    			else
	    				fieldMap.put(en.getKey(), en.getValue());
	    		}
	    		//fieldMap.putAll(t.getFieldMap());
	    		 		
	    		if(t.getTargetLines().isEmpty())
	    			continue;
    		}catch (Exception e) {
    			e.printStackTrace();
    			continue;
    		}
    		
    		//Set<String> value = new HashSet<>();
    	
    		
    		int i1 = signature.indexOf("<"),i2 = signature.indexOf(":");
    		String label = "\""+signature.substring(i1+1,i2).replaceAll("testcases.standalone.","")+"\"";
    		//System.out.println(label);
    		
    		//Interpreter intp = new Interpreter(t,loopCount);
    		
    		Set<Variable> targetIR = new HashSet<>();
    		
    		for (List<String> d : t.getTargetLines().values()) {
    			for (String line : d) {
        			if(t.getTranslatedIR(line)!=null)
        				targetIR.addAll(t.getTranslatedIR(line));
    			}
    		}
    		
    		if(!targetMap.containsKey(signature))
    			targetMap.put(signature, targetIR);
    	 		
    		t2 = System.currentTimeMillis();
    		totalTranslate += t2-t1;
    	
    	}
    	int count = 0;
    	List<Result> results = new ArrayList<>();
    	for(Entry<String,Set<Variable>> en: targetMap.entrySet())
    	{
    		String signature = en.getKey();
    		int i1 = signature.indexOf("<"),i2 = signature.indexOf(":");
    		//System.out.println("\n"+signature);
    		
    	
    		t1 = System.currentTimeMillis();
    		Set<Variable> newIR = replaceExternal(en.getValue(),signature,paraMap,tMap,App);
    		t2 = System.currentTimeMillis();
    		totalTranslate += t2-t1;
    		
    		
    		t1 = System.currentTimeMillis();
			Interpreter intp = new Interpreter(newIR,fieldMap,3);
			Set<String> value = new HashSet<>();
			value.addAll(intp.getValueForIR());
			System.out.println(signature + ": " + value);
			for (String t : intp.getValueForIR()) {
				Result res = new Result();
				res.method = signature;
				res.value = t;
				results.add(res);
			}
			try (FileOutputStream fos = new FileOutputStream("Violist-Results.txt")) {
				fos.write(new GsonBuilder().setPrettyPrinting().create().toJson(results).getBytes());
			}
			
    	}
    	
    	System.out.println(count);
    	System.out.println("Total Trans: "+ totalTranslate);
    	System.out.println("Total Interp: "+ totalInterpret);
	}
    public static class Result {
    	  public String method;
    	  public String value;
      }

	
	private static Variable copyVar(Variable v)
	{
		
		if(v instanceof InternalVar)
			return new InternalVar(((InternalVar) v).getName(),((InternalVar) v).getK(),((InternalVar) v).getSigma(),((InternalVar) v).getRegionNum(),((InternalVar) v).getLine());
		else if(v instanceof Expression)
		{
			List<List<Variable>> newOperandList = new ArrayList<>();
			for(List<Variable> operandList:((Expression) v).getOperands())
			{
			List<Variable> tempOp = new ArrayList<>();
			for(Variable operand:operandList)
			{
				if(operand instanceof InternalVar)
					tempOp.add(new InternalVar(((InternalVar) operand).getName(),((InternalVar) operand).getK(),((InternalVar) operand).getSigma(),((InternalVar) operand).getRegionNum(),((InternalVar) operand).getLine()));
				else if(operand instanceof Expression)
					tempOp.add(copyVar(operand));
				else if(operand instanceof T)
					tempOp.add(new T(copyVar(((T) operand).getVariable()),((T) operand).getTVarName(),((T) operand).getRegionNumber(),((T) operand).getK(),((T) operand).isFi(),((T) operand).getLine()));
				else
					tempOp.add(operand);
			}
			newOperandList.add(tempOp);
			}
			return new Expression(newOperandList,((Expression) v).getOperation());
		}
		else if(v instanceof T)
		{
			return new T(copyVar(((T) v).getVariable()),((T) v).getTVarName(),((T) v).getRegionNumber(),((T) v).getK(),((T) v).isFi(),((T) v).getLine());
		}

		else
			return v;
	}
	
	private static Set<Variable> replaceExternal(Set<Variable> IRs,String signature,Map<String,Set<NodeInterface>> paraMap,Map<String,Translator> tMap,JavaApp App)
	{
		Set<Variable> vSet = new HashSet<>();
		for(Variable v: IRs)
		{
			if(paraMap.get(signature)==null)
				vSet.add(v);
			else
			{
				for(NodeInterface n:paraMap.get(signature))
				{
	    			Set<Variable> newIR = new HashSet<>();
	    				if(App.getCallgraph().getParents(signature).isEmpty())
	    					newIR.add(copyVar(v));
	    				else
	    				{
	    				String parentSig = App.getCallgraph().getParents(signature).iterator().next();
	    				newIR.addAll(replaceExternal(copyVar(v),n,tMap.get(parentSig)));
	    				}
	    			vSet.addAll(newIR);
	    		}				
			}			
		}
		boolean existPara = false;
		for(Variable v:vSet)
		{
			if(containPara(v))
				existPara = true;
		}
		if(!existPara)
			return vSet;
		else
		{
			if(App.getCallgraph().getParents(signature).isEmpty())
				return vSet;
			else
			{
				String parentSig = App.getCallgraph().getParents(signature).iterator().next();
				if(paraMap.get(parentSig)==null)
					return vSet;
				else
				{
					Set<Variable> copy = new HashSet<>();
					for(Variable vv:vSet)
						copy.add(copyVar(vv));
					Set<Variable> newIR = new HashSet<>();
					newIR.addAll(replaceExternal(copy,parentSig, paraMap, tMap, App));
					return newIR;
				}
			}
		}
		
	}
	
	static boolean containPara(Variable v)
	{

			if(v instanceof ExternalPara)
			{
				if(((ExternalPara) v).getName().contains("@parameter"))
					return true;
				else
					return false;
			}
			else if(v instanceof Expression)
			{
				for(List<Variable> operandList:((Expression) v).getOperands())
				{
					for(Variable operand: operandList)
					if(containPara(operand))
						return true;
				}
				return false;
			}
			else if(v instanceof T)
			{
			
				return containPara(((T) v).getVariable());
			}
			else
			return false;
	}
	
	
	private static Set<Variable> replaceExternal(Variable v,NodeInterface n,Translator t)
	{
		Set<Variable> returnSet = new HashSet<>();
		if(v instanceof ExternalPara)
		{
			if(((ExternalPara) v).getName().contains("@parameter"))
			{
				String tmp = ((ExternalPara) v).getName().split(":")[0].replaceAll("@parameter", "");
				int index = Integer.parseInt(tmp);
				
			//	System.out.println(index +" "+valueBox);
			
				List<ValueBox> valueBox = ((Unit)((Node)n).getActualNode()).getUseBoxes();
				
				String para = valueBox.get(index).getValue().toString();
				
				
				if(para.contains("\""))
					returnSet.add( new ConstantString(para));
				else if(valueBox.get(index).getValue().getType().toString().equals("int"))
				{
					//System.out.println("YOYOYO"+para);
					returnSet.add(new ConstantInt(para));
					//returnSet.add( new ConstantString(""+(char)Integer.parseInt(para)));
				}

				else
				{
					System.out.println("Type"+valueBox.get(index).getValue().getType().toString());
					System.out.println(para);
					Set<Variable> newIR = new HashSet<>();

					for(String line:t.getRD().getLineNumForUse(n, para))
						newIR.addAll(t.getTranslatedIR(line));
					
					System.out.println("newIR:"+newIR);
					Interpreter ip = new Interpreter(newIR,3);
					System.out.println("Outcome: "+ip.getValueForIR());
					
					
					
					returnSet.addAll(newIR);
				}
					
				
			}
			else
				returnSet.add(v);
			
		}

		else if(v instanceof Expression)
		{
			List<List<Variable>> newOperandList = new ArrayList<>();
			for(List<Variable> operandList:((Expression) v).getOperands())
			{
				List<Variable> tempOperand = new ArrayList<>();
				for(Variable operand:operandList)
				{
					tempOperand.addAll( replaceExternal(operand,n,t));
				}
				newOperandList.add(tempOperand);
			}
 			((Expression) v).setOperands(newOperandList);
 			returnSet.add(v);
		}
		else if(v instanceof T)
		{
			((T) v).setVariable(replaceExternal(((T) v).getVariable(),n,t).iterator().next());
			returnSet.add(v);
		}
		else
			returnSet.add(v);
		
		return returnSet;
	}
	
	
	
	
	
	
	public static void main(String[] args) throws IOException {
		//String name = "Concat";
		File f = File.createTempFile("tmp", "abfc");
		f.delete();
		f.mkdirs();
		String rtjar = args[0];
		String testcases = args[1];
		InterpretChecker(rtjar, testcases,
			f.getAbsolutePath());
	
		
	}

}
