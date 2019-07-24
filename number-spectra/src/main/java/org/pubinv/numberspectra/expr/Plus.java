package org.pubinv.numberspectra.expr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.pubinv.numberspectra.Rational;

public final class Plus extends BinaryOp {
	private Plus(Expr lhs, Expr rhs) {
		super(lhs, rhs, false);
	}
	
	public static void getTerms(List<Expr> l, Expr e) {
		if (e instanceof Plus) {
			getTerms(l, ((Plus) e).lhs);
			getTerms(l, ((Plus) e).rhs);
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
		return getTerms().equals(((Plus)obj).getTerms());
	}
	
	@Override
	public Rational evalConst() {
		Rational l = lhs.evalConst();
		Rational r = rhs.evalConst();
		if (Rational.NAN.equals(l)) return l;
		if (Rational.NAN.equals(r)) return r;
		
		// 0 + rhs = rhs
		if (Rational.ZERO.equals(l)) return r;
		
		// lhs + 0 = lhs
		if (Rational.ZERO.equals(r)) return l;
		
		if (l != null && r != null) {
			return l.add(r);
		}
		
		return null;
	}
	
	public static Expr make(Expr lhs, Expr rhs) {
		return new Plus(lhs, rhs);
	}
	
	@Override
	public String toString() {		
		return "(+ "+lhs+" "+rhs+")";
	}
	
	@Override
	public double eval() {
		return lhs.eval() + rhs.eval();
	}
}
