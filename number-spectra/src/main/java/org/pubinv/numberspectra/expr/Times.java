package org.pubinv.numberspectra.expr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.pubinv.numberspectra.Rational;

public final class Times extends BinaryOp {
	private Times(Expr lhs, Expr rhs) {
		super(lhs, rhs, false);
	}
	
	public static void getTerms(List<Expr> l, Expr e) {
		if (e instanceof Times) {
			getTerms(l, ((Times) e).lhs);
			getTerms(l, ((Times) e).rhs);
		} else {
			l.add(e);
		}
	}
	
	public List<Expr> getTerms() {
		List<Expr> l = new ArrayList<>();
		getTerms(l, this);
		Collections.sort(l, (a,b) -> {
			if (a.getClass() != b.getClass()) {
				return Integer.compare(a.getClass().hashCode(), b.getClass().hashCode());
			}
			return Integer.compare(a.hashCode(), b.hashCode());
		});
		return l;
	}
	
	@Override
	public int hashCode() {
		return getTerms().hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null || obj.getClass() != this.getClass()) {
			return false;
		}
		return getTerms().equals(((Times)obj).getTerms());
	}
	
	@Override
	public Rational evalConst() {
		Rational l = lhs.evalConst();
		Rational r = rhs.evalConst();
		if (Rational.NAN.equals(l)) return l;
		if (Rational.NAN.equals(r)) return r;
		
		// 1 * rhs = rhs
		if (Rational.ONE.equals(l)) return r;
		
		// lhs * 1 = lhs
		if (Rational.ONE.equals(r)) return l;
		
		// 0 * rhs = 0
		if (Rational.ZERO.equals(l)) return l;
		
		// lhs * 0 = 0
		if (Rational.ZERO.equals(r)) return r;
		
		if (l != null && r != null) {
			return l.multiply(r);
		}
		
		return null;
	}
	
	
	public static Expr make(Expr lhs, Expr rhs) {
		return new Times(lhs,rhs);
	}
	
	@Override
	public String toString() {
		return "(* "+lhs+" "+rhs+")";
	}
	
	@Override
	public double eval() {
		return lhs.eval() * rhs.eval();
	}
}
