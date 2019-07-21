package org.pubinv.numberspectra.expr;

public final class Reciprocal extends UnaryOp {
	private Reciprocal(Expr operand) {
		super(operand);
	}
	
	public static Expr make(Expr e) {
		// 1 / (1 / A) = A
		if (e instanceof Reciprocal) {
			return ((Reciprocal)e).operand;
		} else if (e instanceof Const) {
			return new Const(((Const) e).rational.reciprocal());
	    // 1 / -A = -(1 / A)
		} else if (e.isNegatable()) {
			return Negate.make(make(e.negate()));
		// 1 / (A * B) = 1/A * 1/B
		} else if (e instanceof Times) {
			return Times.make(Reciprocal.make(((Times) e).lhs),Reciprocal.make(((Times) e).rhs));
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
