package org.pubinv.numberspectra.expr;

import java.math.BigInteger;

import org.pubinv.numberspectra.Rational;

public final class NoExpr extends BaseExpr implements Expr {
	public final String str;
	
	public NoExpr(String str) {
		super();
		this.str = str;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + str.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if(!super.equals(obj)) {
			return false;
		}
		NoExpr other = (NoExpr) obj;
        return str.equals(other.str);
	}

	@Override
	public String toString() {
		return str.toString();
	}
	
	@Override
	public double eval() {
		return Double.NaN;
	}
	
	@Override
	public Expr reduce() {
		return this;
	}
}
