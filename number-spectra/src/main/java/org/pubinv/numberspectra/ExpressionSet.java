package org.pubinv.numberspectra;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

public class ExpressionSet {
    final Set<Expr> expressions;
    
    public ExpressionSet(Set<Expr> set) {
    	this.expressions = set;
    }
    
    public static ExpressionSet generateE0() {
    	Set<Expr> expressions = new HashSet<>();
    	return new ExpressionSet(expressions);
    }
    
    public static ExpressionSet generateE1() {
    	Set<Expr> expressions = new HashSet<>();
    	for(Supplier<Expr> s: ArityCatalog.INSTANCE.getExpressionsofArity0()) {
    		expressions.add(s.get());
    	}
    	return new ExpressionSet(expressions);
    }
    
    public static ExpressionSet generateE2() {
    	ExpressionSet E1 = generateE1();
    	Set<Expr> expressions = new HashSet<>();
    	for(Function<Expr, Expr> s: ArityCatalog.INSTANCE.getExpressionsofArity1()) {
    		for(Expr e: E1.expressions) {
    			expressions.add(s.apply(e));
    		}
    	}
    	return new ExpressionSet(expressions);
    }

	@Override
	public String toString() {
		return "ExpressionSet [expressions=" + expressions + "]";
	}

}
