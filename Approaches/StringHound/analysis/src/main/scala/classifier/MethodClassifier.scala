package classifier

import java.net.URL

import extract.methods.lib.detect.StructurePreservingRep
import org.apache.commons.math3.stat.correlation.SpearmansCorrelation
import org.opalj.br.Method
import org.opalj.br.analyses.Project

import scala.io.Source

object MethodClassifier {

  val equal = false

  val spearmansCorrelation = new SpearmansCorrelation()


  var encMethods = new java.util.HashSet[String]();

def readFile(f:java.io.File): java.util.HashSet[String] = {
  val br = new java.io.BufferedReader(new java.io.FileReader(f))
  while (true) {
    val s = br.readLine();
    if (s == null)
     return encMethods;
    encMethods.add(s);
  }
  encMethods
}


  def classify(method: Method, project: Project[URL]): Boolean = {
  if (encMethods.isEmpty()) 
     encMethods = readFile(new java.io.File("StringHound-Input.txt"));
  val str = method.classFile.fqn + " " + method.signature
    if (method.body.isEmpty) return false
//	System.out.println("Checking " + str);
    val q = encMethods.contains(str)
    if (q)
	System.out.println("Found " + str);
    q
  }

  def correlation(xArray: Array[Double], yArray: Array[Double]): Double = spearmansCorrelation.correlation(xArray, yArray)

}
