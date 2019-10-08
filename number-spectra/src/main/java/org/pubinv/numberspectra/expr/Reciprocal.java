package org.pubinv.numberspectra.expr;

import org.pubinv.numberspectra.Rational;

public final class Reciprocal {
	public static Expr make(Expr e) {
		return Power.make(e, new Const(Rational.of(-1)));
	}
}
