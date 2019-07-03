package org.pubinv.numberspectra;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TestExpressions {

	@Test
	public void testNeg() {
		assertEquals(Negate.make(new Const(Rational.ZERO)), new Const(Rational.of(0)));
		assertEquals(Negate.make(new Const(Rational.of(1))), new Const(Rational.of(-1)));
		assertEquals(Negate.make(Negate.make(new Const(Rational.of(-1)))), new Const(Rational.of(-1)));
	}

	@Test
	public void testRecip() {
		assertEquals(Reciprocal.make(new Const(Rational.ZERO)), new Const(Rational.POSITIVE_INFINITY));
		assertEquals(Reciprocal.make(new Const(Rational.POSITIVE_INFINITY)), new Const(Rational.ZERO));
		assertEquals(Reciprocal.make(new Const(Rational.of(5))), new Const(Rational.of(1,5)));
		assertEquals(Reciprocal.make(new Const(Rational.of(1, 5))), new Const(Rational.of(5)));
	}

	@Test
	public void testPlus() {
		assertEquals(Plus.make(new Const(Rational.ZERO), new Const(Rational.ZERO)), new Const(Rational.of(0)));
		assertEquals(Plus.make(new Const(Rational.ONE), new Const(Rational.ZERO)), new Const(Rational.of(1)));
		assertEquals(Plus.make(new Const(Rational.ZERO), new Const(Rational.ONE)), new Const(Rational.of(1)));
		assertEquals(Plus.make(new Const(Rational.ONE), new Const(Rational.ONE)), new Const(Rational.of(2)));
		assertEquals(Plus.make(new Const(Rational.POSITIVE_INFINITY), new Const(Rational.ONE)), new Const(Rational.POSITIVE_INFINITY));
		assertEquals(Plus.make(new Const(Rational.NEGATIVE_INFINITY), new Const(Rational.ONE)), new Const(Rational.NEGATIVE_INFINITY));
		assertEquals(Plus.make(new Const(Rational.POSITIVE_INFINITY), new Const(Rational.NEGATIVE_INFINITY)), new Const(Rational.NAN));
		assertEquals(Plus.make(new Const(Rational.POSITIVE_INFINITY), new Const(Rational.POSITIVE_INFINITY)), new Const(Rational.NAN));
	}

	@Test
	public void testTimes() {
		assertEquals(Times.make(new Const(Rational.of(2)), new Const(Rational.of(3))), new Const(Rational.of(6)));
		assertEquals(Times.make(new Const(Rational.of(1)), new Const(Rational.of(3))), new Const(Rational.of(3)));
		assertEquals(Times.make(new Const(Rational.of(3)), new Const(Rational.of(1))), new Const(Rational.of(3)));
		assertEquals(Times.make(new Const(Rational.of(0)), new Const(Rational.of(3))), new Const(Rational.of(0)));
		assertEquals(Times.make(new Const(Rational.of(3)), new Const(Rational.of(0))), new Const(Rational.of(0)));
		
		assertEquals(Times.make(new Const(Rational.POSITIVE_INFINITY), new Const(Rational.ONE)), new Const(Rational.POSITIVE_INFINITY));
		assertEquals(Times.make(new Const(Rational.NEGATIVE_INFINITY), new Const(Rational.ONE)), new Const(Rational.NEGATIVE_INFINITY));
		assertEquals(Times.make(new Const(Rational.POSITIVE_INFINITY), new Const(Rational.NEGATIVE_INFINITY)), new Const(Rational.NEGATIVE_INFINITY));
		assertEquals(Times.make(new Const(Rational.NEGATIVE_INFINITY), new Const(Rational.NEGATIVE_INFINITY)), new Const(Rational.POSITIVE_INFINITY));
		assertEquals(Times.make(new Const(Rational.POSITIVE_INFINITY), new Const(Rational.POSITIVE_INFINITY)), new Const(Rational.POSITIVE_INFINITY));
	}
	
	@Test
	public void testPower() {
		assertEquals(Power.make(new Const(Rational.of(2)), new Const(Rational.of(3))), new Const(Rational.of(8)));
		assertEquals(Power.make(new Const(Rational.of(0)), new Const(Rational.of(3))), new Const(Rational.of(0)));
		assertEquals(Power.make(new Const(Rational.of(0)), new Const(Rational.of(0))), new Const(Rational.of(1)));
		assertEquals(Power.make(new Const(Rational.of(1)), new Const(Rational.of(0))), new Const(Rational.of(1)));
		assertEquals(Power.make(new Const(Rational.of(1)), new Const(Rational.of(-1))), new Const(Rational.of(1)));
		assertEquals(Power.make(new Const(Rational.of(3)), new Const(Rational.of(-1))), new Const(Rational.of(1, 3)));
		assertEquals(Power.make(new Const(Rational.of(3)), new Const(Rational.of(-2))), new Const(Rational.of(1, 9)));
	}
}
