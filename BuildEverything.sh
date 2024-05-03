#!/bin/bash
#/bin/bash BuildOnlyValBench.sh || exit 1

cwd=$(pwd)

cd "$cwd/Approaches/StringHound/analysis/" || exit 1
sbt -Dsbt.io.jdktimestamps=true 'set test in assembly := {}' clean compile assembly || exit 1



cd "$cwd/Approaches/COAL/" || exit 1
mvn package || exit 1
cd "$cwd/Approaches/JSA/automaton-1.12/" || exit 1
mvn install || exit 1
cd "$cwd/Approaches/JSA/string-2.1/" || exit 1
mvn package || exit 1

cd "$cwd/Approaches/Violist/code/Pixy/" || exit 1
mvn org.apache.maven.plugins:maven-install-plugin:3.1.1:install-file -DgroupId=pixy -DartifactId=pixy -Dversion=1.0.0 -Dpackaging=jar -Dfile=Pixy.jar || exit 1

cd "$cwd/Approaches/Violist/code/graphs/" || exit 1
mvn install || exit 2000
cd "$cwd/Approaches/Violist/code/NewStringAnalysis/" || exit 1
mvn package || exit 2000

/bin/bash BuildOnlyValBench.sh || exit 1
