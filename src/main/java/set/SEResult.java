package set;

import java.util.ArrayList;

public class SEResult {
	private ArrayList<SETNode> _live, _done;

	public SEResult(ArrayList<SETNode> live, ArrayList<SETNode> done) {
		this._live = live;
		this._done = done;
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

	public SETNode getLastestLeaf() {
		SETNode res = null;
		if (this._live != null && this._live.size() > 0) {
			res = this._live.get(this._live.size() - 1);
		}
		return res;
	}
}
