package org.pubinv.numberspectra.expr;

import java.math.BigInteger;

import org.pubinv.numberspectra.Rational;

public final class Power extends BinaryOp {
	Power(Expr lhs, Expr rhs) {
		super(lhs, rhs, true);
	}
	
	@Override
	public Expr reduce() {
		Expr l = lhs.reduce();
		Expr r = rhs.reduce();
		
		if (Const.NAN.equals(l)) return l;
		if (Const.NAN.equals(r)) return r;
		
		// A ^ 1 = A
		if (Const.ONE.equals(r)) return l;
		// A ^ 0 = 1
		if (Const.ZERO.equals(r)) return Const.ONE;
		
		// 0 ^ B = 0
		if (Const.ZERO.equals(l)) return Const.ZERO;
		
		// 1 ^ B = 1
		if (Const.ONE.equals(l)) return Const.ONE;
		
		if (l instanceof Const && r instanceof Const) {
			Rational rl = ((Const)l).rational;
			Rational rr = ((Const)r).rational;
			if (!rr.p.equals(BigInteger.ONE)) {
				Expr pp = new Const(rl.pow(rr.p));
				if (rr.isInteger()) {
					return pp;
				}
				return new Power(pp, new Const(Rational.of(BigInteger.ONE, rr.q)));
			} else if (rr.q.compareTo(BigInteger.valueOf(3)) <= 0) {
				RootAndRemainderRational rootRem = RootAndRemainderRational.extractRoot(rl, rr.q);
				if (!rootRem.root.equals(Rational.ONE)) {
					return Times.make(new Const(rootRem.root), new Power(new Const(rootRem.rem),rhs));
				}
			}
		}
				
		return this;
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
