package symbolic_execution;

import ast.*;
import symbolic_execution.se_tree.*;
import java.util.List;
import java.util.ArrayList;

public class SymbolicExecutionResult{
    
    public  ArrayList<SETNode> done; //can be List
    public  ArrayList<SETNode> live;

    public SymbolicExecutionResult()
    {
        done=new ArrayList<SETNode>();
		live=new ArrayList<SETNode>();
    }
	public void setDoneNodes(ArrayList<SETNode> setNodes){
		this.done.addAll(setNodes);
	}
	public void setLiveNodes(ArrayList<SETNode> setNodes){
		this.live.addAll(setNodes);
	}
    public ArrayList<SETNode> getDoneNodes()
    {
        return (ArrayList)this.done;
    }

    public ArrayList<SETNode> getLiveNodes()
    {
        return (ArrayList)this.live;
    }
}
