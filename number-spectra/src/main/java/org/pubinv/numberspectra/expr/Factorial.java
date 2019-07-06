package org.pubinv.numberspectra.expr;

import java.math.BigInteger;

import org.pubinv.numberspectra.Rational;

public final class Factorial extends UnaryOp {
	private Factorial(Expr operand) {
		super(operand);
	}
	
	public static Expr make(Expr e) {
		if (e instanceof Const) {
			Rational r = ((Const)e).rational;
			if (r.equals(Rational.POSITIVE_INFINITY)) {
				return new Const(r);
			} else if (r.equals(Rational.NEGATIVE_INFINITY) || r.equals(Rational.NAN)) {
				return new Const(Rational.NAN);
			} else if (r.isInteger() && r.signum() < 0 ) {
				return new Const(Rational.NAN);
			} else if (r.isInteger() && r.signum() >= 0 && r.compareTo(Rational.of(10)) <= 0) {
				return new Const(Rational.of(ifac(r.longValue())));
			}
		}
		if (e instanceof Factorial) {
			throw new IllegalArgumentException();
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
		return gamma(operand.eval());
	}
	
	static double logGamma(double x) {
		double tmp = (x - 0.5) * Math.log(x + 4.5) - (x + 4.5);
		double ser = 1.0
				+ 76.18009173 / (x + 0)
				- 86.50532033 / (x + 1)
				+ 24.01409822 / (x + 2)
				- 1.231739516 / (x + 3)
				+ 0.00120858003 / (x + 4)
				- 0.00000536382 / (x + 5);
		return tmp + Math.log(ser * Math.sqrt(2 * Math.PI));
	}

	static double gamma(double x) {
		return Math.exp(logGamma(x));
	}

}
