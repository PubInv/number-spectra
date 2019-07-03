package org.pubinv.numberspectra;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class TestRational {

	@Test
	public void testEq() {
		assertEquals(Rational.ZERO, Rational.ZERO);
		assertNotEquals(Rational.ZERO, Rational.ONE);
		assertEquals(Rational.of(1,2), Rational.of(2,4));
		assertNotEquals(Rational.of(1,2), Rational.of(2,5));
		assertNotEquals(Rational.of(1,2), Rational.ZERO);
		assertEquals(Rational.of(1,2), Rational.of(-1, -2));
		assertEquals(Rational.of(-1,2), Rational.of(1, -2));
		assertNotEquals(Rational.POSITIVE_INFINITY, Rational.NEGATIVE_INFINITY);
		assertNotEquals(Rational.POSITIVE_INFINITY, Rational.NAN);
		assertNotEquals(Rational.NEGATIVE_INFINITY, Rational.NAN);
		assertNotEquals(Rational.ZERO, Rational.NAN);
	}

	@Test
	public void testNeg() {
		assertEquals(Rational.ZERO.negate(), Rational.of(0));
		assertEquals(Rational.ONE.negate(), Rational.of(-1));
		assertEquals(Rational.of(-1).negate(), Rational.ONE);
		assertEquals(Rational.POSITIVE_INFINITY.negate(), Rational.NEGATIVE_INFINITY);
		assertEquals(Rational.NEGATIVE_INFINITY.negate(), Rational.POSITIVE_INFINITY);
		assertEquals(Rational.NAN.negate(), Rational.NAN);
	}

	@Test
	public void testRecip() {
		assertEquals(Rational.ZERO.reciprocal(), Rational.POSITIVE_INFINITY);
		assertEquals(Rational.ONE.reciprocal(), Rational.of(1));
		assertEquals(Rational.of(2).reciprocal(), Rational.of(1, 2));
		assertEquals(Rational.of(1, 2).reciprocal(), Rational.of(2));
		assertEquals(Rational.of(2, 3).reciprocal(), Rational.of(3, 2));
		assertEquals(Rational.POSITIVE_INFINITY.reciprocal(), Rational.ZERO);
		assertEquals(Rational.NEGATIVE_INFINITY.reciprocal(), Rational.ZERO);
		assertEquals(Rational.NAN.reciprocal(), Rational.NAN);
	}

	@Test
	public void testPlus() {
		assertEquals(Rational.ZERO.add(Rational.ZERO), Rational.ZERO);
		assertEquals(Rational.ZERO.add(Rational.ONE), Rational.ONE);
		assertEquals(Rational.ONE.add(Rational.ONE), Rational.of(2));
		assertEquals(Rational.of(2,3).add(Rational.of(3,4)), Rational.of(8 + 9, 3 * 4));
		assertEquals(Rational.of(-2,3).add(Rational.of(3,4)), Rational.of(-8 + 9, 3 * 4));
		assertEquals(Rational.POSITIVE_INFINITY.add(Rational.ZERO), Rational.POSITIVE_INFINITY);
		assertEquals(Rational.NEGATIVE_INFINITY.add(Rational.ZERO), Rational.NEGATIVE_INFINITY);
		assertEquals(Rational.NAN.add(Rational.ZERO), Rational.NAN);
		assertEquals(Rational.POSITIVE_INFINITY.add(Rational.NEGATIVE_INFINITY), Rational.NAN);
		assertEquals(Rational.POSITIVE_INFINITY.add(Rational.NAN), Rational.NAN);
		assertEquals(Rational.NEGATIVE_INFINITY.add(Rational.NAN), Rational.NAN);
	}

	@Test
	public void testTimes() {
		assertEquals(Rational.ZERO.multiply(Rational.ZERO), Rational.ZERO);
		assertEquals(Rational.ZERO.multiply(Rational.ONE), Rational.ZERO);
		assertEquals(Rational.ONE.multiply(Rational.ONE), Rational.of(1));
		assertEquals(Rational.ONE.multiply(Rational.of(2)), Rational.of(2));
		assertEquals(Rational.of(2,3).multiply(Rational.of(3,2)), Rational.of(1));
		assertEquals(Rational.of(2,5).multiply(Rational.of(3,2)), Rational.of(6, 10));
		assertEquals(Rational.of(-2,5).multiply(Rational.of(3,2)), Rational.of(-6, 10));
		assertEquals(Rational.of(-2,5).multiply(Rational.of(-3,2)), Rational.of(6, 10));
	}
}
