package org.pubinv.numberspectra;

import static org.junit.Assert.assertEquals;

import java.util.Collections;

import org.junit.Test;

public class TestExpressionSet {

	@Test
	public void testE0() {
		assertEquals(ExpressionSet.E0.expressions, Collections.emptySet());
	}
}
