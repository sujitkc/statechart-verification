package set;

public class SETNode{
    protected final SETNode parent;
	protected int depth;

	public SETNode (SETNode parent) {
		this.parent = parent;
		if (parent == null) {
			this.depth = 0;
		}
		else {
			this.depth = parent.depth + 1;
		}
	}

    public SETNode getParent() {return this.parent;}
	public int getDepth () {return this.depth;}
}

