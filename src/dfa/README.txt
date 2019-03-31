To build
--------
  Change the value of MYLIBPATH to point to the directory where the jlex and cup are located.
  make test

To clean
--------
  make clean

To run
------
  Single test case:
  ./run.sh <test-case-name>
  For example, to run the test case TestFrontEnd, we run the following command:
  ./run.sh TestFrontEnd

  All test cases (listed in run.sh):
  ./run.sh

Test inputs are placed in data/ directory.
