package org.pubinv.numberspectra.expr;

import org.pubinv.numberspectra.Rational;

public final class Power extends BinaryOp {
	private Power(Expr lhs, Expr rhs) {
		super(lhs, rhs);
	}
	
	public static Expr make(Expr lhs, Expr rhs) {
		if (lhs.equals(Const.NAN)) return lhs;
		if (rhs.equals(Const.NAN)) return rhs;
		
		// A ^ 1 = A
		if (rhs.equals(Const.ONE)) return lhs;
		// A ^ 0 = 1
		if (rhs.equals(Const.ZERO)) return Const.ONE;
		
		// A ^ (-B)  = 1 / (A ^ B)
		if (rhs.isNegatable()) {
			return Reciprocal.make(make(lhs, rhs.negate()));
		}
		
		if (lhs instanceof Const && rhs instanceof Const) {
			Const lhsConst = (Const) lhs;
			Const rhsConst = (Const) rhs;
			Rational rl = lhsConst.rational;
			Rational rr = rhsConst.rational;
			if (rr.isInteger() && rr.compareTo(Rational.of(10)) < 0) {
				int xp = rr.intValue();
				return new Const(Rational.of(rl.p.pow(xp), rl.q.pow(xp)));
			}
		}
		
		// 1 ^ B = 1
		if (lhs.equals(Const.ONE)) return lhs;
		
		if (rhs.equals(Const.NEGATIVE_INFINITY) || rhs.equals(Const.PLUS_INFINITY)) {
			return rhs;
		}
		if (lhs.equals(Const.NEGATIVE_INFINITY) || lhs.equals(Const.PLUS_INFINITY)) {
			return lhs;
		}
		
		return new Power(lhs, rhs);
	}
	

	
	@Override
	public String toString() {
		return "(** "+lhs+" "+rhs+")";
	}
	
	public static void main(String[] args) {
		System.out.println(Math.pow(0.0, -1));
	}
	
	@Override
	public boolean isNegatable() {
		return false;
	}
	
	@Override
	public Expr negate() {
		return Negate.make(this);
	}
	
	@Override
	public double eval() {
		return Math.pow(lhs.eval(), rhs.eval());
	}
}
