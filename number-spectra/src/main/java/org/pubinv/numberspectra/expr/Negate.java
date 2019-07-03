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
