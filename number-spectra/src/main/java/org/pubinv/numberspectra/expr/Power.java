package org.pubinv.numberspectra.expr;

import java.math.BigInteger;

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
			
			// X ^ (A / 1) = X ^ A
			// X ^ (A / B) = (X ^ A) ^ (1 / B)
			{
				Rational pp = rl.pow(rr.p);
				if (rr.q.equals(BigInteger.ONE)) {
					return new Const(pp);
				} else {
					return new Power(new Const(pp), new Const(Rational.of(BigInteger.ONE, rr.q)));
				}
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
		
		if (lhs instanceof Times) {
			Times timesLhs = (Times) lhs;
			return Times.make(make(timesLhs.lhs, rhs), make(timesLhs.rhs, rhs));
		}
		if (lhs instanceof Power) {
			Power powLhs = (Power) lhs;
			return make(powLhs.lhs, Times.make(powLhs.rhs, rhs));
		}
		if (rhs instanceof Plus) {
			Plus plusRhs = (Plus) rhs;
			return Times.make(make(lhs,plusRhs.lhs),make(lhs,plusRhs.rhs));
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
