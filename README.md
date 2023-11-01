## Steps to build and run
- Install [maven](https://maven.apache.org/) and the run:

```
mvn install compile package
java -jar target/analyser-0.1.0.jar <stable_specification_file> <se_max_depth> <property_of_interest>
```

Currently, the `property_of_interest` argument can be set to either `NON_DET` (Non-Determinism) or `STUCK_SPEC` (Stuck Specification).
The engine would alert the user of satisfiability of those properties by printing on the console.

## Testing
To run all tests use `mvn test`.
To run a specific test class, use `mvn -Dtest=<TEST_CLASS_NAME> test`

- Any files related to testing must be placed in the `src/test/resources` directory.
- Additional test classes must be placed in `src/test/java`.

##Running in commandline
git clone -b gradle-build git@github.com:SymbolicPathFinder/jpf-symbc.git --recurse-submodules

gradle :jpf-symbc:buildJars
sudo update-alternatives --set java /usr/lib/jvm/java-8-openjdk-amd64/jre/bin/java
java -Xmx1024m -ea -jar ../jpf-core/build/RunJPF.jar ./src/examples/demo/NumericExample.jpf

clang -I src/main/java/include/ -emit-llvm -c -g -O0 -Xclang -disable-O0-optnone test1.c
