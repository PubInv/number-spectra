package org.pubinv.numberspectra.expr;

import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BinaryOperator;

import org.pubinv.numberspectra.Rational;

public final class Times extends BinaryOp {
	private Times(Expr lhs, Expr rhs) {
		super(lhs, rhs, false);
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
		
		if (lhs.isNegatable() && rhs.isNegatable()) {
			return make(lhs.negate(), rhs.negate());
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
		
		return new Times(lhs, rhs);
	}
	
	@Override
	public String toString() {
		if (rhs.isReciprocatable()) {
			return "(/ "+lhs+" "+rhs.reciprocate()+")";
		} else if (lhs.isReciprocatable()) {
			return "(/ "+rhs+" "+lhs.reciprocate()+")";
		} else {
		    return "(* "+lhs+" "+rhs+")";
		}
	}
	
	@Override
	public Expr reciprocate() {
		return make(lhs.reciprocate(), rhs.reciprocate());
	}
	
	@Override
	public boolean isNegatable() {
		return lhs.isNegatable() || rhs.isNegatable();
	}
	
	@Override
	public boolean isReciprocatable() {
		return lhs.isReciprocatable() && rhs.isReciprocatable();
	}
	
	@Override
	public Expr negate() {
		if (rhs.isNegatable()) {
			return make(lhs, rhs.negate());
		} else {
			return make(lhs.negate(), rhs);			
		}
	}
	
	public static void merge(Map<Expr,Expr> l, Map<Expr,Expr> toadd, BinaryOperator<Expr> o) {
		for(Entry<Expr,Expr> entry: toadd.entrySet()) {
			if (!l.containsKey(entry.getKey())) {
				l.put(entry.getKey(), entry.getValue());
			} else {
				l.put(entry.getKey(),o.apply(l.get(entry.getKey()),entry.getValue()));
			}
		}
	}
	
	@Override
	public double eval() {
		return lhs.eval() * rhs.eval();
	}
}
