package org.pubinv.numberspectra.expr;

import org.pubinv.numberspectra.Rational;

public final class Times extends BinaryOp {
	private Times(Expr lhs, Expr rhs) {
		super(lhs, rhs);
	}

	public static Expr make(Expr lhs, Expr rhs) {
		if (lhs.equals(Const.NAN)) return lhs;
		if (rhs.equals(Const.NAN)) return rhs;
		
		// 0 * rhs = 0
		if (lhs.equals(Const.ZERO)) return Const.ZERO;
		// 1 * rhs = rhs
		if (lhs.equals(Const.ONE)) return rhs;
		
		// lhs * 0 = 0
		if (rhs.equals(Const.ZERO)) return Const.ZERO;
		// lhs * 1 = 0
		if (rhs.equals(Const.ONE)) return lhs;
		
		// (-A * B) = -(A * B)
		if (lhs.isNegatable()) {
			return Negate.make(make(lhs.negate(), rhs));
		}
		// (A * -B) = -(A * B)
		if (rhs.isNegatable()) {
			return Negate.make(make(lhs, rhs.negate()));
		}
		
		// A * (B * C) = (A * B) * C
		if (rhs instanceof Times) {
			Times rtimes = (Times) rhs;
			return make(make(lhs, rtimes.lhs), rtimes.rhs);
		} else if (rhs instanceof Const) {
			Rational rr = ((Const) rhs).rational;

			// K * K
			if (lhs instanceof Const) {
				return new Const(((Const) lhs).rational.multiply(rr));
			} else if (lhs instanceof Times) {
				Times timesLhs = (Times) lhs;
				// (K * e) * K
				if (timesLhs.lhs instanceof Const) {
					return make(timesLhs.rhs, new Const(((Const) (timesLhs.lhs)).rational.multiply(rr)));
				}
				// (e * K) * K
				if (timesLhs.rhs instanceof Const) {
					return make(timesLhs.lhs, new Const(((Const) (timesLhs.rhs)).rational.multiply(rr)));
				}
			}
		}
		
		// (A + B) * C = A * C + B * C
		if (lhs instanceof Plus) {
			Plus plusLhs = (Plus) lhs;
			return Plus.make(Times.make(plusLhs.lhs, rhs), Times.make(plusLhs.rhs, rhs));
		}
		// A * (B + C) = A * B + A * C
		if (rhs instanceof Plus) {
			Plus plusRhs = (Plus) rhs;
			return Plus.make(Times.make(lhs, plusRhs.lhs), Times.make(lhs, plusRhs.rhs));
		}
		if (lhs instanceof Const && !(rhs instanceof Const)) {
			return new Times(rhs, lhs);
		}
		return new Times(lhs, rhs);
	}

	@Override
	public String toString() {
		return "(* " + lhs + " " + rhs + ")";
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
		return lhs.eval() * rhs.eval();
	}
}
