package symbolic_execution;

import ast.*;
import symbolic_execution.se_tree.*;
import java.util.List;
import java.util.ArrayList;

public class SymbolicExecutionResult{
    
    private  ArrayList<SETNode> done;
    private  ArrayList<SETNode> live;

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
        return this.done;
    }

    public ArrayList<SETNode> getLiveNodes()
    {
        return this.live;
    }
}
