package generators;

import ast.*;
import java.lang.Math;   
import java.util.Random;
import com.code_intelligence.jazzer.api.FuzzedDataProvider;
import com.code_intelligence.jazzer.api.FuzzerSecurityIssueMedium;

public class RandomInputGenerator extends InputGenerator {
  	 Random rd = new Random();
  public BooleanConstant getBoolean(){
  	
  	return new BooleanConstant(rd.nextBoolean());
  }
  public IntegerConstant getInt(){
  	
  	return new IntegerConstant(rd.nextInt(41)-20);
  }
  
  public IntegerConstant getInt(int lowerBound, int upperBound){
  
  	return new IntegerConstant(rd.nextInt(upperBound - lowerBound) + lowerBound);
  }
}
