package org.pubinv.numberspectra.expr;

import java.math.BigInteger;

import org.pubinv.numberspectra.Rational;

public final class Power extends BinaryOp {
	private Power(Expr lhs, Expr rhs) {
		super(lhs, rhs, true);
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
		
		// 1 ^ B = 1
		if (lhs.equals(Const.ONE)) return Const.ONE;
		
		// B ^ 1 = B
		if (rhs.equals(Const.ONE)) return lhs;
		
		// (A * B) ^ C = (A^C * B^C)
		if (lhs instanceof Times) {
			Times timesLhs = (Times) lhs;
			return Times.make(make(timesLhs.lhs, rhs), make(timesLhs.rhs, rhs));
		}
		// (A ^ B) ^ C = A ^ (B * C)
		if (lhs instanceof Power) {
			Power powLhs = (Power) lhs;
			return make(powLhs.lhs, Times.make(powLhs.rhs, rhs));
		}
		// A ^ (B + C) = A^B * A^C
		if (rhs instanceof Plus) {
			Plus plusRhs = (Plus) rhs;
			return Times.make(make(lhs,plusRhs.lhs),make(lhs,plusRhs.rhs));
		}
		
		if (rhs instanceof Const) {
			Const rhsConst = (Const) rhs;
			Rational rr = rhsConst.rational;
			
			// X ^ (A / B) = (X ^ A) ^ (1 / B)
			if (!rr.isInteger()) {
				if (!rr.p.equals(BigInteger.ONE) && lhs instanceof Const) {
					Expr pp = new Const((((Const) lhs).rational).pow(rr.p));
					return make(pp, new Const(Rational.of(BigInteger.ONE, rr.q)));
				}
			} else if (lhs instanceof Const) {
				return new Const((((Const) lhs).rational).pow(rr.p));
			}
		}
				
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
