#!/usr/bin/bash

testcase=${1}

java -cp lib/junit-4.8.1.jar:ast:classes:./ org.junit.runner.JUnitCore testcases.Test$testcase
