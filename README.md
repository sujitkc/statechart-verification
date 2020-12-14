## Steps to build and run
- Install [maven](https://maven.apache.org/) and the run:

```
mvn install compile package
java -jar target/analyser-0.1.0.jar <stable_specification_file> <se_max_depth> <property_of_interest>
```

Currently, the `property_of_interest` argument can be set to either `NON_DET` (Non-Determinism) or `STUCK_SPEC` (Stuck Specification).
The engine would alert the user of satisfiability of those properties by printing on the console.
