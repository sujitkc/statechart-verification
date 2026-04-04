package generators;

import ast.*;
import java.lang.Math;   
import java.util.Random;
import java.util.Scanner;
public class InteractiveInputGenerator extends InputGenerator {
  	 Random rd = new Random();
  	 Scanner in=new Scanner(System.in);
  	public BooleanConstant getBoolean(){
  	System.out.println("Enter a boolean - 0 or 1 (press 'enter' once done) : ");
  	try{
  	String input = in.nextLine();
  	if(input.equals("0"))
  		return new BooleanConstant(false);
  	else if(input.equals("1"))
  		return new BooleanConstant(true);
  	else
  		throw new Exception();
  	}
  	catch(Exception e){
  		System.out.println("Input was not a boolean - Stopping execution");
  		System.exit(0);
  		//return new IntegerConstant(rd.nextInt());
  	}
  	return new BooleanConstant(rd.nextBoolean());
  }
  public IntegerConstant getInt(){
  	
  	System.out.println("Enter a integer (press 'enter' once done) : ");
  	try{
  	String input = in.nextLine();
  	int num=Integer.parseInt(input);
  	return new IntegerConstant(num);
  	}
  	catch(Exception e){
  		System.out.println("Input was not an integer - Stopping execution");
  		System.exit(0);
  		//return new IntegerConstant(rd.nextInt());
  	}
  	return null;
  }
  
  public IntegerConstant getRandomInt(int lowerBound, int upperBound){
  
  	return new IntegerConstant(rd.nextInt(upperBound - lowerBound) + lowerBound);
  }
}
