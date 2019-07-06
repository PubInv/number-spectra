package org.pubinv.numberspectra.expr;

public abstract class BinaryOp extends BaseExpr {
	@Override
	public int hashCode() {
		return lhs.hashCode() + rhs.hashCode() ;
	}
	@Override
	public boolean equals(Object obj) {
		if(!super.equals(obj)) {
			return false;
		}
		BinaryOp other = (BinaryOp) obj;
		if (ordered) {
			return lhs.equals(other.lhs) && rhs.equals(other.rhs);
		} else {
			return
					lhs.equals(other.lhs) && rhs.equals(other.rhs) ||
					lhs.equals(other.rhs) && rhs.equals(other.lhs);
		}
	}
	final Expr lhs;
	final Expr rhs;
	final boolean ordered;
	protected BinaryOp(Expr lhs, Expr rhs, boolean ordered) {
		super();
		this.lhs = lhs;
		this.rhs = rhs;
		this.ordered = ordered;
	}
}
