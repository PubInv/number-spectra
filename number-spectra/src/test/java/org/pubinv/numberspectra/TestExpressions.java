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
	private Expr v(String a) {
		return new NoExpr(a);
	}

	@Test
	public void testNeg() {
		assertEquals(neg(n(0)).reduce(), Const.ZERO);
		assertEquals(neg(n(1)).reduce(), Const.of(-1));
	}

	@Test
	public void testRecip() {
		assertEquals(recip(new Const(Rational.of(1, 5))).reduce(), Const.of(5));
	}

	@Test
	public void testPlus() {
		assertEquals(plus(n(0), n(3)).reduce(), Const.of(3));
		assertEquals(plus(n(2), n(3)).reduce(), Const.of(5));
		assertEquals(plus(n(1), v("x")).reduce(), plus(v("x"), n(1)).reduce());
		assertEquals(plus(plus(n(2), v("x")),n(3)).reduce(), plus(v("x"), n(5)).reduce());
		assertEquals(plus(plus(n(2), v("x")),plus(n(3), v("y"))).reduce(), plus(plus(v("x"), v("y")), n(5)).reduce());
	}

	@Test
	public void testTimes() {		
		assertEquals(times(n(1), n(3)).reduce(), Const.of(3));
		assertEquals(times(n(3), n(1)).reduce(), Const.of(3));
		assertEquals(times(n(0), n(3)).reduce(), Const.of(0));
		assertEquals(times(n(3), n(0)).reduce(), Const.of(0));
		assertEquals(times(n(2), n(3)).reduce(), Const.of(6));
		assertEquals(times(n(1), v("x")).reduce(), times(v("x"), n(1)).reduce());
		assertEquals(times(times(n(2), v("x")),n(3)).reduce(), times(v("x"), n(6)).reduce());
		assertEquals(times(times(n(2), v("x")),times(n(3), v("y"))).reduce(), times(times(v("x"), v("y")), n(6)).reduce());
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
