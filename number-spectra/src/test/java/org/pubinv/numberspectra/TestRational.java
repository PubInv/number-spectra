package org.pubinv.numberspectra;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigInteger;

import org.junit.Test;

public class TestRational {
	
	public boolean areEq(Rational r,double y) {
		if (r == Rational.NAN) return Double.isNaN(y);
		if (r == Rational.POSITIVE_INFINITY) return (Double.isInfinite(y) && y > 0);
		if (r == Rational.NEGATIVE_INFINITY) return (Double.isInfinite(y) && y < 0);
		return Math.abs(r.doubleValue() - y) < 1e15;
	}

	public boolean areEq(Rational r,float y) {
		if (r == Rational.NAN) return Float.isNaN(y);
		if (r == Rational.POSITIVE_INFINITY) return (Float.isInfinite(y) && y > 0);
		if (r == Rational.NEGATIVE_INFINITY) return (Float.isInfinite(y) && y < 0);
		return Math.abs(r.floatValue() - y) < 1e8;
	}

	@Test
	public void testToDouble() {
		assertTrue(areEq(Rational.ZERO, 0.0));
		assertTrue(areEq(Rational.ONE, 1.0));
		assertTrue(areEq(Rational.of(1,3), 1.0 / 3));
		assertTrue(areEq(Rational.of(-1,3), -1.0 / 3));
		assertTrue(areEq(Rational.NAN, Double.NaN));
		assertTrue(areEq(Rational.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
		assertTrue(areEq(Rational.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY));
	}

	@Test
	public void testToFloat() {
		assertTrue(areEq(Rational.ZERO, 0.0F));
		assertTrue(areEq(Rational.ONE, 1.0F));
		assertTrue(areEq(Rational.of(1,3), 1.0F / 3F));
		assertTrue(areEq(Rational.of(-1,3), -1.0F / 3F));
		assertTrue(areEq(Rational.NAN, Float.NaN));
		assertTrue(areEq(Rational.POSITIVE_INFINITY, Float.POSITIVE_INFINITY));
		assertTrue(areEq(Rational.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY));
	}

	@Test
	public void testIsInteger() {
		assertTrue(Rational.ZERO.isInteger());
		assertTrue(Rational.ONE.isInteger());
		assertTrue(Rational.of(-1).isInteger());
		assertTrue(Rational.of(4,2).isInteger());
		assertFalse(Rational.of(2, 4).isInteger());
		assertFalse(Rational.POSITIVE_INFINITY.isInteger());
		assertFalse(Rational.NEGATIVE_INFINITY.isInteger());
		assertFalse(Rational.NAN.isInteger());
	}

	@Test
	public void testSigNum() {
		assertTrue(Rational.ZERO.signum() == 0);
		assertTrue(Rational.ONE.signum() > 0);
		assertTrue(Rational.of(-1).signum() < 0);
		assertTrue(Rational.POSITIVE_INFINITY.signum() > 0);
		assertTrue(Rational.NEGATIVE_INFINITY.signum() < 0);
		assertTrue(Rational.NAN.signum() == 0);
	}

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
	public void testPow() {
		assertEquals(Rational.ZERO.pow(BigInteger.ZERO), Rational.of(1));
		assertEquals(Rational.ZERO.pow(BigInteger.ONE), Rational.of(0));
		assertEquals(Rational.of(3).pow(BigInteger.ZERO), Rational.of(1));
		assertEquals(Rational.of(3).pow(BigInteger.ONE), Rational.of(3));
		assertEquals(Rational.of(3).pow(BigInteger.valueOf(2)), Rational.of(9));
		assertEquals(Rational.of(3).pow(BigInteger.valueOf(3)), Rational.of(27));
		assertEquals(Rational.of(3).pow(BigInteger.valueOf(5)), Rational.of(3 * 3 * 3 * 3 * 3));
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
