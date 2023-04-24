package constabl.simulator;
import ast.*;
import java.util.*;
import constabl.ast.*;

public class Simulator{
    List<CodeNode> codenodelist=new ArrayList<CodeNode>();
    List<CodeNode> execnodelist=new ArrayList<CodeNode>();
    Statechart statechart;
    public Simulator(Statechart statechart){
        this.statechart=statechart;
        System.out.println("************* Concurrent Simulator Begins ***********");
        computeCFA(statechart);
        
    }
    
    public CFA getCFAfromList(String name, List<CodeNode> nodelist){
        for(int i=0;i<nodelist.size();i++){
            if(nodelist.get(i) instanceof constabl.ast.CFA && (nodelist.get(i)).name.equals(name))
                return (CFA)nodelist.get(i);
        }
       
        return null;
    }
    public void computeCFA(Statechart sc){
        System.out.println("Computing CFAs... ");

        traverseStates((State)sc);
        traverseTransitions((State)sc);

        System.out.println("Computing CFAs Completed ... \n" + codenodelist);

     }
     public void traverseTransitions(State s){
        if(s.states.size()==0){}
        else{
            for(Transition t:s.transitions){
                System.out.println("Computing cfa for the transition action : "+t.name);
                CFA cfan=StatementToCFA.convertToCFA(t.action, t.name+"_A");
                codenodelist.add(cfan);
            }
            for(State e:s.states){
                traverseTransitions(e);
            }
        }
     }
     public void traverseStates(State s){
        if(s.states.size()==0){
            //s is atomic
            System.out.println("Computing cfa for the atomic state : "+s.getFullName());
            CFA cfan=StatementToCFA.convertToCFA(s.entry, s.getFullName()+"_N");
             codenodelist.add(cfan);
            CFA cfax=StatementToCFA.convertToCFA(s.exit, s.getFullName()+"_X");   
             codenodelist.add(cfax);
            
        }
        else{
            if(s instanceof ast.Shell){
                System.out.println("Computing cfa for the shell state : "+s.getFullName());
                CFA cfan=StatementToCFA.convertToCFA(s.entry, s.getFullName()+"_N");
                codenodelist.add(cfan);
                CFA cfax=StatementToCFA.convertToCFA(s.exit, s.getFullName()+"_X");
                codenodelist.add(cfax);

            }
            else if(s.getSuperstate() instanceof ast.Shell){
                System.out.println("Computing cfa for the region state : "+s.getFullName());
                CFA cfan=StatementToCFA.convertToCFA(s.entry, s.getFullName()+"_N");
                codenodelist.add(cfan);
                CFA cfax=StatementToCFA.convertToCFA(s.exit, s.getFullName()+"_X"); 
                codenodelist.add(cfax);


            }
            else{ 
                System.out.println("Computing cfa for the composite state : "+s.name);
                CFA cfan=StatementToCFA.convertToCFA(s.entry, s.getFullName()+"_N");
                codenodelist.add(cfan);
               CFA cfax=StatementToCFA.convertToCFA(s.exit, s.getFullName()+"_X");
                codenodelist.add(cfax);
            }
            for(State e:s.states){
                traverseStates(e);
            }
        }
     }


    
    }