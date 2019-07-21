package org.pubinv.numberspectra;

import static org.junit.Assert.assertEquals;

import java.math.BigInteger;

import org.junit.Test;
import org.pubinv.numberspectra.expr.Const;
import org.pubinv.numberspectra.expr.Expr;
import org.pubinv.numberspectra.expr.Negate;
import org.pubinv.numberspectra.expr.NoExpr;
import org.pubinv.numberspectra.expr.Plus;
import org.pubinv.numberspectra.expr.Reciprocal;
import org.pubinv.numberspectra.expr.RootAndRemainder;
import org.pubinv.numberspectra.expr.RootAndRemainderRational;
import org.pubinv.numberspectra.expr.Times;

public class TestExpressions {
	
	public Expr i(long n) {
		return new NoExpr("V"+n);
	}
	
	private Expr neg(Expr e) {
		return Negate.make(e);
	}
	private Expr recip(Expr e) {
		return Reciprocal.make(e);
	}
	private Expr plus(Expr x, Expr y) {
		return Plus.make(x, y);
	}
	private Expr times(Expr x, Expr y) {
		return Times.make(x, y);
	}
	private Expr n(long a) {
		return new Const(Rational.of(a));
	}

	@Test
	public void testNeg() {
		assertEquals(neg(n(0)), n(0));
		assertEquals(neg(n(1)), n(-1));
		assertEquals(neg(neg(i(1))), i(1));
		assertEquals(neg(plus(i(1), i(2))), plus(neg(i(1)), neg(i(2))));
	}

	@Test
	public void testRecip() {
		assertEquals(recip(new Const(Rational.of(1, 5))), new Const(Rational.of(5)));
		assertEquals(recip(recip(i(1))), i(1));
		assertEquals(recip(neg(i(1))), neg(recip(i(1))));
	}

	@Test
	public void testPlus() {
		assertEquals(plus(n(0), i(1)), i(1));
		assertEquals(plus(i(1), n(0)), i(1));
		assertEquals(plus(i(1), plus(i(2), i(3))), plus(plus(i(1), i(2)), i(3)));
		assertEquals(plus(i(1), plus(i(2), i(3))), plus(plus(i(1), i(2)), i(3)));
		assertEquals(plus(n(1), plus(i(1), n(2))), plus(n(3), i(1)));
		assertEquals(plus(i(1), n(3)), plus(n(3), i(1)));
	}

	@Test
	public void testTimes() {		
		assertEquals(times(neg(i(2)), neg(i(3))), times(i(3), i(2)));
		
		assertEquals(times(n(0), i(2)), n(0));
		assertEquals(times(n(1), i(2)), i(2));
		
		assertEquals(times(i(2), n(1)), i(2));
		assertEquals(times(i(2), n(0)), n(0));
		
		assertEquals(times(i(4), times(i(2), i(3))), times(times(i(4), i(2)), i(3)));
		assertEquals(times(n(3), times(i(4), n(2))), times(n(6), i(4)));
		assertEquals(times(i(4), n(3)), times(n(3), i(4)));
	}
	
	@Test
	public void testRootAndRemainder() {
		BigInteger two = BigInteger.valueOf(2);
		assertEquals(RootAndRemainder.extractRoot(BigInteger.ZERO, two), new RootAndRemainder(BigInteger.ZERO));
		assertEquals(RootAndRemainder.extractRoot(BigInteger.ONE, two), new RootAndRemainder(BigInteger.ONE));
		assertEquals(RootAndRemainder.extractRoot(BigInteger.valueOf(-1), two), new RootAndRemainder(BigInteger.valueOf(-1)));
		assertEquals(RootAndRemainder.extractRoot(BigInteger.valueOf(2), two), new RootAndRemainder(BigInteger.valueOf(2)));
		assertEquals(RootAndRemainder.extractRoot(BigInteger.valueOf(-2), two), new RootAndRemainder(BigInteger.valueOf(-2)));
		assertEquals(RootAndRemainder.extractRoot(BigInteger.valueOf(3), two), new RootAndRemainder(BigInteger.valueOf(3)));
		assertEquals(RootAndRemainder.extractRoot(BigInteger.valueOf(4), two), new RootAndRemainder(BigInteger.valueOf(2), BigInteger.valueOf(1)));
		assertEquals(RootAndRemainder.extractRoot(BigInteger.valueOf(-4), two), new RootAndRemainder(BigInteger.valueOf(2), BigInteger.valueOf(-1)));
		assertEquals(RootAndRemainder.extractRoot(BigInteger.valueOf(5), two), new RootAndRemainder(BigInteger.valueOf(5)));
		assertEquals(RootAndRemainder.extractRoot(BigInteger.valueOf(6), two), new RootAndRemainder(BigInteger.valueOf(6)));
		assertEquals(RootAndRemainder.extractRoot(BigInteger.valueOf(8), two), new RootAndRemainder(BigInteger.valueOf(2), BigInteger.valueOf(2)));
		assertEquals(RootAndRemainder.extractRoot(BigInteger.valueOf(8), BigInteger.valueOf(3)), new RootAndRemainder(BigInteger.valueOf(2), BigInteger.ONE));
	}
	
	@Test
	public void testRootAndRemainderRational() {
		BigInteger two = BigInteger.valueOf(2);
		assertEquals(RootAndRemainderRational.extractRoot(Rational.ZERO, two), new RootAndRemainderRational(Rational.ZERO));
		assertEquals(RootAndRemainderRational.extractRoot(Rational.ONE, two), new RootAndRemainderRational(Rational.ONE));
		assertEquals(RootAndRemainderRational.extractRoot(Rational.of(4), two), new RootAndRemainderRational(Rational.of(2), Rational.ONE));
		assertEquals(RootAndRemainderRational.extractRoot(Rational.of(4,3), two), new RootAndRemainderRational(Rational.of(2), Rational.of(1,3)));
		assertEquals(RootAndRemainderRational.extractRoot(Rational.of(4,9), two), new RootAndRemainderRational(Rational.of(2, 3), Rational.ONE));
		assertEquals(RootAndRemainderRational.extractRoot(Rational.of(-4,9), two), new RootAndRemainderRational(Rational.of(2, 3), Rational.of(-1)));
	}
}
