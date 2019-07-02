package org.pubinv.numberspectra;

import java.math.BigInteger;

public class Factorial implements Expr {
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((operand == null) ? 0 : operand.hashCode());
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
		Factorial other = (Factorial) obj;
		if (operand == null) {
			if (other.operand != null)
				return false;
		} else if (!operand.equals(other.operand))
			return false;
		return true;
	}

	final Expr operand;

	private Factorial(Expr operand) {
		super();
		this.operand = operand;
	}
	
	public static Expr make(Expr e) {
		if (e instanceof Const) {
			Rational r = ((Const)e).rational;
			if (r.isInteger() && r.p.signum() >= 0 && r.p.compareTo(BigInteger.valueOf(100)) <= 0) {
				return new Const(new Rational(ifac(r.p.longValue()), BigInteger.ONE));
			} else if (r.isInteger() && r.p.signum() < 0 ) {
				return new Const(Rational.of(0, 0));
			} else if (r.isPositiveInfinity()) {
				return new Const(r);
			}else if (r.isNegativeInfinity() || r.isNan()) {
				return new Const(Rational.of(0, 0));
			}
		}
		return new Factorial(e);
	}

	private static BigInteger ifac(long l) {
		BigInteger res = BigInteger.ONE;
		while(l > 1) {
			res = res.multiply(BigInteger.valueOf(l));
			l--;
		}
		return res;
	}
	
	@Override
	public String toString() {
		return "(! " + operand + ")";
	}
	
	@Override
	public boolean isNegatable() {
		return false;
	}
	
	@Override
	public Expr negate() {
		return Negate.make(this);
	}
	
	@Override
	public double eval() {
		return logGamma(operand.eval());
	}
	
	static double logGamma(double x) {
		double tmp = (x - 0.5) * Math.log(x + 4.5) - (x + 4.5);
		double ser = 1.0 + 76.18009173 / (x + 0) - 86.50532033 / (x + 1) + 24.01409822 / (x + 2) - 1.231739516 / (x + 3)
				+ 0.00120858003 / (x + 4) - 0.00000536382 / (x + 5);
		return tmp + Math.log(ser * Math.sqrt(2 * Math.PI));
	}

	static double gamma(double x) {
		return Math.exp(logGamma(x));
	}

}
