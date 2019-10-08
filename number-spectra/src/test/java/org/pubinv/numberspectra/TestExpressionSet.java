package org.pubinv.numberspectra;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.junit.Test;
import org.pubinv.numberspectra.expr.Expr;

public class TestExpressionSet {

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
		for(int i = 0; i < 16; i++) {
			ExpressionSet set = ExpressionSet.generateE(history.toArray(new ExpressionSet[0]));
			for(Entry<Expr, Set<Expr>> e: set.expressions.entrySet()) {
				double f = e.getKey().eval();
				if (Double.isNaN(f)) continue;
				if (Double.isInfinite(f)) continue;
				Set<Expr> l = map.get(f);
				if (l == null) {
					l = new HashSet<>();
					map.put(f, l);
				}
				l.addAll(e.getValue());
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
