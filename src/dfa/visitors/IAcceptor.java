package visitors;

public interface IAcceptor {
	public void accept(IExprVisitor<?> visitor) throws Exception;
}
