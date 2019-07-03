package org.pubinv.numberspectra;

import java.util.Arrays;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import org.pubinv.numberspectra.expr.Const;
import org.pubinv.numberspectra.expr.Expr;
import org.pubinv.numberspectra.expr.Factorial;
import org.pubinv.numberspectra.expr.Negate;
import org.pubinv.numberspectra.expr.Plus;
import org.pubinv.numberspectra.expr.Power;
import org.pubinv.numberspectra.expr.Reciprocal;
import org.pubinv.numberspectra.expr.Times;

public class ArityCatalog {
	public static ArityCatalog INSTANCE = new ArityCatalog();
	
    public List<Supplier<Expr>> getExpressionsofArity0() {
    	return Arrays.asList(() -> new Const(Rational.ONE));
    }
    public List<UnaryOperator<Expr>> getExpressionsofArity1() {
    	return Arrays.asList(
    			e -> Negate.make(e),
    			e -> Reciprocal.make(e),
    			e -> Factorial.make(e));
    }
    public List<BinaryOperator<Expr>> getExpressionsofArity2() {
    	return Arrays.asList(
    			(lhs,rhs) -> Plus.make(lhs, rhs),
    			(lhs,rhs) -> Power.make(lhs, rhs),
    			(lhs,rhs) -> Times.make(lhs, rhs));
    }
}
