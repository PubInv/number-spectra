package org.pubinv.numberspectra;

public final class Plus extends BinaryOp {
	private Plus(Expr lhs, Expr rhs) {
		super(lhs, rhs);
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
		return "(+ "+lhs+" "+rhs+")";
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
		return lhs.eval() + rhs.eval();
	}
}
