package flt;


import java.util.List;
import java.util.ArrayList;
import java.util.ListIterator;

import ast.State;
import ast.Statechart;
import ast.Transition;
import ast.Name;
import ast.StatementList;
import ast.Statement;

import ast.Environment;
import ast.Declaration;
import ast.DeclarationList;



public class Flatten {

   public Flatten(){}

   public List<State> allStates = new ArrayList<>();
   public List<State> leaves = new ArrayList<>();
   public List<Transition> allTransitions = new ArrayList<>();
   public State newStartState;

   public Boolean isLeaf(State s){
    return s.states.isEmpty();
   }


   public void getStates(State s){
    allStates.add(s);
    if (s.states.isEmpty()){
        return;
    }
    else{
        for (State s1: s.states){
            getStates(s1);

        }
    }
    allStates.add(s);
   }

  public void getLeaves(){
      for (State s: this.allStates){
          if(isLeaf(s)){
              leaves.add(s);
          }
      }
  }

  public void getTransitions(State s){

      for (Transition t: s.transitions){
          if(!allTransitions.contains(t)) {
              allTransitions.add(t);
          }


      }
      if (s.states.isEmpty()) {
          return ;
      }
      else{
          for(State s1: s.states){
              getTransitions(s1);
          }
      }
  }




  public void getStartState(State s){
      if (s.states.isEmpty()){
          newStartState = s;
      }
      else{
          getStartState(s.states.get(0));
      }

  }









   public StatementList getExitList(State src, State commonAncestor, State currentState, StatementList exitList){
       if (commonAncestor.equals(src)) {
           //exitList.add(src.exit);
           return exitList;
       }
       else if (!currentState.equals(commonAncestor)){
           exitList.add(currentState.exit);
           getExitList(src, commonAncestor, currentState.getSuperstate(), exitList);
       }

       else if (currentState.equals(commonAncestor)){
           return exitList;
       }



    return exitList;
   }

   public StatementList getEntryList(State dest, State commonAncestor, State currentState, StatementList entryList){
       if (commonAncestor.equals(dest)) {
           return entryList;
       }

       else if(!currentState.equals(dest)){
           entryList.add(currentState.entry);
           for (State s : currentState.states){
               if (s.isAncestor(dest)){
                   getEntryList(dest, commonAncestor, s, entryList);
               }
           }
       }
       else if (currentState.equals(dest)){
           entryList.add(dest.entry);
           return entryList;
       }

       return entryList;

   }

  public StatementList getNewAction(StatementList exitOrder, Statement tAction, StatementList entryOrder){
        StatementList newAction = new StatementList();
        for (Statement s : exitOrder.getStatements()){
            newAction.add(s);
        }

        newAction.add(tAction);

        for (Statement s : entryOrder.getStatements()){
            newAction.add(s);
        }
        return newAction;
    }

   public State getLowest(List<State> commonAncestors){
       for (State s1 :commonAncestors){
           boolean flag = true;
           for(State s2: commonAncestors){
               if(!s1.equals(s2) && s2.isAncestor(s1)){
                   flag = flag && true;
               }
               else if (s1.equals(s2)) continue;
               else if (!s1.equals(s2) && !s2.isAncestor(s1)) flag = false;
               else flag = false;
           }
           if (flag == true) return s1;

       }

       return new State("srry, wrong number!", null, null, null, null, null);
   }




   public State getLowestCommonAncestor(State s1, State s2){
       List<State> superStatesList_1 = s1.getAllSuperstates();
       List<State> superStatesList_2 = s2.getAllSuperstates();
       List<State> commonAncestors = new ArrayList<>();
       for (State s: superStatesList_1){
           if (superStatesList_2.contains(s)){
               commonAncestors.add(s);
               //System.out.println("Common Ancestor --- " + s.name);
           }
       }


       if (commonAncestors.isEmpty()) {
           System.out.println("no common ancestor found!");
           return s1;
       }

       else if(commonAncestors.size() == 1){
           return commonAncestors.get(0);
       }



       else{
           return getLowest(commonAncestors);
       }


    }










    public List<Transition> getFlatTransitions (){

        int transitionCount = 0;
        List<Transition> flatTransitions = new ArrayList<>();
        List<String> visitedTransitions = new ArrayList<>();
        for (Transition t: allTransitions){
            State src = t.getSource();
            State dest = t.getDestination();
            if(isLeaf(src) && isLeaf(dest)){
                State commonAncestor = getLowestCommonAncestor(src, dest);

                StatementList exitOrder = getExitList(src, commonAncestor, src, new StatementList());
                StatementList entryOrder = getEntryList(dest, commonAncestor, commonAncestor, new StatementList());
                StatementList newAction = getNewAction(exitOrder, t.action, entryOrder);
                String flatname = "flattransition-" + src.name + " to " + dest.name ;
                if(!visitedTransitions.contains(flatname)){
                    Transition t_1 = new Transition(flatname, new Name(src.name), new Name(dest.name), t.trigger, t.guard, newAction);
                    flatTransitions.add(t_1);
                    visitedTransitions.add(flatname);
                }
            }
            else if(!isLeaf(src) && isLeaf(dest)){
                State commonAncestor = getLowestCommonAncestor(src, dest);
                for (State l : leaves){
                    if (src.isAncestor(l)){
                        StatementList exitOrder = getExitList(l, commonAncestor, l, new StatementList());
                        StatementList entryOrder = getEntryList(dest, commonAncestor, commonAncestor, new StatementList());
                        StatementList newAction = getNewAction(exitOrder, t.action, entryOrder);
                        String flatname = "flattransition-" + l.name + " to " + dest.name ;
                        if(!visitedTransitions.contains(flatname)){
                            Transition t_1 = new Transition(flatname, new Name(l.name), new Name(dest.name), t.trigger, t.guard, newAction);
                            flatTransitions.add(t_1);
                            visitedTransitions.add(flatname);
                        }
                    }
                }
            }

            else if(isLeaf(src) && !isLeaf(dest)){
                State commonAncestor = getLowestCommonAncestor(src, dest);
                State l = dest.states.get(0);

                StatementList exitOrder = getExitList(src, commonAncestor, src, new StatementList());
                StatementList entryOrder = getEntryList(l, commonAncestor, commonAncestor, new StatementList());
                StatementList newAction = getNewAction(exitOrder, t.action, entryOrder);
                String flatname = "flattransition-" + src.name + " to " + l.name ;
                if(!visitedTransitions.contains(flatname)){
                    Transition t_1 = new Transition(flatname, new Name(src.name), new Name(l.name), t.trigger, t.guard, newAction);
                    flatTransitions.add(t_1);
                    visitedTransitions.add(flatname);
                }

            }

            else if(!isLeaf(src) && !isLeaf(dest)){
                State commonAncestor = getLowestCommonAncestor(src, dest);
                State l2 = dest.states.get(0);
                for(State l1 : leaves){

                    if (src.isAncestor(l1)){
                        StatementList exitOrder = getExitList(l1, commonAncestor, l1, new StatementList());
                        StatementList entryOrder = getEntryList(l2, commonAncestor, commonAncestor, new StatementList());
                        StatementList newAction = getNewAction(exitOrder, t.action, entryOrder);
                        String flatname = "flattransition-" + l1.name + " to " + l2.name ;
                        if(!visitedTransitions.contains(flatname)){
                            Transition t_1 = new Transition(flatname, new Name(l1.name), new Name(l2.name), t.trigger, t.guard, newAction);
                            flatTransitions.add(t_1);
                            visitedTransitions.add(flatname);
                        }

                    }

                }
            }

        }





        return flatTransitions;

    }






    public Statechart flatten(Statechart s){



        for (State s1 : s.states){
            getStates(s1);
        }


        getLeaves();
        getTransitions(s);

        List<State> newStates = new ArrayList<>();
        for (State s1 : leaves){
            newStates.add(new State(s1.name, s.declarations, new StatementList(), new StatementList(), new ArrayList<State>(), new ArrayList<Transition>()));
        }

        DeclarationList fltDecl = new DeclarationList();
        List<Transition> flatTransitions = getFlatTransitions();
        List<State> start = new ArrayList<>();
        start.add(newStartState);
        try{
            Statechart FlatStatechart = new Statechart(s.name, s.types, s.events, s.declarations, null, null, s.functionDeclarations, newStates, flatTransitions);

            for (Transition t: flatTransitions){

                t.setStatechart(FlatStatechart);
                t.setSourceDestinationStates();

            }
            /*
            System.out.println("Flat State Chart - \n");

            System.out.println(FlatStatechart.toString());
            */
            return FlatStatechart;

        }






        catch(Exception e){
            System.out.println(e);
        }

        return s;

    }




}
