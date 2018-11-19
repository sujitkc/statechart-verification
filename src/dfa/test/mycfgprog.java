package test;

import cfg.*;
import stablcfg.*;
import set.*;
import program.*;
import ast.*;

public class mycfgprog{

	public static void main(String ar[]){
		CFG sCfg=new CFG();
		
		//state
		final Name state_P=new Name("state_P");
		final Name state_Q=new Name("state_Q");
		final Name state_R=new Name("state_R");
		final Name state_S=new Name("state_S");
		
		//events
		final Name eQ=new Name("eQ");
		final Name eR=new Name("eR");
		final Name eSubmit=new Name("eSubmit");
		final Name eView=new Name("eView");
		
		final Name v_null=new Name("null");
		
		Name v_state=new Name("state");
		Name v_event=new Name("event");
		Name v_a=new Name("a");
		Name v_b=new Name("b");
		Name v_x=new Name("x");
		Name v_y=new Name("y");
		Name v_A=new Name("A");
		Name v_B=new Name("B");
		IntegerConstant i_1=new IntegerConstant(1);
		IntegerConstant i_2=new IntegerConstant(2);
		IntegerConstant i_3=new IntegerConstant(3);
		IntegerConstant i_4=new IntegerConstant(4);
		
		Statement s1=new AssignmentStatement(v_state,state_P);
		CFGBasicBlockNode B1=new CFGBasicBlockNode(s1);
		
		Statement s2=new AssignmentStatement(v_a,i_1);
		CFGBasicBlockNode B2=new CFGBasicBlockNode(s2);
		
		Statement s3=new AssignmentStatement(v_state,state_Q);
		CFGBasicBlockNode B3=new CFGBasicBlockNode(s3);
		
		Statement s4=new AssignmentStatement(v_b,i_2);
		CFGBasicBlockNode B4=new CFGBasicBlockNode(s4);
		
		Statement s5=new AssignmentStatement(v_state,state_R);
		CFGBasicBlockNode B5=new CFGBasicBlockNode(s5);
		
		Statement s6=new AssignmentStatement(v_A,v_a);
		CFGBasicBlockNode B6=new CFGBasicBlockNode(s6);
		
		Statement s7=new AssignmentStatement(v_b,i_2);
		CFGBasicBlockNode B7=new CFGBasicBlockNode(s7);
		
		Statement s8=new AssignmentStatement(v_state,state_R);
		CFGBasicBlockNode B8=new CFGBasicBlockNode(s8);
		
		Statement s9=new AssignmentStatement(v_A,v_a);
		CFGBasicBlockNode B9=new CFGBasicBlockNode(s9);
		
		Statement s10=new AssignmentStatement(v_x,v_A);
		CFGBasicBlockNode B10=new CFGBasicBlockNode(s10);
		
		Statement s11=new AssignmentStatement(v_y,v_B);
		CFGBasicBlockNode B11=new CFGBasicBlockNode(s11);
		
		Statement s12=new AssignmentStatement(v_state,state_S);
		CFGBasicBlockNode B12=new CFGBasicBlockNode(s12);
		
		Statement s13=new AssignmentStatement(v_B,v_b);
		CFGBasicBlockNode B13=new CFGBasicBlockNode(s13);
		
		Statement s14=new AssignmentStatement(v_a,i_4);
		CFGBasicBlockNode B14=new CFGBasicBlockNode(s14);
		
		Statement s15=new AssignmentStatement(v_state,state_Q);
		CFGBasicBlockNode B15=new CFGBasicBlockNode(s15);
		
		Statement s16=new AssignmentStatement(v_B,v_b);
		CFGBasicBlockNode B16=new CFGBasicBlockNode(s16);
		
		Statement s17=new AssignmentStatement(v_x,v_A);
		CFGBasicBlockNode B17=new CFGBasicBlockNode(s17);
		
		Statement s18=new AssignmentStatement(v_y,v_B);
		CFGBasicBlockNode B18=new CFGBasicBlockNode(s18);
		
		Statement s19=new AssignmentStatement(v_state,state_S);
		CFGBasicBlockNode B19=new CFGBasicBlockNode(s19);
		
		
		
		BinaryExpression be1=new BinaryExpression(v_event,v_state,"="); //rectify
		CFGDecisionNode D1=new CFGDecisionNode(be1);
		
		BinaryExpression be2=new BinaryExpression(v_state,state_P,"=");
		CFGDecisionNode D2=new CFGDecisionNode(be2);
		
		BinaryExpression be3=new BinaryExpression(v_event,eQ,"=");
		CFGDecisionNode D3=new CFGDecisionNode(be3);
		
		BinaryExpression be4=new BinaryExpression(v_state,state_Q,"=");
		CFGDecisionNode D4=new CFGDecisionNode(be4);
		
		BinaryExpression be5=new BinaryExpression(v_event,eR,"=");
		CFGDecisionNode D5=new CFGDecisionNode(be5);
		
		BinaryExpression be6=new BinaryExpression(v_event,eSubmit,"=");
		CFGDecisionNode D6=new CFGDecisionNode(be6);
		
		BinaryExpression be7=new BinaryExpression(v_state,state_R,"=");
		CFGDecisionNode D7=new CFGDecisionNode(be7);
		
		BinaryExpression be8=new BinaryExpression(v_a,v_null,"!=");
		CFGDecisionNode D8=new CFGDecisionNode(be8);
		
		BinaryExpression be9=new BinaryExpression(v_event,eView,"=");
		CFGDecisionNode D9=new CFGDecisionNode(be9);
		
		BinaryExpression be10=new BinaryExpression(v_event,eSubmit,"=");
		CFGDecisionNode D10=new CFGDecisionNode(be10);
		
		BinaryExpression be11=new BinaryExpression(v_b,v_null,"!=");
		CFGDecisionNode D11=new CFGDecisionNode(be11);
		
		BinaryExpression be12=new BinaryExpression(v_event,eView,"=");
		CFGDecisionNode D12=new CFGDecisionNode(be12);
		
		B1.setSuccessorNode(D1);
		B2.setSuccessorNode(B3);
		B3.setSuccessorNode(D1);
		B4.setSuccessorNode(B5);
		B5.setSuccessorNode(D1);
		B6.setSuccessorNode(B7);
		B7.setSuccessorNode(B8);
		B8.setSuccessorNode(D1);
		B9.setSuccessorNode(B10);
		B10.setSuccessorNode(B11);
		B11.setSuccessorNode(B12);
		B12.setSuccessorNode(D1);
		B13.setSuccessorNode(B14);
		B14.setSuccessorNode(B15);
		B15.setSuccessorNode(D1);
		B16.setSuccessorNode(B17);
		B17.setSuccessorNode(B18);
		B18.setSuccessorNode(B19);
		B19.setSuccessorNode(D1);
		D1.setThenSuccessorNode(D2);
		D1.setElseSuccessorNode(D1);
		D2.setThenSuccessorNode(D3);
		D2.setElseSuccessorNode(D4);
		D3.setThenSuccessorNode(B2);
		D3.setElseSuccessorNode(D5);
		D4.setThenSuccessorNode(D6);
		D4.setElseSuccessorNode(D7);
		D5.setThenSuccessorNode(B4);
		D5.setElseSuccessorNode(D4);
		D6.setThenSuccessorNode(D8);
		D6.setElseSuccessorNode(D9);
		D7.setThenSuccessorNode(D10);
		D7.setElseSuccessorNode(D1);
		D8.setThenSuccessorNode(B6);
		D8.setElseSuccessorNode(D10);
		D9.setThenSuccessorNode(B9);
		D9.setElseSuccessorNode(D7);
		D10.setThenSuccessorNode(D11);
		D10.setElseSuccessorNode(D12);
		D11.setThenSuccessorNode(B13);
		D11.setElseSuccessorNode(D1);
		D12.setThenSuccessorNode(B16);
		D12.setElseSuccessorNode(D1);
	
	}
}