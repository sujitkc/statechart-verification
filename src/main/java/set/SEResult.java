package set;

import java.util.ArrayList;

public class SEResult {
	private ArrayList<SETNode> _live, _done;

	public SEResult(ArrayList<SETNode> live, ArrayList<SETNode> done) {
		this._live = live;
		this._done = done;
	}

	public SEResult() {
		this._live = new ArrayList<>();
		this._done = new ArrayList<>();
	}

	public void addToLive(SETNode node) {
		this._live.add(node);
	}

	public void addToDone (SETNode node) {
		this._done.add(node);
	}

	public SEResult merge (SEResult result) {
		ArrayList<SETNode> live = new ArrayList<>();
		ArrayList<SETNode> done = new ArrayList<>();
		live.addAll(this._live);
		live.addAll (result._live);
		done.addAll(this._done);
		done.addAll (result._done);

		return new SEResult(live, done);
	}

	public ArrayList<SETNode> getLive() {return _live;}
	public ArrayList<SETNode> getDone() {return _done;}
}
