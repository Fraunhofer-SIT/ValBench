# ValBench - Java and Android Precise Value Test Site

## Overview
This test suite is meant to provide test cases for precise value and string extraction.

We welcome new contributions (test cases as well as approaches).

```Approaches/``` contains the code for the approaches we are allowed to legally redistribute (i.e. BlueSeal, Harvester and ValDroid are sadly missing, but can be obtained from the respective authors). Note that these are not neccesary to run the evaluation since we include precomputed results.
```ValueTestcases/``` contains the code for the ValBench test case suite.
```valbenchbaseapk/``` contains the code for a minimum APK file, which is used as base when building the Android based test cases.
```Evaluator/Results/``` contains the precomputed results. The evaluator will use the precomputed results if they are available; these allow an easy way to exactly replicate the paper results. In order to recompute these results, just delete them. The Evaluator will call the corresponding tools to re-run them on the testsuite.

## Building
Build using ```./BuildEverything.sh```.

This builds the approaches as well as the ValBench test cases.
See ```ValueTestcases/target/valbench-testcases-*.jar``` for the Java benchmark suite and ```ValueTestcases/target/valbench-testcases-android.apk``` for the Android one.
The folders ```ValueTestcases/target/single-testcase-jars/``` and ```ValueTestcases/target/single-testcase-apks/``` contain the shrinked variants of the Java and Android testcases (one test case per file). These get build automatically when building everything.

This is the Logging Point signature (gets inserted during Build by ```ExplicitLoggingPointCreator```):

```<valbench.ExplicitLoggingPoint: void explicitLoggingPoint(java.lang.Object[])>```

Alternatively, if your tool does not support arrays as arguments, use 

```<valbench.ExplicitLoggingPoint: void explicitLoggingPoint(java.lang.Object)>```

## Evaluation

Run the simple evaluation:

```java -jar Evaluator/target/value-testcases-evaluator-0.0.1-SNAPSHOT.jar```

In order to replicate the paper results, use the ```--papereval <DIR>``` argument to write the LaTeX files to a directory.

Note that if you want to rerun tools, you need to have Java 8 installed. Use the ```JAVA8``` environment variable to point to the Java Home directory of Java 8.
Just delete the corresponding results of the tool from the ```Evaluator/Results``` folder.


## Paper

If you use this test suite, feel free to cite our SOAP '24 paper ValBench: Benchmarking Exact Value Analysis (DOI 10.1145/3652588.3663322).


## License Information

We license our own code part of ValBench under the Apache License 2.0.

The Harvester results loader (```Approaches/Harvester-Results-Reader/```) is licensed under the Apache License 2.0.

COAL (```Approaches/COAL/```) is licensed under the Apache License 2.0.

JSA (```Approaches/JSA/string-2.1/```) is licensed under the GPL v2.
JSA automation (```Approaches/JSA/automaton-1.12```) is licensed under the BSD license.

StringHound (```Approaches/StringHound```) is licensed under the BSD 2 license.

Violist (```Approaches/Violist```) is licensed under the GPL license.
