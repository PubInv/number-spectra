package org.pubinv.numberspectra;

public final class Negate implements Expr {
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + operand.hashCode();
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
		Negate other = (Negate) obj;
		return operand.equals(other.operand);
	}

	final Expr operand;

	private Negate(Expr operand) {
		super();
		this.operand = operand;
	}
	
	public static Expr make(Expr e) {
		// -(-e) = e
		if (e.isNegatable()) {
			return e.negate();
		} else if (e instanceof Const) {
			return new Const(((Const) e).rational.negate());
		}
		return new Negate(e);
	}
	
	@Override
	public String toString() {
		return "(- " + operand + ")";
	}
	
	@Override
	public boolean isNegatable() {
		return true;
	}
	
	@Override
	public Expr negate() {
		return operand;
	}
	
	@Override
	public double eval() {
		return -operand.eval();
	}
}
