package org.pubinv.numberspectra.expr;

import org.pubinv.numberspectra.Rational;

public final class Negate {
	public static Expr make(Expr e) {
		return Times.make(new Const(Rational.of(-1)), e);
	}
}
