package constabl;
import ast.*;
import java.util.*;
import constabl.ast.*;
import constabl.ast.connectors.*;

public class StatechartSimulator extends Simulator {
    private Statechart statechart = null;
    private List<String> eventQueue=null;
    private Configuration activeconfig=new Configuration();
    
    
    EventQueue eq=new EventQueue();
    private final String tinit=EventQueue.tinit;
    public StatechartSimulator(Statechart sc){
        this.statechart=sc;
        computeCFA(sc);
        this.simulate();
        
    }
    public void computeCFA(Statechart sc){
        System.out.println("Computing CFAs... ");

        traverseStates((State)sc);
        traverseTransitions((State)sc);

       // System.out.println("CFAs... " + codenodelist);

     }
     public void traverseTransitions(State s){
        if(s.states.size()==0){}
        else{
            for(Transition t:s.transitions){
                System.out.println("Computing cfa for the transition action : "+t.name);
                CFA cfan=StatementToCFA.convertToCFA(t.action, t.name+"_A");
                // Seq snb=new Seq(t.name+"_AB");
                // Seq sne=new Seq(t.name+"_AE");
                
                // cfan.addPrev(snb);
                // sne.addPrev(cfan);
                // codenodelist.add(snb);
                // codenodelist.add(sne);
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
            //compute CFA for entry
            /* 1. Create a seq node for entry begin (Statename_NB)
             * 2. Create a seq node for entry end (Statename_NE)
             * 3. Create a cfa node for the entry (statename_N) 
             * 4. Form link - Statename_NB -> statename_N -> statename_NE
             * 5. add all to the code node list
             */
            CFA cfan=StatementToCFA.convertToCFA(s.entry, s.getFullName()+"_N");

            //  Seq snb=new Seq(s.name+"_NB");
            //  Seq sne=new Seq(s.name+"_NE");
            //  cfan.addPrev(snb);
            //  sne.addPrev(cfan);
            //  codenodelist.add(snb);
            //  codenodelist.add(sne);
             codenodelist.add(cfan);
             
            //compute CFA for exit
            /* 1. Create a seq node for entry begin (Statename_XB)
             * 2. Create a seq node for entry end (Statename_XE)
             * 3. Create a cfa node for the entry (statename_X) 
             * 4. Form link - Statename_XB -> statename_X -> statename_XE
             * 5. add all to the code node list
             */
            CFA cfax=StatementToCFA.convertToCFA(s.exit, s.getFullName()+"_X");   
            //  Seq sxb=new Seq(s.name+"_XB");
            //  Seq sxe=new Seq(s.name+"_XE");
             
            //  cfax.addPrev(sxb);
            //  sxe.addPrev(cfax);
            //  codenodelist.add(sxb);
            //  codenodelist.add(sxe);
             codenodelist.add(cfax);
            
            
        }
        else{
            if(s instanceof ast.Shell){
                System.out.println("Computing cfa for the shell state : "+s.getFullName());
                /*Shell state detected - entry compute nodes
                 * 1. Create a Seq node with name statename_NB
                 * 2. Create a Fork node with name statename_NE
                 * 3. create a CFA node with the name statename_N
                 * 4. form linkages - statename_NB -> statename_N -> statename_NE
                 * 
                 */
                CFA cfan=StatementToCFA.convertToCFA(s.entry, s.getFullName()+"_N");
                    // Seq snb=new Seq(s.name+"_NB");
                    // Fork sne=new Fork(s.name+"_NE");
                    
                    // cfan.addPrev(snb);
                    // sne.addPrev(cfan);
                    // codenodelist.add(snb);
                    // codenodelist.add(sne);
                    codenodelist.add(cfan);
                /*Shell state detected - exit compute nodes
                 * 1. Create a Join mode with name statename_XB
                 * 2. Create a Seq node with name statename_XE
                 * 3. create a CFA node with the name statename_X
                 * 4. form linkages - statename_XB -> statename_X -> statename_XE
                 * 5. add all to code nodelist
                 */
                CFA cfax=StatementToCFA.convertToCFA(s.exit, s.getFullName()+"_X");
                    // Join sxb=new Join(s.name+"_XB");
                    // Seq sxe=new Seq(s.name+"_XE");
                  
                    // cfax.addPrev(sxb);
                    // sxe.addPrev(cfax);
                    // codenodelist.add(sxb);
                    // codenodelist.add(sxe);
                    codenodelist.add(cfax);

            }
            else if(s.getSuperstate() instanceof ast.Shell){
                System.out.println("Computing cfa for the region state : "+s.getFullName());
                /*Region state detected - entry compute nodes
                 * 1. Create a Fork node with name s.superstate.name_NE
                 * 2. Create a Seq node with name statename_NE
                 * 3. create a CFA node with the name statename_N
                 * 4. form linkages - s.superstate.name_NE -> statename_N -> statename_NE
                 * 
                 */
                CFA cfan=StatementToCFA.convertToCFA(s.entry, s.getFullName()+"_N");

                //  Fork snb=new Fork((s.getSuperstate()).name+"_NB");
                //  Seq sne=new Seq(s.name+"_NE");
                //  cfan.addPrev(snb);
                //  sne.addPrev(cfan);
                //  codenodelist.add(snb);
                //  codenodelist.add(sne);
                 codenodelist.add(cfan);
             /*REgion state detected - exit compute nodes
              * 1. Create a seq mode with name statename_XB
              * 2. Create a join node with name s.superstate.name_XB
              * 3. create a CFA node with the name statename_X
              * 4. form linkages - statename_XB -> statename_X -> s.superstate.name_XB
              * 5. add all to code nodelist
              */
              CFA cfax=StatementToCFA.convertToCFA(s.exit, s.getFullName()+"_X"); 
                    // Seq sxb=new Seq(s.name+"_XB");
                    // Join sxe=new Join((s.getSuperstate()).name+"_XB");
                    
                    // cfax.addPrev(sxb);
                    // sxe.addPrev(cfax);
                    // codenodelist.add(sxb);
                    // codenodelist.add(sxe);
                 codenodelist.add(cfax);


            }
            else{ 
                System.out.println("Computing cfa for the composite state : "+s.name);
                //compute CFA for entry
            /* 1. Create a seq node for entry begin (Statename_NB)
             * 2. Create a seq node for entry end (Statename_NE)
             * 3. Create a cfa node for the entry (statename_N) 
             * 4. Form link - Statename_NB -> statename_N -> statename_NE
             * 5. add all to the code node list
             */
            CFA cfan=StatementToCFA.convertToCFA(s.entry, s.getFullName()+"_N");

            //  Seq snb=new Seq(s.name+"_NB");
            //  Seq sne=new Seq(s.name+"_NE");
            //  cfan.addPrev(snb);
            //  sne.addPrev(cfan);
            //  codenodelist.add(snb);
            //  codenodelist.add(sne);
             codenodelist.add(cfan);
             
            //compute CFA for exit
            /* 1. Create a seq node for entry begin (Statename_XB)
             * 2. Create a seq node for entry end (Statename_XE)
             * 3. Create a cfa node for the entry (statename_X) 
             * 4. Form link - Statename_XB -> statename_X -> statename_XE
             * 5. add all to the code node list
             */
            CFA cfax=StatementToCFA.convertToCFA(s.exit, s.getFullName()+"_X");

            //  Seq sxb=new Seq(s.name+"_XB");
            //  Seq sxe=new Seq(s.name+"_XE");
            //  cfax.addPrev(sxb);
            //  sxe.addPrev(cfax);
            //  codenodelist.add(sxb);
            //  codenodelist.add(sxe);
             codenodelist.add(cfax);
            }
            for(State e:s.states){
                traverseStates(e);
            }
        }
     }
    public void simulate(){
       // System.out.println("simulate inside Statechart simulator");

        initialize();
        for(String ev : eq.eventQueue){
            consumeEvent(ev);   
        }
    }
    public void simulateCode(){
                CodeSimulator cs=new CodeSimulator(codenodelist);
                Seq sq=getSeqfromList("start");
                //CFA startCFA=getNextCFAtoSeqConnector(cfalist,sq);
                List<CodeNode> nodelist=getNextNode(sq);
                CFA startCFA=(CFA)nodelist.get(0);
                cs.execute(startCFA);
               
    }
    public void initialize(){
        //initialize the statechart with tinit transition
    //    System.out.println("initialize");
        List<Transition> identifiedTransitions=new ArrayList<Transition>();
        Transition initTransition=new Transition(tinit,null,null,null,null,null);
        identifiedTransitions.add(initTransition);
       
        computeCode(activeconfig,identifiedTransitions);
    }
   
    public void consumeEvent(String event){
        //detectNonDeterminism
            //find transitions
            List<Transition> transitions=findTransitions(activeconfig,event);
            System.out.print(transitions.size()+" Transitions found - ");
            for(Transition t:transitions)
                System.out.print(t.name+", ");
            System.out.println();
            findNonDeterminism(transitions);
            //if no non determinism detected
            if(transitions.size()>0){             
                activeconfig=takeTransitions(activeconfig,transitions);
                }

    }
    public void computeCode(Configuration currentconfig,List<Transition> transitions){
        for(Transition t:transitions){
            if(t.name.equals(tinit)){
                currentconfig.addState(statechart);
                while(!currentconfig.isStable())
                    currentconfig=computeDefaultEntry(currentconfig);
                // for the stable config
                    computeDefaultEntry(currentconfig);
                
                //CodeSimulator cs=new CodeSimulator(cfalist, forklist, joinlist, seqlist);
                //simulateCode();
                activeconfig=currentconfig;
            }
            else{
                
            }
        }
    }
    public void computeGraph(){
        /*
         * 1. We have to construct the graph that marks the sequential and forked execution 
         * 2. forked execution is when all the CFAs are added to the pool to pick a node arbitrarily.
         * 3. join is when all the nodes that are finished in the pool - then it can join
         * 4. there can be pool within a pool like set theory.
         * 5. How to maintain pools and random node pick in pool is the logic to be worked out.
         * 
         */
        //computation of CFA pool
        // A pool can contain a single or multiple CFA nodes with active control points in each CFA in the pool
        /*Q1 - how to add CFA to pool
         *Q2 - when to add to pool
         *Q3 - can pool become empty
         *Is there a pool merge/pool split dynamically/statically
         */
    }
    public Configuration computeDefaultEntry(Configuration currentconfig)
    {
       // System.out.println("inside compute default entry");
        Configuration newconfig=new Configuration();
        Connector initial;
        if((currentconfig.getCurrentStates()).size()>0){
            //create a fork node here
            initial=new Fork("initial");
        }
        else{
            //create a sequence node here
            initial=new Seq("initial");
        }

        for (State entryState : currentconfig.getCurrentStates()){
            if(entryState instanceof ast.Shell){
                System.out.println( "Shell state : "+entryState.getFullName());
                CFA cfa=getCFAfromList(entryState.getFullName()+"_N");
                cfa.addPrev(initial);
                // CFA cfa=StatementToCFA.convertToCFA(entryState.entry, entryState.getFullName()+"_N");
                // Seq f=getSeqfromList(entryState.getSuperstate().getFullName());
                // cfa.addPrev(f);
                // //cfalist.add(cfa);  
                // codenodelist.add(cfa);
                newconfig.addAllState(entryState.states);          
            }
            else if(entryState.states.size()>0){
                System.out.println( "Composite state : "+entryState.getFullName());
                //CFA cfa=StatementToCFA.convertToCFA(entryState.entry, entryState.getFullName()+"_N");
                if(entryState instanceof ast.Statechart){
                    //do nothing
                    // Seq s=getSeqfromList("start");
                    // cfa.addPrev(s);
                }
                else if(entryState.getSuperstate() instanceof ast.Shell){
                    // Fork f=getForkfromList(entryState.getSuperstate().getFullName());
                    // cfa.addPrev(f);
                }
                else{
                    //System.out.println()
                    // Seq s=getSeqfromList((entryState.getSuperstate()).getFullName());
                    // cfa.addPrev(s);
                }
                //cfalist.add(cfa); 
                //codenodelist.add(cfa);
                newconfig.addState(entryState.states.get(0)); 
            }
            else{
                System.out.println( "Atomic state : "+entryState.getFullName());
                // CFA cfa=StatementToCFA.convertToCFA(entryState.entry, entryState.getFullName()+"_N");
                // Seq f=getSeqfromList(entryState.getSuperstate().getFullName());
                // cfa.addPrev(f);
                // //cfalist.add(cfa);
                // codenodelist.add(cfa);
            }

        }
        //System.out.println()
        return newconfig;
    } 
  /*  public Configuration computeDefaultEntry(Configuration currentconfig)
    {
        Configuration newconfig=new Configuration();
        for (State entryState : currentconfig.getCurrentStates()){
            if(entryState instanceof ast.Shell){
                System.out.println( "Shell state : "+entryState.getFullName());
                CFA cfa=StatementToCFA.convertToCFA(entryState.entry, entryState.getFullName()+"_N");
                Seq f=getSeqfromList(entryState.getSuperstate().getFullName());
                cfa.addPrev(f);
                //cfalist.add(cfa);  
                codenodelist.add(cfa);
                newconfig.addAllState(entryState.states);          
            }
            else if(entryState.states.size()>0){
                System.out.println( "Composite state : "+entryState.getFullName());
                CFA cfa=StatementToCFA.convertToCFA(entryState.entry, entryState.getFullName()+"_N");
                if(entryState instanceof ast.Statechart){
                    //do nothing
                    Seq s=getSeqfromList("start");
                    cfa.addPrev(s);
                }
                else if(entryState.getSuperstate() instanceof ast.Shell){
                    Fork f=getForkfromList(entryState.getSuperstate().getFullName());
                    cfa.addPrev(f);
                }
                else{
                    //System.out.println()
                    Seq s=getSeqfromList((entryState.getSuperstate()).getFullName());
                    cfa.addPrev(s);
                }
                //cfalist.add(cfa); 
                codenodelist.add(cfa);
                newconfig.addState(entryState.states.get(0)); 
            }
            else{
                System.out.println( "Atomic state : "+entryState.getFullName());
                CFA cfa=StatementToCFA.convertToCFA(entryState.entry, entryState.getFullName()+"_N");
                Seq f=getSeqfromList(entryState.getSuperstate().getFullName());
                cfa.addPrev(f);
                //cfalist.add(cfa);
                codenodelist.add(cfa);
            }

        }
        //System.out.println()
        return newconfig;
        
    }*/
  
    // public CFA computeCFADefaultEntry(Configuration currentconfig, State entryState)
    // {      
    //     try{                  
    //        System.out.println("\n== entering state : "+entryState.name);
    //        State state=entryState;    
    //             if(state instanceof ast.Shell){
    //                 System.out.print("Shell detected:  "+state.name);  
    //                 CFA actionBlock=StatementToCFA.convertToCFA(state.entry);
    //                 System.out.println("action block cfa 1: "+actionBlock);
    //                 Fork f=new Fork();
    //                 actionBlock.addNext(f);
    //                 List<State> newEntryStates=state.getAllSubstates();
    //                 for(State s:newEntryStates){
    //                     System.out.print(s.name+", ");
    //                     //this is wrong
    //                     actionBlock=computeActionDefaultEntryForState(currentconfig,s);
    //                     System.out.println("action block cfa 2: "+actionBlock);
    //                     f.addNext(actionBlock);
    //                 }
    //                 System.out.println("action block cfa 2: "+actionBlock);                    
    //             }else if(state.getAllSubstates().size()>0){
                    
    //                 System.out.println("Composite detected:  "+state.name);
    //                 CFA actionBlock=StatementToCFA.convertToCFA(state.entry);
    //                 Seq s=new Seq();
    //                 actionBlock.setNext(s);
    //                 s.setNext(computeActionDefaultEntryForState(currentconfig,state.getAllSubstates().get(0)));
                    
    //                 System.out.println("action block cfa composite: "+actionBlock);
    //                 //actionBlock.addAll(computeActionDefaultEntryForState(currentconfig,state.getAllSubstates().get(0),actionBlock));
                    

                    
    //             }
    //             else{
    //                 //found atomic state
    //                 System.out.println("Atomic detected:  "+state.name);
    //                 //actionBlock=computeActionDefaultEntryForState(currentconfig,state.getAllSubstates().get(0),actionBlock);
    //                 CFA actionBlock=StatementToCFA.convertToCFA(state.entry);
    //                 System.out.println("action block cfa composite: "+actionBlock);

    //                 activeconfig.addState(state);
    //                // actionSequence=addProgramPoints(actionSequence,state,null,ActionType.STATE_ENTRY_ACTION);

                   
    //             }
           
    //        if(state==entryState){
    //             //System.out.println("ActionSequence.next : "+actionSequence.next); 
    //             return actionBlock;
    //        }
    //        else{
    //            return computeActionDefaultEntryForState(currentconfig,entryState);
    //         }
          
    //     }
    //     catch(Exception e){
    //         e.printStackTrace();
    //     }
    //     return actionBlock;
    // }
    public ArrayList<Transition> findTransitions( Configuration c, String e){
            
        String str="Finding the transitions suitable for event : "+e + " for configuration : "+c.getCurrentStatesName();
        
        System.out.println(str);
        ArrayList<Transition> allTransitionsInConfiguration=new ArrayList<Transition>();
        ArrayList<State> allSourceStatesInConfiguration=new ArrayList<State>();
        
        ArrayList<Transition> selectedTransitions=new ArrayList<Transition>();
        
        allSourceStatesInConfiguration.addAll(c.getCurrentStates());
        // adding all the superstates of the states in the current configuration
        for(State s:c.getCurrentStates()){
            //System.out.println("State : "+s);
            if(s.transitions!=null)
                allTransitionsInConfiguration.addAll(s.transitions);
                ArrayList<State> superstates=new ArrayList<State>();
                superstates.addAll(s.getAllSuperstates());
                allSourceStatesInConfiguration.addAll(superstates);  
            for(State sup:superstates){
                allTransitionsInConfiguration.addAll(sup.transitions);                    
            }
        }
        //System.out.println("Source states found : ");
        /*for(State s:allSourceStatesInConfiguration){
            System.out.println(s.name);
        }*/
        //System.out.println("All transitions found: "+allTransitionsInConfiguration);
        for(Transition t: allTransitionsInConfiguration){
            //System.out.println("source state : "+ t.getSource().name + "in "+ allSourceStatesInConfiguration);
            if(allSourceStatesInConfiguration.contains(t.getSource())&&t.trigger.equals(e))
            {
                if(!selectedTransitions.contains(t))
                    selectedTransitions.add(t);
            }
        }
        return selectedTransitions;
    }
    public void findNonDeterminism(List<Transition> activeTransitions){
         List<State> activeStates=new ArrayList<State>();
         for(Transition t:activeTransitions){
                //For each transition, identify its source and destination and compute the program
                //case 1 - two transitions are from a state and its ancestor
                //case 2 - two transitions are from different regions of a shell state
                activeStates.add(t.getSource());
            }

            //System.out.println("Active states are : "+activeStates);
            for(State s: activeStates){
                ArrayList<State> ancestorStates=new ArrayList<State>();
                ancestorStates.addAll(s.getAllSuperstates());

                ancestorStates.retainAll(activeStates);
                if(ancestorStates.size()!=0){
                    System.out.println("Non Determinism Detected between outgoing transitions of two states :" +s.name+" and ");
                    System.exit(0);
                }
                //if(checkStates.contains(s.getAllSuperstates()))
            }
    }
    public Configuration takeTransitions(Configuration currentconfig, List<Transition> tlist)
    {
        System.out.println("*********************************");
        
        System.out.print("Take Transition : ");
        for(Transition t:tlist)
            System.out.print(t.name+", ");
        //System.out.println();
        Configuration config=currentconfig;
        
        try{
            Transition t=tlist.get(0); // still concurrent transitions not handled
            System.out.println(" : Configuration : "+config.getCurrentStatesName());
            Connector con;
            if(tlist.size()>0)
                con=new Fork("start");
            else
                con=new Seq("start");
                //compute LUB-1 node to exit
                List<State> activestates=config.getCurrentStates();
                    State exitancesestor=null;
                    for(State s: (t.lub()).states){
                    // System.out.println("LUB :"+s.name);
                        for(State as:activestates){
                            if(as.getAllSuperstates().contains(s))
                                exitancesestor=s;
                        }
                    }
                //Compute ExitCFAList
                State exState=exitState(currentconfig,exitancesestor);
                //Compute transitionCFAList
                System.out.println("Looking for cfa node : "+exState.name+"_X");
                CFA prevcfa=getCFAfromList(exState.name+"_X");
               // Seq sqbegin=getSeqfromList(t.name+"_B");
               // sqbegin.addPrev(cfa);
               // Seq sqend=getSeqfromList(t.name+"_B");
                
                CFA transitionCFA=computeTransitionCFAList(prevcfa,t);

                //computing the entry. finding the state that has to be recursively
                System.out.println("Computing enter state : ");
                State enterancestor=null;
                State dest=t.getDestination();
                State LUB=t.lub();
                for(State s : LUB.states){
                    if(dest==s||(dest.getAllSuperstates()).contains(s)){
                       
                        enterancestor=s;
                        break;
                    }
                }
                if(enterancestor!=null)
                System.out.println("Enter the state : "+enterancestor.getFullName());
                enterState(transitionCFA,enterancestor, dest);
                System.out.println(execnodelist);
        //            List<State> entrystatelist=computeEntryCFAList(t.lub(),t.getDestination());
                //System.out.println("Entry states :");
                //for(State s : entrystatelist){
                //    System.out.print(s.name+", ");
                //}
                //ExecutionBlock exitBlock=computeExitExecutionBlock(currentconfig,t.lub());
                //System.out.println ("Exit Execution Block : "+exitBlock);
                //Compute TransitionBlock -- Done
                //ExecutionBlock transitionActionBlock=computeTransitionAction(t);
                //System.out.println ("Transition Action Sequence : "+transitionActionBlock);
                //Compute EntryActionBlock
                 //ExecutionBlock entryBlock=computeEntryExecutionBlock(new SequentialExecutionBlock(),currentconfig,t.lub(), t.getDestination());
                // System.out.println ("Entry Execution Block : "+entryBlock);
                 
                 //ConstaBLActionExecutor.executeAction(exitBlock,null);
                
            
        }
        catch(Exception e){
            e.printStackTrace();
        }
        System.out.println("Return config : "+config);
        return currentconfig;
    }
    public void enterDefaultState(CFA prev, State s){
        CFA cfa=getCFAfromList(s.getFullName()+"_N");
        if(prev!=null)
        {
            cfa.addPrev(prev);
        }
        else{
            State sup=s.getSuperstate();
            cfa.addPrev(getCFAfromList(sup.getFullName()+"_N"));
        }
        execnodelist.add(cfa);
        if((s.states).size()>0 && s instanceof ast.Shell){
            for(State e:s.states){
                System.out.println(e.getFullName());
                enterDefaultState(null,e);
            }
            
        }
        else if((s.states).size()>0){
            State e=(s.states).get(0);
            System.out.println(e.getFullName());
            enterDefaultState(null,e);
        }
        else{
            System.out.println(s.getFullName());
        }
    }
    public void enterState(CFA prev, State enstate, State destination){
        
        System.out.println(enstate.getFullName());
        CFA cfa=getCFAfromList(enstate.getFullName()+"_N");
        if(prev!=null)
        {
            cfa.addPrev(prev);
        }
        else{
            State s=enstate.getSuperstate();
            cfa.addPrev(getCFAfromList(s.getFullName()+"_N"));
        }
        execnodelist.add(cfa);
        if(enstate==destination){            
            if(destination.getSuperstate() instanceof ast.Shell){
                State shellstate=destination.getSuperstate();
                for(State s:shellstate.states){
                    enterDefaultState(getCFAfromList(shellstate.getFullName()+"_N"), (destination.states).get(0));
                }
            }
            else if((destination.states).size()>0){
                enterDefaultState(getCFAfromList(destination.getFullName()+"_N"), (destination.states).get(0));
            }
        }
        else{
            State newEnterState=null;
            for(State s:enstate.states){
                //System.out.println("substate of entrystate :"+s.name);
                List<State> superstates=destination.getAllSuperstates();
                if(superstates.contains(s)||s==destination){
                    newEnterState=s;
                    break;
                }
            }
            
                enterState(getCFAfromList(enstate.getFullName()+"_N"),newEnterState, destination);
        }
        /*for(State s:enstate.states){
            if(s==destination){
                System.out.println(s.getFullName());
                CFA cfa1=getCFAfromList(enstate.getFullName()+"_N");
                cfa1.addPrev(prev);
                execnodelist.add(cfa);
                

            } 
            else if((destination.getAllSuperstates()).contains(s)){
                System.out.println(s.getFullName());

                enterState(cfa,s,destination);
                break;
            }
        }*/
    }
    public CFA computeTransitionCFAList(CFA prev, Transition t){
            CFA cfa=StatementToCFA.convertToCFA(t.action,t.name+"_A");
            cfa.addPrev(prev);
            //seqend.addPrev(cfa);
            //cfalist.add(cfa);
            //seqlist.add(seq);
            //codenodelist.add(cfa);
            //codenodelist.add(seqbegin);
            //codenodelist.add(seqend);
            execnodelist.add(cfa);
            return cfa;
    }

    public List<State> computeEntryCFAList(State LUB, State destination){
        System.out.println("computeEntryCFAList");
        List<State> entryStates=new ArrayList<State>();

        for(State s: LUB.states){
             System.out.println("LUB :"+s.name);
            if(destination.getAllSuperstates().contains(s)){
                entryStates.add(s);
                getEntryStateList(entryStates,s,destination);
            }
            else if(s==destination){
                entryStates.add(s);
            }
             /*for(State as:activestates){
                 if(as.getAllSuperstates().contains(s))
                     exitancesestor=s;
             }*/
         }
         return entryStates;
    }
    public void getEntryStateList(List<State> entryStateList, State entry, State destination){
        System.out.println("entry : "+entry.name);
        //if(destination.getAllSubstates().contains())
    }


    public State exitState(Configuration config, State exitancesestor){
        if(exitancesestor!=null)
            System.out.println("Exit Ancestor LUB-1 :"+exitancesestor.name);
        // if(exitancesestor instanceof ast.Shell){
        //     CFA cfa=getCFAfromList(exitancesestor.getFullName()+"_X");
        //     for(State s:exitancesestor.states){
        //         CFA scfa=getCFAfromList(s.getFullName()+"_X");
        //         cfa.addPrev(scfa);
        //         execnodelist.add(scfa);
        //     }
        //     execnodelist.add(cfa);
        // }
        List<State> activestates=config.getCurrentStates();
        if(activestates.size()>1){
            Fork f=new Fork("start");
            State shellancestor=getShellAncestor(activestates.get(0));
            for(State s : activestates){
                
                computeSequenceUntilLUB(s, shellancestor);
                
            }
            Join j=getJoinfromList(shellancestor.name);
            for(State s : shellancestor.states)
                j.addPrev(getCFAfromList(s.name+"_X"));
            // This is not completed    
        }
        else{
            //single state to exit - 
            //is it possible to exit a shell state when number of program points is 1? - No
            //contained within OR state, so computeSequenceUntilLUB can be used to find a sequence?
            State s=activestates.get(0);
            computeSequenceUntilLUB(s, exitancesestor);
            //System.out.println(es);            
        }
    
        return exitancesestor;
     }

    public State computeExitCFAList(Configuration config, State LUB){
    
      

        List<State> activestates=config.getCurrentStates();
        
        State exitancesestor=null;
        for(State s: LUB.states){
           // System.out.println("LUB :"+s.name);
            for(State as:activestates){
                if(as.getAllSuperstates().contains(s))
                    exitancesestor=s;
            }
        }
        if(exitancesestor!=null)
            System.out.println("Exit Ancestor LUB-1 :"+exitancesestor.name);

        if(activestates.size()>1){
            Fork f=new Fork("start");
            State shellancestor=getShellAncestor(activestates.get(0));
            for(State s : activestates){
                
                computeSequenceUntilLUB(s, shellancestor);
                
            }
            Join j=getJoinfromList(shellancestor.name);
            for(State s : shellancestor.states)
                j.addPrev(getCFAfromList(s.name+"_X"));
            // This is not completed    
        }
        else{
            //single state to exit - 
            //is it possible to exit a shell state when number of program points is 1? - No
            //contained within OR state, so computeSequenceUntilLUB can be used to find a sequence?
            State s=activestates.get(0);
            computeSequenceUntilLUB(s, LUB);
            //System.out.println(es);            
        }
    
        return exitancesestor;
     }
     public void computeSequenceUntilLUB(State s, State lub){
        //System.out.println("State : "+s.name);
        //System.out.println("lub : "+lub.name);
        
        if(s == lub || s==null)
            return;
        else{
            Seq seq=new Seq(s.name);
            seq.addPrev(getCFAfromList(s.name+"_X"));
            CFA cfa=StatementToCFA.convertToCFA(s.exit,s.name+"_X");
            cfa.addPrev(seq);
            //cfalist.add(cfa);
            //seqlist.add(seq);
            codenodelist.add(cfa);
            codenodelist.add(seq);
           // ses=(SequentialExecutionBlock)addProgramPoints(ses,s,null, ActionType.STATE_EXIT_ACTION);           
            computeSequenceUntilLUB(s.getSuperstate(),lub);
        }

    }
}
