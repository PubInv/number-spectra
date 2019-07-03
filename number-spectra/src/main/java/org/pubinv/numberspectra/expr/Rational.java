package org.pubinv.numberspectra.expr;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

public final class Rational extends Number implements Comparable<Rational> {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public final BigInteger p;
    public final BigInteger q;
    
    public static Rational NEGATIVE_INFINITY = new Rational(BigInteger.ONE.negate(), BigInteger.ZERO);
    public static Rational POSITIVE_INFINITY = new Rational(BigInteger.ONE, BigInteger.ZERO);
    public static Rational NAN = new Rational(BigInteger.ZERO, BigInteger.ZERO);
    public static Rational ONE = new Rational(BigInteger.ONE, BigInteger.ONE);
    public static Rational ZERO = new Rational(BigInteger.ZERO, BigInteger.ONE);
    
    private Rational(BigInteger p, BigInteger q) {
		super();
		BigInteger div = p.gcd(q);
		if (q.signum() < 0 && div.signum() > 0) {
			div = div.negate();
		}
		if (div.equals(BigInteger.ZERO)) {
			this.p = p;
			this.q = q;		
		} else {
			this.p = p.divide(div);
			this.q = q.divide(div);
		}
	}
    
    public int signum() {
    	return p.signum();
    }
    
    public boolean isInteger() {
    	return q.equals(BigInteger.ONE);
    }
    
    public Rational add(Rational rhs) {
    	return new Rational(
    			p.multiply(rhs.q).add(rhs.p.multiply(q)),
    			q.multiply(rhs.q));
    }

    public Rational multiply(Rational rhs) {
    	return new Rational(p.multiply(rhs.p), q.multiply(rhs.q));
    }
    
    public Rational negate() {
    	return new Rational(p.negate(),q);
    }
    
    public Rational reciprocal() {
    	return new Rational(q, p);
    }
    
    public static Rational of(BigInteger l) {
    	return of(l, BigInteger.ONE);
    }
    public static Rational of(BigInteger p, BigInteger q) {
    	return new Rational(p, q);
    }
    public static Rational of(long l) {
    	return of(l, 1);
    }
    public static Rational of(long p, long q) {
    	return of(BigInteger.valueOf(p), BigInteger.valueOf(q));
    }
    
	@Override
    public String toString() {
		if (equals(Rational.NAN)) {
			return String.valueOf(Double.NaN);			
		}
		if (equals(Rational.NEGATIVE_INFINITY)) {
			return String.valueOf(Double.NEGATIVE_INFINITY);			
		}
		if (equals(Rational.POSITIVE_INFINITY)) {
			return String.valueOf(Double.POSITIVE_INFINITY);			
		}
    	if (q.equals(BigInteger.ONE)) {
    		return p.toString();
    	}
		if (p.equals(BigInteger.ZERO)) {
			return "0";
		}
    	return "(/ " + p + " " + q + ")";
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + p.hashCode();
		result = prime * result + q.hashCode();
		return result;
	}

	@Override
	public int compareTo(Rational o) {
		BigInteger i1 = p.multiply(o.q);
		BigInteger i2 = o.p.multiply(q);
		return i1.compareTo(i2);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rational other = (Rational) obj;
		return p.equals(other.p) && q.equals(other.q);
	}

	@Override
	public int intValue() {
		return p.divide(q).intValue();
	}

	@Override
	public long longValue() {
		return p.divide(q).longValue();
	}

	@Override
	public float floatValue() {
		if (equals(Rational.NAN)) {
			return Float.NaN;
		}
		if (equals(Rational.POSITIVE_INFINITY)) {
			return Float.POSITIVE_INFINITY;
		}
		if (equals(Rational.NEGATIVE_INFINITY)) {
			return Float.NEGATIVE_INFINITY;
		}
		BigDecimal pd = new BigDecimal(p);
		BigDecimal qd = new BigDecimal(q);
		return pd.divide(qd, new MathContext(20)).floatValue();
	}

	@Override
	public double doubleValue() {
		if (equals(Rational.NAN)) {
			return Double.NaN;
		}
		if (equals(Rational.POSITIVE_INFINITY)) {
			return Double.POSITIVE_INFINITY;
		}
		if (equals(Rational.NEGATIVE_INFINITY)) {
			return Double.NEGATIVE_INFINITY;
		}
		BigDecimal pd = new BigDecimal(p);
		BigDecimal qd = new BigDecimal(q);
		return pd.divide(qd, new MathContext(20)).doubleValue();
	}
}
