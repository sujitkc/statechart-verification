package testcases.simulator2;

import com.code_intelligence.jazzer.api.FuzzedDataProvider;
import com.code_intelligence.jazzer.api.FuzzerSecurityIssueMedium;
import java.security.SecureRandom;
import java.util.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java_cup.runtime.Symbol;
 import static org.junit.Assert.*;

import frontend.FrontEnd;
import frontend.Parser;
import frontend.Typechecker;

import ast.*;
import java.io.*;
import simulator2.*;
import simulator2.cfg.*;
import simulator2.tree.*;
import simulator2.simulator.*;
public class TestSimulateFuzzer {
  static List<TestCase> testcaselist=new ArrayList<TestCase>();
  
  static List<String> filenameList=new ArrayList<String>();
  static List<String[]> sourceList=new ArrayList<String[]>();
  static List<String[]> destList=new ArrayList<String[]>();
  static int i=0;
  public static void populate(){
  
  	//atomic
  	
  	makeTestCase("data/constabl_actions/1_source_atomic/t1_1.stbl", new String[] {"A"}, new String[]{"B"});
  	
  	makeTestCase("data/constabl_actions/1_source_atomic/t1_2#1.stbl", new String[] {"A"}, new String[]{"B1"});
  	makeTestCase("data/constabl_actions/1_source_atomic/t1_2#2.stbl", new String[] {"A"}, new String[]{"B11"});
  	makeTestCase("data/constabl_actions/1_source_atomic/t1_2#3.stbl", new String[] {"A"}, new String[]{"A1","A2"});
  	
  	makeTestCase("data/constabl_actions/1_source_atomic/t1_3#1.stbl", new String[] {"A"}, new String[]{"B1"});
  	makeTestCase("data/constabl_actions/1_source_atomic/t1_3#2.stbl", new String[] {"A"}, new String[]{"B11"});
  	makeTestCase("data/constabl_actions/1_source_atomic/t1_3#3.stbl", new String[] {"A"}, new String[]{"A1","A2"});
  	
  	makeTestCase("data/constabl_actions/1_source_atomic/t1_4.stbl", new String[] {"A"}, new String[]{"A1","A2"});
  	
  	makeTestCase("data/constabl_actions/1_source_atomic/t1_5#1.stbl", new String[] {"A"}, new String[]{"R1A","R2A"});
  	makeTestCase("data/constabl_actions/1_source_atomic/t1_5#2.stbl", new String[] {"A"}, new String[]{"R1A1","R2A"});
  	makeTestCase("data/constabl_actions/1_source_atomic/t1_5#3.stbl", new String[] {"A"}, new String[]{"ShR1A","ShR2A", "R2A"});
  	
  	makeTestCase("data/constabl_actions/1_source_atomic/t1_6#1.stbl", new String[] {"A"}, new String[]{"R1A","R2A"});
  	makeTestCase("data/constabl_actions/1_source_atomic/t1_6#2.stbl", new String[] {"A"}, new String[]{"R1A1","R2A"});
  	makeTestCase("data/constabl_actions/1_source_atomic/t1_6#3.stbl", new String[] {"A"}, new String[]{"ShR1A","ShR2A", "R2A"});
  	
  	
  	//source - composite
  	makeTestCase("data/constabl_actions/2#1_source_composite(activeatomicsubstate)/t2#1_1.stbl", new String[] {"A1"}, new String[]{"B"});
  	
  	makeTestCase("data/constabl_actions/2#1_source_composite(activeatomicsubstate)/t2#1_2#1.stbl", new String[] {"A1"}, new String[]{"B1"});
  	makeTestCase("data/constabl_actions/2#1_source_composite(activeatomicsubstate)/t2#1_2#2.stbl", new String[] {"A1"}, new String[]{"B11"});
  	makeTestCase("data/constabl_actions/2#1_source_composite(activeatomicsubstate)/t2#1_2#3.stbl", new String[] {"A1"}, new String[]{"A1","A2"});
  	
  	makeTestCase("data/constabl_actions/2#1_source_composite(activeatomicsubstate)/t2#1_3#1.stbl", new String[] {"A1"}, new String[]{"B1"});
  	makeTestCase("data/constabl_actions/2#1_source_composite(activeatomicsubstate)/t2#1_3#2.stbl", new String[] {"A1"}, new String[]{"B11"});
  	makeTestCase("data/constabl_actions/2#1_source_composite(activeatomicsubstate)/t2#1_3#3.stbl", new String[] {"A1"}, new String[]{"R1A","R2A"});
  	
  	makeTestCase("data/constabl_actions/2#1_source_composite(activeatomicsubstate)/t2#1_4.stbl", new String[] {"A1"}, new String[]{"R1A","R2A"});
  	
  	makeTestCase("data/constabl_actions/2#1_source_composite(activeatomicsubstate)/t2#1_5#1.stbl", new String[] {"A1"}, new String[]{"R1A","R2A"});
  	makeTestCase("data/constabl_actions/2#1_source_composite(activeatomicsubstate)/t2#1_5#2.stbl", new String[] {"A1"}, new String[]{"R1A1","R2A"});
  	makeTestCase("data/constabl_actions/2#1_source_composite(activeatomicsubstate)/t2#1_5#3.stbl", new String[] {"A1"}, new String[]{"ShR1A","ShR2A", "R2A"});
  	
  	makeTestCase("data/constabl_actions/2#1_source_composite(activeatomicsubstate)/t2#1_6#1.stbl", new String[] {"A1"}, new String[]{"R1A","R2A"});
  	makeTestCase("data/constabl_actions/2#1_source_composite(activeatomicsubstate)/t2#1_6#2.stbl", new String[] {"A1"}, new String[]{"R1A1","R2A"});
  	makeTestCase("data/constabl_actions/2#1_source_composite(activeatomicsubstate)/t2#1_6#3.stbl", new String[] {"A1"}, new String[]{"ShR1A","ShR2A", "R2A"});
  	
  	//2#2
  	makeTestCase("data/constabl_actions/2#2_source_composite(activecompositesubstate)/t2#2_1.stbl", new String[] {"A11"}, new String[]{"B"});
  	
  	makeTestCase("data/constabl_actions/2#2_source_composite(activecompositesubstate)/t2#2_2#1.stbl", new String[] {"A11"}, new String[]{"B1"});
  	makeTestCase("data/constabl_actions/2#2_source_composite(activecompositesubstate)/t2#2_2#2.stbl", new String[] {"A11"}, new String[]{"B11"});
  	makeTestCase("data/constabl_actions/2#2_source_composite(activecompositesubstate)/t2#2_2#3.stbl", new String[] {"A11"}, new String[]{"A1","A2"});
  	
  	makeTestCase("data/constabl_actions/2#2_source_composite(activecompositesubstate)/t2#2_3#1.stbl", new String[] {"A11"}, new String[]{"B1"});
  	makeTestCase("data/constabl_actions/2#2_source_composite(activecompositesubstate)/t2#2_3#2.stbl", new String[] {"A11"}, new String[]{"B11"});
  	makeTestCase("data/constabl_actions/2#2_source_composite(activecompositesubstate)/t2#2_3#3.stbl", new String[] {"A11"}, new String[]{"R1A","R2A"});
  	
  	makeTestCase("data/constabl_actions/2#2_source_composite(activecompositesubstate)/t2#2_4.stbl", new String[] {"A11"}, new String[]{"R1A","R2A"});
  	
  	makeTestCase("data/constabl_actions/2#2_source_composite(activecompositesubstate)/t2#2_5#1.stbl", new String[] {"A11"}, new String[]{"R1A","R2A"});
  	makeTestCase("data/constabl_actions/2#2_source_composite(activecompositesubstate)/t2#2_5#2.stbl", new String[] {"A11"}, new String[]{"R1A1","R2A"});
  	makeTestCase("data/constabl_actions/2#2_source_composite(activecompositesubstate)/t2#2_5#3.stbl", new String[] {"A11"}, new String[]{"ShR1A","ShR2A", "R2A"});
  	
  	makeTestCase("data/constabl_actions/2#2_source_composite(activecompositesubstate)/t2#2_6#1.stbl", new String[] {"A11"}, new String[]{"R1A","R2A"});
  	makeTestCase("data/constabl_actions/2#2_source_composite(activecompositesubstate)/t2#2_6#2.stbl", new String[] {"A11"}, new String[]{"R1A1","R2A"});
  	makeTestCase("data/constabl_actions/2#2_source_composite(activecompositesubstate)/t2#2_6#3.stbl", new String[] {"A11"}, new String[]{"ShR1A","ShR2A", "R2A"});
  	
  	//2#3(4)
  	makeTestCase("data/constabl_actions/2#3(4)source_composite(activeshellsubstate)/t2#3(4)_1.stbl", new String[] {"ShR1A","ShR2A"}, new String[]{"B"});
  	
  	makeTestCase("data/constabl_actions/2#3(4)source_composite(activeshellsubstate)/t2#3(4)_2#1.stbl", new String[] {"ShR1A","ShR2A"}, new String[]{"B1"});
  	makeTestCase("data/constabl_actions/2#3(4)source_composite(activeshellsubstate)/t2#3(4)_2#2.stbl", new String[] {"ShR1A","ShR2A"}, new String[]{"B11"});
  	makeTestCase("data/constabl_actions/2#3(4)source_composite(activeshellsubstate)/t2#3(4)_2#3.stbl", new String[] {"ShR1A","ShR2A"}, new String[]{"A1","A2"});
  	
  	makeTestCase("data/constabl_actions/2#3(4)source_composite(activeshellsubstate)/t2#3(4)_3#1.stbl", new String[] {"ShR1A","ShR2A"}, new String[]{"B1"});
  	makeTestCase("data/constabl_actions/2#3(4)source_composite(activeshellsubstate)/t2#3(4)_3#2.stbl", new String[] {"ShR1A","ShR2A"}, new String[]{"B11"});
  	makeTestCase("data/constabl_actions/2#3(4)source_composite(activeshellsubstate)/t2#3(4)_3#3.stbl", new String[] {"ShR1A","ShR2A"}, new String[]{"R1A","R2A"});
  	
  	makeTestCase("data/constabl_actions/2#3(4)source_composite(activeshellsubstate)/t2#3(4)_4.stbl", new String[] {"ShR1A","ShR2A"}, new String[]{"R1A","R2A"});
  	
  	makeTestCase("data/constabl_actions/2#3(4)source_composite(activeshellsubstate)/t2#3(4)_5#1.stbl", new String[] {"ShR1A","ShR2A"}, new String[]{"R1A","R2A"});
  	makeTestCase("data/constabl_actions/2#3(4)source_composite(activeshellsubstate)/t2#3(4)_5#2.stbl", new String[] {"ShR1A","ShR2A"}, new String[]{"R1A1","R2A"});
  	makeTestCase("data/constabl_actions/2#3(4)source_composite(activeshellsubstate)/t2#3(4)_5#3.stbl", new String[] {"ShR1A","ShR2A"}, new String[]{"ShR1A","ShR2A", "R2A"});
  	
  	makeTestCase("data/constabl_actions/2#3(4)source_composite(activeshellsubstate)/t2#3(4)_6#1.stbl", new String[] {"ShR1A","ShR2A"}, new String[]{"R1A","R2A"});
  	makeTestCase("data/constabl_actions/2#3(4)source_composite(activeshellsubstate)/t2#3(4)_6#2.stbl", new String[] {"ShR1A","ShR2A"}, new String[]{"R1A1","R2A"});
  	makeTestCase("data/constabl_actions/2#3(4)source_composite(activeshellsubstate)/t2#3(4)_6#3.stbl", new String[] {"ShR1A","ShR2A"}, new String[]{"ShR1A","ShR2A", "R2A"});
  	
  	//3#1
  	makeTestCase("data/constabl_actions/3#1_source_substateofcomposite(activeatomic)/t3#1_1.stbl", new String[] {"A1"}, new String[]{"B"});
  	
  	makeTestCase("data/constabl_actions/3#1_source_substateofcomposite(activeatomic)/t3#1_2#1.stbl", new String[] {"A1"}, new String[]{"B1"});
  	makeTestCase("data/constabl_actions/3#1_source_substateofcomposite(activeatomic)/t3#1_2#2.stbl", new String[] {"A1"}, new String[]{"B11"});
  	makeTestCase("data/constabl_actions/3#1_source_substateofcomposite(activeatomic)/t3#1_2#3.stbl", new String[] {"A1"}, new String[]{"A1","A2"});
  	
  	makeTestCase("data/constabl_actions/3#1_source_substateofcomposite(activeatomic)/t3#1_3#1.stbl", new String[] {"A1"}, new String[]{"B1"});
  	makeTestCase("data/constabl_actions/3#1_source_substateofcomposite(activeatomic)/t3#1_3#2.stbl", new String[] {"A1"}, new String[]{"B11"});
  	makeTestCase("data/constabl_actions/3#1_source_substateofcomposite(activeatomic)/t3#1_3#3.stbl", new String[] {"A1"}, new String[]{"R1A","R2A"});
  	
  	makeTestCase("data/constabl_actions/3#1_source_substateofcomposite(activeatomic)/t3#1_4.stbl", new String[] {"A1"}, new String[]{"R1A","R2A"});
  	
  	makeTestCase("data/constabl_actions/3#1_source_substateofcomposite(activeatomic)/t3#1_5#1.stbl", new String[] {"A1"}, new String[]{"R1A","R2A"});
  	makeTestCase("data/constabl_actions/3#1_source_substateofcomposite(activeatomic)/t3#1_5#2.stbl", new String[] {"A1"}, new String[]{"R1A1","R2A"});
  	makeTestCase("data/constabl_actions/3#1_source_substateofcomposite(activeatomic)/t3#1_5#3.stbl", new String[] {"A1"}, new String[]{"ShR1A","ShR2A", "R2A"});
  	
  	makeTestCase("data/constabl_actions/3#1_source_substateofcomposite(activeatomic)/t3#1_6#1.stbl", new String[] {"A1"}, new String[]{"R1A","R2A"});
  	makeTestCase("data/constabl_actions/3#1_source_substateofcomposite(activeatomic)/t3#1_6#2.stbl", new String[] {"A1"}, new String[]{"R1A1","R2A"});
  	makeTestCase("data/constabl_actions/3#1_source_substateofcomposite(activeatomic)/t3#1_6#3.stbl", new String[] {"A1"}, new String[]{"ShR1A","ShR2A", "R2A"});
  	
  	//3#2
  	
  	makeTestCase("data/constabl_actions/3#2_source_substateofcomposite(activecompositesubstate)/t3#2_1.stbl", new String[] {"A11"}, new String[]{"B"});
  	
  	makeTestCase("data/constabl_actions/3#2_source_substateofcomposite(activecompositesubstate)/t3#2_2#1.stbl", new String[] {"A11"}, new String[]{"B1"});
  	makeTestCase("data/constabl_actions/3#2_source_substateofcomposite(activecompositesubstate)/t3#2_2#2.stbl", new String[] {"A11"}, new String[]{"B11"});
  	makeTestCase("data/constabl_actions/3#2_source_substateofcomposite(activecompositesubstate)/t3#2_2#3.stbl", new String[] {"A11"}, new String[]{"R1A","R2A"});
  	
  	makeTestCase("data/constabl_actions/3#2_source_substateofcomposite(activecompositesubstate)/t3#2_3#1.stbl", new String[] {"A11"}, new String[]{"B1"});
  	makeTestCase("data/constabl_actions/3#2_source_substateofcomposite(activecompositesubstate)/t3#2_3#2.stbl", new String[] {"A11"}, new String[]{"B11"});
  	makeTestCase("data/constabl_actions/3#2_source_substateofcomposite(activecompositesubstate)/t3#2_3#3.stbl", new String[] {"A11"}, new String[]{"R1A","R2A"});
  	
  	makeTestCase("data/constabl_actions/3#2_source_substateofcomposite(activecompositesubstate)/t3#2_4.stbl", new String[] {"A11"}, new String[]{"R1A","R2A"});
  	
  	makeTestCase("data/constabl_actions/3#2_source_substateofcomposite(activecompositesubstate)/t3#2_5#1.stbl", new String[] {"A11"}, new String[]{"R1A","R2A"});
  	makeTestCase("data/constabl_actions/3#2_source_substateofcomposite(activecompositesubstate)/t3#2_5#2.stbl", new String[] {"A11"}, new String[]{"R1A1","R2A"});
  	makeTestCase("data/constabl_actions/3#2_source_substateofcomposite(activecompositesubstate)/t3#2_5#3.stbl", new String[] {"A11"}, new String[]{"ShR1A","ShR2A", "R2A"});
  	
  	makeTestCase("data/constabl_actions/3#2_source_substateofcomposite(activecompositesubstate)/t3#2_6#1.stbl", new String[] {"A11"}, new String[]{"R1A","R2A"});
  	makeTestCase("data/constabl_actions/3#2_source_substateofcomposite(activecompositesubstate)/t3#2_6#2.stbl", new String[] {"A11"}, new String[]{"R1A1","R2A"});
  	makeTestCase("data/constabl_actions/3#2_source_substateofcomposite(activecompositesubstate)/t3#2_6#3.stbl", new String[] {"A11"}, new String[]{"ShR1A","ShR2A", "R2A"});
  	
  	//t3#3(4)
  	
  	makeTestCase("data/constabl_actions/3#3(4)source_substateofcomposite(activeshellsubstate)/t3#3(4)_1.stbl", new String[] {"ShR1A","ShR2A"}, new String[]{"B"});
  	
  	makeTestCase("data/constabl_actions/3#3(4)source_substateofcomposite(activeshellsubstate)/t3#3(4)_2#1.stbl", new String[] {"ShR1A","ShR2A"}, new String[]{"B1"});
  	makeTestCase("data/constabl_actions/3#3(4)source_substateofcomposite(activeshellsubstate)/t3#3(4)_2#2.stbl", new String[] {"ShR1A","ShR2A"}, new String[]{"B11"});
  	makeTestCase("data/constabl_actions/3#3(4)source_substateofcomposite(activeshellsubstate)/t3#3(4)_2#3.stbl", new String[] {"ShR1A","ShR2A"}, new String[]{"R1A","R2A"});
  	
  	makeTestCase("data/constabl_actions/3#3(4)source_substateofcomposite(activeshellsubstate)/t3#3(4)_3#1.stbl", new String[] {"ShR1A","ShR2A"}, new String[]{"B1"});
  	makeTestCase("data/constabl_actions/3#3(4)source_substateofcomposite(activeshellsubstate)/t3#3(4)_3#2.stbl", new String[] {"ShR1A","ShR2A"}, new String[]{"B11"});
  	makeTestCase("data/constabl_actions/3#3(4)source_substateofcomposite(activeshellsubstate)/t3#3(4)_3#3.stbl", new String[] {"ShR1A","ShR2A"}, new String[]{"R1A","R2A"});
  	
  	makeTestCase("data/constabl_actions/3#3(4)source_substateofcomposite(activeshellsubstate)/t3#3(4)_4.stbl", new String[] {"ShR1A","ShR2A"}, new String[]{"R1A","R2A"});
  	
  	makeTestCase("data/constabl_actions/3#3(4)source_substateofcomposite(activeshellsubstate)/t3#3(4)_5#1.stbl", new String[] {"ShR1A","ShR2A"}, new String[]{"R1A","R2A"});
  	makeTestCase("data/constabl_actions/3#3(4)source_substateofcomposite(activeshellsubstate)/t3#3(4)_5#2.stbl", new String[] {"ShR1A","ShR2A"}, new String[]{"R1A1","R2A"});
  	makeTestCase("data/constabl_actions/3#3(4)source_substateofcomposite(activeshellsubstate)/t3#3(4)_5#3.stbl", new String[] {"ShR1A","ShR2A"}, new String[]{"ShR1A","ShR2A", "R2A"});
  	
  	makeTestCase("data/constabl_actions/3#3(4)source_substateofcomposite(activeshellsubstate)/t3#3(4)_6#1.stbl", new String[] {"ShR1A","ShR2A"}, new String[]{"R1A","R2A"});
  	makeTestCase("data/constabl_actions/3#3(4)source_substateofcomposite(activeshellsubstate)/t3#3(4)_6#2.stbl", new String[] {"ShR1A","ShR2A"}, new String[]{"R1A1","R2A"});
  	makeTestCase("data/constabl_actions/3#3(4)source_substateofcomposite(activeshellsubstate)/t3#3(4)_6#3.stbl", new String[] {"ShR1A","ShR2A"}, new String[]{"ShR1A","ShR2A", "R2A"});
  	
  	
  	//5#1
  	
  	makeTestCase("data/constabl_actions/5#1_source_region(activeatomicsubstate)/t5#1_1.stbl", new String[] {"ShR1A","ShR2A"}, new String[]{"B"});
  	
  	makeTestCase("data/constabl_actions/5#1_source_region(activeatomicsubstate)/t5#1_2#1.stbl", new String[] {"ShR1A","ShR2A"}, new String[]{"B1"});
  	makeTestCase("data/constabl_actions/5#1_source_region(activeatomicsubstate)/t5#1_2#2.stbl", new String[] {"ShR1A","ShR2A"}, new String[]{"B11"});
  	makeTestCase("data/constabl_actions/5#1_source_region(activeatomicsubstate)/t5#1_2#3.stbl", new String[] {"ShR1A","ShR2A"}, new String[]{"R1A","R2A"});
  	
  	makeTestCase("data/constabl_actions/5#1_source_region(activeatomicsubstate)/t5#1_3#1.stbl", new String[] {"ShR1A","ShR2A"}, new String[]{"B1"});
  	makeTestCase("data/constabl_actions/5#1_source_region(activeatomicsubstate)/t5#1_3#2.stbl", new String[] {"ShR1A","ShR2A"}, new String[]{"B11"});
  	makeTestCase("data/constabl_actions/5#1_source_region(activeatomicsubstate)/t5#1_3#3.stbl", new String[] {"ShR1A","ShR2A"}, new String[]{"R1A","R2A"});
  	
  	makeTestCase("data/constabl_actions/5#1_source_region(activeatomicsubstate)/t5#1_4.stbl", new String[] {"ShR1A","ShR2A"}, new String[]{"R1A","R2A"});
  	
  	makeTestCase("data/constabl_actions/5#1_source_region(activeatomicsubstate)/t5#1_5#1.stbl", new String[] {"ShR1A","ShR2A"}, new String[]{"R1A","R2A"});
  	makeTestCase("data/constabl_actions/5#1_source_region(activeatomicsubstate)/t5#1_5#2.stbl", new String[] {"ShR1A","ShR2A"}, new String[]{"R1A1","R2A"});
  	makeTestCase("data/constabl_actions/5#1_source_region(activeatomicsubstate)/t5#1_5#3.stbl", new String[] {"ShR1A","ShR2A"}, new String[]{"ShR1A","ShR2A", "R2A"});
  	
  	makeTestCase("data/constabl_actions/5#1_source_region(activeatomicsubstate)/t5#1_6#1.stbl", new String[] {"ShR1A","ShR2A"}, new String[]{"R1A","R2A"});
  	makeTestCase("data/constabl_actions/5#1_source_region(activeatomicsubstate)/t5#1_6#2.stbl", new String[] {"ShR1A","ShR2A"}, new String[]{"R1A1","R2A"});
  	makeTestCase("data/constabl_actions/5#1_source_region(activeatomicsubstate)/t5#1_6#3.stbl", new String[] {"ShR1A","ShR2A"}, new String[]{"ShR1A","ShR2A", "R2A"});
  	
  	//5#2
  	
  	makeTestCase("data/constabl_actions/5#2_source_region(compositesubstate)/t5#2_1.stbl", new String[] {"ShR1A","ShR2A"}, new String[]{"B"});
  	
  	makeTestCase("data/constabl_actions/5#2_source_region(compositesubstate)/t5#2_2#1.stbl", new String[] {"ShR1A","ShR2A"}, new String[]{"B1"});
  	makeTestCase("data/constabl_actions/5#2_source_region(compositesubstate)/t5#2_2#2.stbl", new String[] {"ShR1A","ShR2A"}, new String[]{"B11"});
  	makeTestCase("data/constabl_actions/5#2_source_region(compositesubstate)/t5#2_2#3.stbl", new String[] {"ShR1A","ShR2A"}, new String[]{"R1A","R2A"});
  	
  	makeTestCase("data/constabl_actions/5#2_source_region(compositesubstate)/t5#2_3#1.stbl", new String[] {"ShR1A","ShR2A"}, new String[]{"B1"});
  	makeTestCase("data/constabl_actions/5#2_source_region(compositesubstate)/t5#2_3#2.stbl", new String[] {"ShR1A","ShR2A"}, new String[]{"B11"});
  	makeTestCase("data/constabl_actions/5#2_source_region(compositesubstate)/t5#2_3#3.stbl", new String[] {"ShR1A","ShR2A"}, new String[]{"R1A","R2A"});
  	
  	makeTestCase("data/constabl_actions/5#2_source_region(compositesubstate)/t5#2_4.stbl", new String[] {"ShR1A","ShR2A"}, new String[]{"R1A","R2A"});
  	
  	makeTestCase("data/constabl_actions/5#2_source_region(compositesubstate)/t5#2_5#1.stbl", new String[] {"ShR1A","ShR2A"}, new String[]{"R1A","R2A"});
  	makeTestCase("data/constabl_actions/5#2_source_region(compositesubstate)/t5#2_5#2.stbl", new String[] {"ShR1A","ShR2A"}, new String[]{"R1A1","R2A"});
  	makeTestCase("data/constabl_actions/5#2_source_region(compositesubstate)/t5#2_5#3.stbl", new String[] {"ShR1A","ShR2A"}, new String[]{"ShR1A","ShR2A", "R2A"});
  	
  	makeTestCase("data/constabl_actions/5#2_source_region(compositesubstate)/t5#2_6#1.stbl", new String[] {"ShR1A","ShR2A"}, new String[]{"R1A","R2A"});
  	makeTestCase("data/constabl_actions/5#2_source_region(compositesubstate)/t5#2_6#2.stbl", new String[] {"ShR1A","ShR2A"}, new String[]{"R1A1","R2A"});
  	makeTestCase("data/constabl_actions/5#2_source_region(compositesubstate)/t5#2_6#3.stbl", new String[] {"ShR1A","ShR2A"}, new String[]{"ShR1A","ShR2A", "R2A"});
  	
  	//5#3
  	makeTestCase("data/constabl_actions/5#3_source_region(shellsubstate)/t5#3_1.stbl", new String[] {"Sh1R1A","Sh1R2A","ShR2A"}, new String[]{"B"});
  	
  	makeTestCase("data/constabl_actions/5#3_source_region(shellsubstate)/t5#3_2#1.stbl", new String[] {"Sh1R1A","Sh1R2A","ShR2A"}, new String[]{"B1"});
  	makeTestCase("data/constabl_actions/5#3_source_region(shellsubstate)/t5#3_2#2.stbl", new String[] {"Sh1R1A","Sh1R2A","ShR2A"}, new String[]{"B11"});
  	makeTestCase("data/constabl_actions/5#3_source_region(shellsubstate)/t5#3_2#3.stbl", new String[] {"Sh1R1A","Sh1R2A","ShR2A"}, new String[]{"R1A","R2A"});
  	
  	makeTestCase("data/constabl_actions/5#3_source_region(shellsubstate)/t5#3_3#1.stbl", new String[] {"Sh1R1A","Sh1R2A","ShR2A"}, new String[]{"B1"});
  	makeTestCase("data/constabl_actions/5#3_source_region(shellsubstate)/t5#3_3#2.stbl", new String[] {"Sh1R1A","Sh1R2A","ShR2A"}, new String[]{"B11"});
  	makeTestCase("data/constabl_actions/5#3_source_region(shellsubstate)/t5#3_3#3.stbl", new String[] {"Sh1R1A","Sh1R2A","ShR2A"}, new String[]{"R1A","R2A"});
  	
  	makeTestCase("data/constabl_actions/5#3_source_region(shellsubstate)/t5#3_4.stbl", new String[] {"Sh1R1A","Sh1R2A","ShR2A"}, new String[]{"R1A","R2A"});
  	
  	makeTestCase("data/constabl_actions/5#3_source_region(shellsubstate)/t5#3_5#1.stbl", new String[] {"Sh1R1A","Sh1R2A","ShR2A"}, new String[]{"R1A","R2A"});
  	makeTestCase("data/constabl_actions/5#3_source_region(shellsubstate)/t5#3_5#2.stbl", new String[] {"Sh1R1A","Sh1R2A","ShR2A"}, new String[]{"R1A1","R2A"});
  	makeTestCase("data/constabl_actions/5#3_source_region(shellsubstate)/t5#3_5#3.stbl", new String[] {"Sh1R1A","Sh1R2A","ShR2A"}, new String[]{"ShR1A","ShR2A", "R2A"});
  	
  	makeTestCase("data/constabl_actions/5#3_source_region(shellsubstate)/t5#3_6#1.stbl", new String[] {"Sh1R1A","Sh1R2A","ShR2A"}, new String[]{"R1A","R2A"});
  	makeTestCase("data/constabl_actions/5#3_source_region(shellsubstate)/t5#3_6#2.stbl", new String[] {"Sh1R1A","Sh1R2A","ShR2A"}, new String[]{"R1A1","R2A"});
  	makeTestCase("data/constabl_actions/5#3_source_region(shellsubstate)/t5#3_6#3.stbl", new String[] {"Sh1R1A","Sh1R2A","ShR2A"}, new String[]{"ShR1A","ShR2A", "R2A"});
  	
  	//6#1
  	
  	makeTestCase("data/constabl_actions/6#1_source_substateofregion(atomicsubstate)/t6#1_1.stbl", new String[] {"ShR1A","ShR2A"}, new String[]{"B"});
  	
  	makeTestCase("data/constabl_actions/6#1_source_substateofregion(atomicsubstate)/t6#1_2#1.stbl", new String[] {"ShR1A","ShR2A"}, new String[]{"B1"});
  	makeTestCase("data/constabl_actions/6#1_source_substateofregion(atomicsubstate)/t6#1_2#2.stbl", new String[] {"ShR1A","ShR2A"}, new String[]{"B11"});
  	makeTestCase("data/constabl_actions/6#1_source_substateofregion(atomicsubstate)/t6#1_2#3.stbl", new String[] {"ShR1A","ShR2A"}, new String[]{"R1A","R2A"});
  	
  	makeTestCase("data/constabl_actions/6#1_source_substateofregion(atomicsubstate)/t6#1_3#1.stbl", new String[] {"ShR1A","ShR2A"}, new String[]{"B1"});
  	makeTestCase("data/constabl_actions/6#1_source_substateofregion(atomicsubstate)/t6#1_3#2.stbl", new String[] {"ShR1A","ShR2A"}, new String[]{"B11"});
  	makeTestCase("data/constabl_actions/6#1_source_substateofregion(atomicsubstate)/t6#1_3#3.stbl", new String[] {"ShR1A","ShR2A"}, new String[]{"R1A","R2A"});
  	
  	makeTestCase("data/constabl_actions/6#1_source_substateofregion(atomicsubstate)/t6#1_4.stbl", new String[] {"ShR1A","ShR2A"}, new String[]{"R1A","R2A"});
  	
  	makeTestCase("data/constabl_actions/6#1_source_substateofregion(atomicsubstate)/t6#1_5#1.stbl", new String[] {"ShR1A","ShR2A"}, new String[]{"R1A","R2A"});
  	makeTestCase("data/constabl_actions/6#1_source_substateofregion(atomicsubstate)/t6#1_5#2.stbl", new String[] {"ShR1A","ShR2A"}, new String[]{"R1A1","R2A"});
  	makeTestCase("data/constabl_actions/6#1_source_substateofregion(atomicsubstate)/t6#1_5#3.stbl", new String[] {"ShR1A","ShR2A"}, new String[]{"ShR1A","ShR2A", "R2A"});
  	
  	makeTestCase("data/constabl_actions/6#1_source_substateofregion(atomicsubstate)/t6#1_6#1.stbl", new String[] {"ShR1A","ShR2A"}, new String[]{"R1A","R2A"});
  	makeTestCase("data/constabl_actions/6#1_source_substateofregion(atomicsubstate)/t6#1_6#2.stbl", new String[] {"ShR1A","ShR2A"}, new String[]{"R1A1","R2A"});
  	makeTestCase("data/constabl_actions/6#1_source_substateofregion(atomicsubstate)/t6#1_6#3.stbl", new String[] {"ShR1A","ShR2A"}, new String[]{"ShR1A","ShR2A", "R2A"});
  	
  	//6#2
  	
  	makeTestCase("data/constabl_actions/6#2_source_substateofregion(compositesubstate)/t6#2_1.stbl", new String[] {"ShR1A","ShR2A"}, new String[]{"B"});
  	
  	makeTestCase("data/constabl_actions/6#2_source_substateofregion(compositesubstate)/t6#2_2#1.stbl", new String[] {"ShR1A","ShR2A"}, new String[]{"B1"});
  	makeTestCase("data/constabl_actions/6#2_source_substateofregion(compositesubstate)/t6#2_2#2.stbl", new String[] {"ShR1A","ShR2A"}, new String[]{"B11"});
  	makeTestCase("data/constabl_actions/6#2_source_substateofregion(compositesubstate)/t6#2_2#3.stbl", new String[] {"ShR1A","ShR2A"}, new String[]{"R1A","R2A"});
  	
  	makeTestCase("data/constabl_actions/6#2_source_substateofregion(compositesubstate)/t6#2_3#1.stbl", new String[] {"ShR1A","ShR2A"}, new String[]{"B1"});
  	makeTestCase("data/constabl_actions/6#2_source_substateofregion(compositesubstate)/t6#2_3#2.stbl", new String[] {"ShR1A","ShR2A"}, new String[]{"B11"});
  	makeTestCase("data/constabl_actions/6#2_source_substateofregion(compositesubstate)/t6#2_3#3.stbl", new String[] {"ShR1A","ShR2A"}, new String[]{"R1A","R2A"});
  	
  	makeTestCase("data/constabl_actions/6#2_source_substateofregion(compositesubstate)/t6#2_4.stbl", new String[] {"ShR1A","ShR2A"}, new String[]{"R1A","R2A"});
  	
  	makeTestCase("data/constabl_actions/6#2_source_substateofregion(compositesubstate)/t6#2_5#1.stbl", new String[] {"ShR1A","ShR2A"}, new String[]{"R1A","R2A"});
  	makeTestCase("data/constabl_actions/6#2_source_substateofregion(compositesubstate)/t6#2_5#2.stbl", new String[] {"ShR1A","ShR2A"}, new String[]{"R1A1","R2A"});
  	makeTestCase("data/constabl_actions/6#2_source_substateofregion(compositesubstate)/t6#2_5#3.stbl", new String[] {"ShR1Aa","ShR2A"}, new String[]{"ShR1A","ShR2A", "R2A"});
  	
  	makeTestCase("data/constabl_actions/6#2_source_substateofregion(compositesubstate)/t6#2_6#1.stbl", new String[] {"ShR1A","ShR2A"}, new String[]{"R1A","R2A"});
  	makeTestCase("data/constabl_actions/6#2_source_substateofregion(compositesubstate)/t6#2_6#2.stbl", new String[] {"ShR1A","ShR2A"}, new String[]{"R1A1","R2A"});
  	makeTestCase("data/constabl_actions/6#2_source_substateofregion(compositesubstate)/t6#2_6#3.stbl", new String[] {"ShR1A","ShR2A"}, new String[]{"ShR1A","ShR2A", "R2A"});
  	
  	//6#3
  	makeTestCase("data/constabl_actions/6#3_source_region(shellsubstate)/t6#3_1.stbl", new String[] {"ShR1A","ShR2A", "ShR2A1"}, new String[]{"B"});
  	
  	makeTestCase("data/constabl_actions/6#3_source_region(shellsubstate)/t6#3_2#1.stbl", new String[] {"ShR1A","ShR2A", "ShR2A1"}, new String[]{"B1"});
  	makeTestCase("data/constabl_actions/6#3_source_region(shellsubstate)/t6#3_2#2.stbl", new String[] {"ShR1A","ShR2A", "ShR2A1"}, new String[]{"B11"});
  	makeTestCase("data/constabl_actions/6#3_source_region(shellsubstate)/t6#3_2#3.stbl", new String[] {"ShR1A","ShR2A", "ShR2A1"}, new String[]{"R1A","R2A"});
  	
  	makeTestCase("data/constabl_actions/6#3_source_region(shellsubstate)/t6#3_3#1.stbl", new String[] {"ShR1A","ShR2A", "ShR2A1"}, new String[]{"B1"});
  	makeTestCase("data/constabl_actions/6#3_source_region(shellsubstate)/t6#3_3#2.stbl", new String[] {"ShR1A","ShR2A", "ShR2A1"}, new String[]{"B11"});
  	makeTestCase("data/constabl_actions/6#3_source_region(shellsubstate)/t6#3_3#3.stbl", new String[] {"ShR1A","ShR2A", "ShR2A1"}, new String[]{"R1A","R2A"});
  	
  	makeTestCase("data/constabl_actions/6#3_source_region(shellsubstate)/t6#3_4.stbl", new String[] {"ShR1A","ShR2A", "ShR2A1"}, new String[]{"R1A","R2A"});
  	
  	makeTestCase("data/constabl_actions/6#3_source_region(shellsubstate)/t6#3_5#1.stbl", new String[] {"ShR1A","ShR2A", "ShR2A1"}, new String[]{"R1A","R2A"});
  	makeTestCase("data/constabl_actions/6#3_source_region(shellsubstate)/t6#3_5#2.stbl", new String[] {"ShR1A","ShR2A", "ShR2A1"}, new String[]{"R1A1","R2A"});
  	makeTestCase("data/constabl_actions/6#3_source_region(shellsubstate)/t6#3_5#3.stbl", new String[] {"ShR1A","ShR2A", "ShR2A1"}, new String[]{"ShR1A","ShR2A", "R2A"});
  	
  	makeTestCase("data/constabl_actions/6#3_source_region(shellsubstate)/t6#3_6#1.stbl", new String[] {"ShR1A","ShR2A", "ShR2A1"}, new String[]{"R1A","R2A"});
  	makeTestCase("data/constabl_actions/6#3_source_region(shellsubstate)/t6#3_6#2.stbl", new String[] {"ShR1A","ShR2A", "ShR2A1"}, new String[]{"R1A1","R2A"});
  	makeTestCase("data/constabl_actions/6#3_source_region(shellsubstate)/t6#3_6#3.stbl", new String[] {"ShR1A","ShR2A", "ShR2A1"}, new String[]{"ShR1A","ShR2A", "R2A"});
  	 }
  public static void makeTestCase(String filename, String[] sourceConfig, String[] destConfig){
  	TestCase tc=new TestCase(filename,sourceConfig, destConfig);
  	testcaselist.add(tc);
  	filenameList.add(filename);
  	sourceList.add(sourceConfig);
  	destList.add(destConfig);
  }
  public static void fuzzerInitialize() {
    // Optional initialization to be run before the first call to fuzzerTestOneInput.
  }

  public static void fuzzerTestOneInput(FuzzedDataProvider data) {
	populate();
	String[] randomSourceState=data.pickValue(sourceList);
	String[] randomDestState=data.pickValue(destList);
	String fname=data.pickValue(filenameList);
	System.out.println(randomSourceState);
	System.out.println(randomDestState);
	System.out.println(fname);
	System.out.println();
	
	
	 Set<State> newconfig=runTest("test"+(i++), fname, randomSourceState);
  	  String[] output=new String[newconfig.size()];
      
          int j=0;
	  for(State s:newconfig) {
	   		output[j++]=s.name;
	   		}
	  Arrays.sort(output);
	  Arrays.sort(randomDestState);
	  System.out.println("******** Testing Assertion ******** comparing ******"+Arrays.toString(randomDestState)+"==="+Arrays.toString(output));
          assertArrayEquals(randomDestState,output);	
	
	/*List<String> events=new ArrayList<String>();
	events.add("e1");
	events.add("e2");
	events.add("e3");
	events.add("e4");
	events.add("e5");
	events.add("e6");
	int eventlength=data.consumeInt​(1,events.size());
	List<String> randomEvents=data.pickValues(events,eventlength);
	System.out.println(randomEvents);
	
	int sourcelength=data.consumeInt​(1,events.size());
	List<String> randomEvents=data.pickValues(events,eventlength);
	System.out.println(randomEvents);
	
	int eventlength=data.consumeInt​(1,events.size());
	List<String> randomEvents=data.pickValues(events,eventlength);
	System.out.println(randomEvents);
	*/
	
    /*String input = data.consumeRemainingAsString();
    // Without the hook in ExampleFuzzerHooks.java, the value of random would change on every
    // invocation, making it almost impossible to guess for the fuzzer.
    long random = new SecureRandom().nextLong();
    if (input.startsWith("magicstring" + random) && input.length() > 30
        && input.charAt(25) == 'C') {
      mustNeverBeCalled();
    }*/
  }
 /* public void callTestCases(){
  try{
  	
  	System.out.println("Test begins : Number of testcases : "+ testcaselist.size());
  	for(int i=0;i<testcaselist.size();i++){
  	  String inputfile=testcaselist.get(i).filename;
  	  String[] listofActiveAtomicStates= testcaselist.get(i).sourceConfig;
  	  String[] expected = testcaselist.get(i).destConfig;
  	  Set<State> newconfig=runTest("test"+i, inputfile, listofActiveAtomicStates);
  	  String[] output=new String[newconfig.size()];
      
          int j=0;
	  for(State s:newconfig) {
	   		output[j++]=s.name;
	   		}
	  Arrays.sort(output);
	  Arrays.sort(expected);
	  System.out.println("******** Testing Assertion ******** comparing ******"+Arrays.toString(expected)+"==="+Arrays.toString(output));
          assertArrayEquals(expected,output);		
  		}
  	}
 	 catch(Exception e){
  		
          e.printStackTrace();
          System.exit(1);
  	}
  }*/
 

  public static Set<State> runTest(String testname, String filename, String[] statename) {
    Statechart statechart = test_template(testname, filename);

    try
    {
      Set<State> configuration = new HashSet<>();
      Simulator simulator = new Simulator(statechart, configuration);
      for(int i=0;i<statename.length;i++){
        State s1 = simulator.getSubstateByName(statename[i], statechart);
        configuration.add(s1);
        System.out.println(s1.name);
      }
        // State s2 = simulator.getSubstateByName("B2B", statechart);
      
      //configuration.add(s2);
     
      //System.out.println(s2.name);
     Set<State> newConfiguration = simulator.simulationStep("e1");
     return newConfiguration;
    }
    catch(Exception e) {
      System.out.println("Something Went Wrong!\n");
      e.printStackTrace();
      System.exit(1);
    }
    return null;
  }
  public static Statechart test_template(String testName, String inputFileName) {
    System.out.println("\n\n>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n>>>>>>>>>>>>>>>>>>>>>>>>\n\n"+testName+"\n\n<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<\n>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n");

    Typechecker typechecker;

    Statechart statechart = null;
    String input = inputFileName;
    try {
      Parser parser = new FrontEnd(input).getParser();    
      Symbol result = parser.parse();
      statechart = (Statechart)result.value;
    }
    catch(FileNotFoundException e) {
      System.out.println("Couldn't open file '" + input + "'"); 
    }
    catch(Exception e) {
      System.out.println("Couldn't parse '" + input + "' : " + e.getMessage()); 
      e.printStackTrace();
      System.exit(1);
    }
    try {
      new Typechecker(statechart).typecheck();
      return statechart;
    }
    catch(Exception e) {
      System.out.println("Couldn't typecheck '" + input + "' : " + e.getMessage()); 
      e.printStackTrace();
    }
    return null;
  }
  private static void mustNeverBeCalled() {
    throw new FuzzerSecurityIssueMedium("mustNeverBeCalled has been called");
  }
}

