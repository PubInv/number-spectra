package org.pubinv.numberspectra.expr;

public final class Negate extends UnaryOp {
	private Negate(Expr operand) {
		super(operand);
	}
	
	public static Expr make(Expr e) {
		// -(-e) = e
		if (e.isNegatable()) {
			return e.negate();
		} else if (e instanceof Const) {
			return new Const(((Const) e).rational.negate());
		// -(a + b) = -a - b
		} else if (e instanceof Plus) {
			return Plus.make(((Plus) e).lhs.negate(), ((Plus) e).rhs.negate());
		// -(a * b) = -a * b
		} else if (e instanceof Times) {
			return Times.make(((Times) e).lhs.negate(), ((Times) e).rhs);
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
