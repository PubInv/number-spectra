package org.pubinv.numberspectra.expr;

import org.pubinv.numberspectra.Rational;

public final class Power extends BinaryOp {
	Power(Expr lhs, Expr rhs) {
		super(lhs, rhs, true);
	}
	
	@Override
	public Rational evalConst() {
		Rational l = lhs.evalConst();
		Rational r = rhs.evalConst();
		
		if (Rational.NAN.equals(l)) return l;
		if (Rational.NAN.equals(r)) return r;
		
		// A ^ 1 = A
		if (Rational.ONE.equals(r)) return l;
		// A ^ 0 = 1
		if (Rational.ZERO.equals(r)) return Rational.ONE;
		
		// 0 ^ B = 0
		if (Rational.ZERO.equals(l)) return Rational.ZERO;
		
		// 1 ^ B = 1
		if (Rational.ONE.equals(l)) return Rational.ONE;
		
		if (l != null && r != null && r.isInteger()) {
			return l.pow(r.p);
		}
				
		return null;
	}
	
	public static Expr make(Expr lhs, Expr rhs) {				
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
	public double eval() {
		return Math.pow(lhs.eval(), rhs.eval());
	}
}
