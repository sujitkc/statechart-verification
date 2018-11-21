package utilities;

public interface IPair<T1, T2> {
	public T1 getFirst();
	
	public T2 getSecond();
	
	public boolean equals(T1 first, T2 second);
}
