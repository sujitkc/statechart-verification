package testcases.simulator2;

import java.util.*;

public class TestCase {
	public String filename;
	public String[] sourceConfig;
	
	public String[] destConfig;
	public String[] events;
	public TestCase(String name, String[] s, String[] d){
		this.filename=name;
		this.sourceConfig=s;
		this.destConfig=d;
	}
	public TestCase(String name, String[] s, String[] d, String[] e){
		this.filename=name;
		this.sourceConfig=s;
		this.destConfig=d;
		this.events=e;
	}
}
