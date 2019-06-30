package org.pubinv.numberspectra;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class ArityCatalog {
	public static ArityCatalog INSTANCE = new ArityCatalog();
	
    public List<Supplier<Expr>> getExpressionsofArity0() {
    	return Arrays.asList(() -> new One());
    }
    public List<Function<Expr,Expr>> getExpressionsofArity1() {
    	return Arrays.asList(
    			e -> new Negate(e),
    			e -> new Factorial(e));
    }
    public List<BiFunction<Expr, Expr,Expr>> getExpressionsofArity2() {
    	return Arrays.asList(
    			(lhs,rhs) -> new Plus(lhs, rhs),
    			(lhs,rhs) -> new Power(lhs, rhs),
    			(lhs,rhs) -> new Times(lhs, rhs));
    }
}
