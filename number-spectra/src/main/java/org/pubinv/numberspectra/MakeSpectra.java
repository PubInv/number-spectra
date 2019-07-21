package org.pubinv.numberspectra;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import org.pubinv.numberspectra.expr.Expr;

public class MakeSpectra {
	public static void main(String[] args) throws IOException {
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
		List<String> lines = new ArrayList<>();
		map.forEach((k, v) -> {
			for(Expr e: v) {
				lines.add("{value:"+k+",expression_text:\""+e+"\"}");
			}
		});
		FileWriter fw = new FileWriter("/tmp/spectra.js");
		fw.write("const SPECTRA0 = [\n"+String.join(",\n", lines)+"];");
		fw.close();
	}
}
