package org.pubinv.numberspectra;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import org.junit.Test;
import org.pubinv.numberspectra.expr.Const;
import org.pubinv.numberspectra.expr.Expr;
import org.pubinv.numberspectra.expr.Factorial;
import org.pubinv.numberspectra.expr.Negate;
import org.pubinv.numberspectra.expr.Reciprocal;

public class TestExpressionSet {

	@Test
	public void testE0() {
		assertEquals(ExpressionSet.generateE0().expressions, Collections.emptySet());
	}

	@Test
	public void testE1() {
		assertEquals(ExpressionSet.generateE1().expressions, new HashSet<Expr>(Arrays.asList(
				new Const(Rational.ONE))));
	}

	@Test
	public void testE2() {
		assertEquals(ExpressionSet.generateE(ExpressionSet.generateE1()).expressions, new HashSet<Expr>(Arrays.asList(
				Factorial.make(new Const(Rational.ONE)),
				Reciprocal.make(new Const(Rational.ONE)),
				Negate.make(new Const(Rational.ONE))
				)));
	}

	@Test
	public void testE3() {
		ExpressionSet E0 = ExpressionSet.generateE0();
		ExpressionSet E1 = ExpressionSet.generateE1();
		ExpressionSet E2 = ExpressionSet.generateE(E0, E1);
		ExpressionSet E3 = ExpressionSet.generateE(E0, E1, E2);
	    System.out.println(E3);
	}
	
	@Test
	public void testEn() {
		Rational.of(1, -1);
		ExpressionSet E0 = ExpressionSet.generateE0();
		ExpressionSet E1 = ExpressionSet.generateE1();
		List<ExpressionSet> history = new ArrayList<>();
		history.add(E0);
		history.add(E1);
		TreeMap<Double, Set<Expr>> map = new TreeMap<>();
		for(int i = 0; i < 15; i++) {
			ExpressionSet set = ExpressionSet.generateE(history.toArray(new ExpressionSet[0]));
			for(Expr e: set.expressions) {
				double f = e.eval();
				if (Double.isNaN(f)) continue;
				if (Double.isInfinite(f)) continue;
				Set<Expr> l = map.get(f);
				if (l == null) {
					l = new HashSet<>();
					map.put(f, l);
				}
				l.add(e);
			}
			history.add(set);
			System.out.println(history.size());
		}
		map.forEach((k, v) -> {
			for(Expr e: v) {
				System.out.println(k + "=" + e);								
			}
		});
	}
}
