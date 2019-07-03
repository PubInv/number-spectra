package org.pubinv.numberspectra;

public abstract class UnaryOp implements Expr {
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
		UnaryOp other = (UnaryOp) obj;
		return operand.equals(other.operand);
	}

	final Expr operand;

	protected UnaryOp(Expr operand) {
		super();
		this.operand = operand;
	}
}
