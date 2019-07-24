package org.pubinv.numberspectra.expr;

import java.math.BigInteger;

import org.pubinv.numberspectra.Rational;

public final class Const extends BaseExpr implements Expr {
	public final Rational rational;
	
	public static final Const NAN = new Const(Rational.NAN);
	public static final Const PLUS_INFINITY = new Const(Rational.POSITIVE_INFINITY);
	public static final Const NEGATIVE_INFINITY = new Const(Rational.NEGATIVE_INFINITY);
	public static final Const ZERO = new Const(Rational.ZERO);
	public static final Const ONE = new Const(Rational.ONE);
	
	public Const(Rational rational) {
		super();
		this.rational = rational;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + rational.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if(!super.equals(obj)) {
			return false;
		}
		Const other = (Const) obj;
        return rational.equals(other.rational);
	}

	@Override
	public String toString() {
		return rational.toString();
	}
	
	@Override
	public double eval() {
		return rational.doubleValue();
	}
	
	@Override
	public Rational evalConst() {
		return rational;
	}
}
