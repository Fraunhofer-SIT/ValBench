#!/bin/bash
cwd=$(pwd)

cd "$cwd" || exit 1 
mvn package || exit 1 

