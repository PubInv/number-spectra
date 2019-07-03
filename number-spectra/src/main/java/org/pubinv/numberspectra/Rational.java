package org.pubinv.numberspectra;

import java.math.BigInteger;

public class Rational {
    public final BigInteger p;
    public final BigInteger q;
    
    public static Rational NEGATIVE_INFINITY = new Rational(BigInteger.ONE.negate(), BigInteger.ZERO);
    public static Rational PLUS_INFINITY = new Rational(BigInteger.ONE, BigInteger.ZERO);
    public static Rational NAN = new Rational(BigInteger.ZERO, BigInteger.ZERO);
    public static Rational ONE = new Rational(BigInteger.ONE, BigInteger.ONE);
    public static Rational ZERO = new Rational(BigInteger.ZERO, BigInteger.ONE);
    
    public Rational(BigInteger p, BigInteger q) {
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
    	return p.signum() * q.signum();
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
    
    public static Rational of(long l) {
    	return new Rational(BigInteger.valueOf(l), BigInteger.ONE);
    }
    public static Rational of(long p, long q) {
    	return new Rational(BigInteger.valueOf(p), BigInteger.valueOf(q));
    }
    
	@Override
    public String toString() {
		if (q.equals(BigInteger.ZERO)) {
			if (p.equals(BigInteger.ZERO)) {
				return String.valueOf(Double.NaN);			
			} else if (p.signum() < 0) {
				return String.valueOf(Double.NEGATIVE_INFINITY);	
			} else {
				return String.valueOf(Double.POSITIVE_INFINITY);	
			}
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
		result = prime * result + ((p == null) ? 0 : p.hashCode());
		result = prime * result + ((q == null) ? 0 : q.hashCode());
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
		Rational other = (Rational) obj;
		if (p == null) {
			if (other.p != null)
				return false;
		} else if (!p.equals(other.p))
			return false;
		if (q == null) {
			if (other.q != null)
				return false;
		} else if (!q.equals(other.q))
			return false;
		return true;
	}
}
