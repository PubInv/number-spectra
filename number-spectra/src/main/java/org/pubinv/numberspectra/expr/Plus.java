package org.pubinv.numberspectra.expr;

import org.pubinv.numberspectra.Rational;

public final class Plus extends BinaryOp {
	private Plus(Expr lhs, Expr rhs) {
		super(lhs, rhs, false);
	}
	
	public static Expr make(Expr lhs, Expr rhs) {
		if (lhs.equals(Const.NAN)) return lhs;
		if (rhs.equals(Const.NAN)) return rhs;
		
		// 0 + rhs = rhs
		if (lhs.equals(Const.ZERO)) return rhs;
		
		// lhs + 0 = lhs
		if (rhs.equals(Const.ZERO)) return lhs;
		
		// A + (B + C) = (A + B) + C
		if (rhs instanceof Plus) {
			Plus rplus = (Plus) rhs;
			return make(make(lhs, rplus.lhs), rplus.rhs);
		} else if (rhs instanceof Const) {
			Rational rr = ((Const) rhs).rational;
			// K + K
			if (lhs instanceof Const) {
				return new Const(((Const) lhs).rational.add(rr));
			} else if (lhs instanceof Plus) {
				Plus plusLhs = (Plus) lhs;
				// (K + e) + K
				if (plusLhs.lhs instanceof Const) {
					return make(plusLhs.rhs, new Const(((Const) (plusLhs.lhs)).rational.add(rr)));
				}
				// (e + K) + K
				if (plusLhs.rhs instanceof Const) {
					return make(plusLhs.lhs, new Const(((Const) (plusLhs.rhs)).rational.add(rr)));
				}
			}
		}
		return new Plus(lhs, rhs);
	}
	
	@Override
	public String toString() {		
		if (rhs.isNegatable()) {
			return "(- "+lhs+" "+rhs.negate()+")";
		} else if (lhs.isNegatable()) {
			return "(- "+rhs+" "+lhs.negate()+")";
		} else {
		    return "(+ "+lhs+" "+rhs+")";
		}
	}
	
	@Override
	public boolean isNegatable() {
		return lhs.isNegatable() && rhs.isNegatable();
	}
	
	@Override
	public Expr reciprocate() {
		return Reciprocal.make(this);
	}
	
	@Override
	public boolean isReciprocatable() {
		return false;
	}
	
	@Override
	public Expr negate() {
		return make(lhs.negate(), rhs.negate());
	}
	
	@Override
	public double eval() {
		return lhs.eval() + rhs.eval();
	}
}
