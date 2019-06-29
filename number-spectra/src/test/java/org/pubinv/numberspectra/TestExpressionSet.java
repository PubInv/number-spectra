package org.pubinv.numberspectra;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import org.junit.Test;

public class TestExpressionSet {

	@Test
	public void testE0() {
		assertEquals(ExpressionSet.generateE0().expressions, Collections.emptySet());
	}

	@Test
	public void testE1() {
		assertEquals(ExpressionSet.generateE1().expressions, new HashSet<Expr>(Arrays.asList(new One())));
	}

	@Test
	public void testE2() {
		assertEquals(ExpressionSet.generateE2().expressions, new HashSet<Expr>(Arrays.asList(
				new Factorial(new One()),
				new Negate(new One()))));
	}
}
