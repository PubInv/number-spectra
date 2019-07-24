package org.pubinv.numberspectra;

import java.util.Arrays;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import org.pubinv.numberspectra.expr.Const;
import org.pubinv.numberspectra.expr.Expr;
import org.pubinv.numberspectra.expr.Plus;
import org.pubinv.numberspectra.expr.Power;
import org.pubinv.numberspectra.expr.Times;

public class ArityCatalog {
	public static ArityCatalog INSTANCE = new ArityCatalog();
	
    public List<Supplier<Expr>> getExpressionsofArity0() {
    	return Arrays.asList(() -> new Const(Rational.ONE),() -> new Const(Rational.of(-1)));
    }
    public List<UnaryOperator<Expr>> getExpressionsofArity1() {
    	return Arrays.asList();
    }
    public List<BinaryOperator<Expr>> getExpressionsofArity2() {
    	return Arrays.asList(
    			Plus::make,
    			Power::make,
    			Times::make);
    }
}
