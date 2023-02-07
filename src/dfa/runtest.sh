#!/usr/bin/bash

testcase=${1}

java -cp lib/java-cup-11a.jar:lib/jflex-1.4.1.jar:lib/junit-4.8.1.jar:ast:classes:./ org.junit.runner.JUnitCore testcases.Test$testcase
