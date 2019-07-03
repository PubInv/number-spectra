package org.pubinv.numberspectra.expr;

public abstract class BinaryOp extends BaseExpr {
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + lhs.hashCode();
		result = prime * result + rhs.hashCode();
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if(!super.equals(obj)) {
			return false;
		}
		BinaryOp other = (BinaryOp) obj;
		return lhs.equals(other.lhs) && rhs.equals(other.rhs);
	}
	final Expr lhs;
	final Expr rhs;
	protected BinaryOp(Expr lhs, Expr rhs) {
		super();
		this.lhs = lhs;
		this.rhs = rhs;
	}
}
