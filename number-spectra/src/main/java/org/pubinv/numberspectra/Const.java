package org.pubinv.numberspectra;

public class Const implements Expr {
	final Rational rational;
	
	public static final Const NAN = new Const(Rational.NAN);
	public static final Const PLUS_INFINITY = new Const(Rational.PLUS_INFINITY);
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
		result = prime * result + ((rational == null) ? 0 : rational.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Const other = (Const) obj;
		if (rational == null) {
			if (other.rational != null)
				return false;
		} else if (!rational.equals(other.rational))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return rational.toString();
	}
	
	@Override
	public boolean isNegatable() {
		return rational.signum() < 0;
	}
	
	@Override
	public Expr negate() {
		return new Const(rational.negate());
	}
	
	@Override
	public double eval() {
		return rational.p.doubleValue() / rational.q.doubleValue();
	}
}
