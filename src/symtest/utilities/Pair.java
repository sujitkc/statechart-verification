package utilities;

public class Pair<T1, T2> implements IPair<T1, T2>{
	private T1 mFirst;
	private T2 mSecond;
	
	public Pair(T1 first, T2 second) {
		this.mFirst = first;
		this.mSecond = second;
	}
	
	public T1 getFirst() {
		return this.mFirst;
	}
	
	public T2 getSecond() {
		return this.mSecond;
	}

	@Override
	public boolean equals(T1 first, T2 second) {
		return (first.equals(this.mFirst) && second.equals(this.mSecond)); 
	}

}
