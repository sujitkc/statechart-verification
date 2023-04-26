package generators;

import ast.*;
import java.lang.Math;   
import java.util.Random;
public class RandomInputGenerator extends InputGenerator {
  	static Random rd = new Random();
  public static BooleanConstant getRandomBoolean(){
  	
  	return new BooleanConstant(rd.nextBoolean());
  }
  public static IntegerConstant getRandomInt(){
  	return new IntegerConstant(rd.nextInt());
  }
  
  public static IntegerConstant getRandomInt(int lowerBound, int upperBound){
  
  	return new IntegerConstant(rd.nextInt(upperBound - lowerBound) + lowerBound);
  }
}
