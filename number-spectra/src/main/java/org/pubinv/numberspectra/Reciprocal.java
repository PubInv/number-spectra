package org.pubinv.numberspectra;

public class Reciprocal implements Expr {
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((operand == null) ? 0 : operand.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Reciprocal other = (Reciprocal) obj;
		if (operand == null) {
			if (other.operand != null)
				return false;
		} else if (!operand.equals(other.operand))
			return false;
		return true;
	}

	final Expr operand;

	private Reciprocal(Expr operand) {
		super();
		this.operand = operand;
	}
	
	public static Expr make(Expr e) {
		if (e instanceof Reciprocal) {
			return ((Reciprocal)e).operand;
		} else if (e instanceof Const) {
			return new Const(((Const) e).rational.reciprocal());
		} else if (e.isNegatable()) {
			return Negate.make(make(e.negate()));
		}
		return new Reciprocal(e);
	}
	
	@Override
	public String toString() {
		return "(/ 1 " + operand + ")";
	}
	
	@Override
	public boolean isNegatable() {
		return operand.isNegatable();
	}
	
	@Override
	public Expr negate() {
		return make(operand.negate());
	}
	
	@Override
	public double eval() {
		return 1 / operand.eval();
	}
}