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
	public Expr reduce() {
		Expr l = lhs.reduce();
		Expr r = rhs.reduce();
		if (Const.NAN.equals(l)) return l;
		if (Const.NAN.equals(r)) return r;
		
		// 0 + rhs = rhs
		if (Const.ZERO.equals(l)) return r;
		
		// lhs + 0 = lhs
		if (Const.ZERO.equals(r)) return l;
		
		if (l instanceof Const && r instanceof Const) {
			Rational rl = ((Const)l).rational;
			Rational rr = ((Const)r).rational;
			return new Const(rl.add(rr));
		}
		
		if (r instanceof Const) {
			return new Plus(r, l).reduce();
		}

		if (l instanceof Const && r instanceof Plus && ((Plus)r).lhs instanceof Const) {
			Rational rl = ((Const)l).rational;
			Rational rr = ((Const)((Plus)r).lhs).rational;
			return new Plus(new Const(rl.add(rr)), ((Plus)r).rhs);
		}
		
		if (r instanceof Plus && ((Plus)r).lhs instanceof Const && l instanceof Plus && ((Plus)l).lhs instanceof Const) {
			Rational rl = ((Const)((Plus)l).lhs).rational;
			Rational rr = ((Const)((Plus)r).lhs).rational;
			return new Plus(new Const(rl.add(rr)), new Plus(((Plus)l).rhs, ((Plus)r).rhs));
		}
		
		return this;
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
