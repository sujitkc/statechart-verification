package constabl.actionprogram;

import ast.*;

public class ProgramPoint{
    Statement st;
    Environment env;
    Object element; //denotes the state/transition that the statement is contained in
    //there should be something that denotes if its in entry/exit action block
    String location; //can take value entry, exit or transition
}