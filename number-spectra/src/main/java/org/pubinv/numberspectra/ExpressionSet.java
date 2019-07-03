package org.pubinv.numberspectra;

import java.util.HashSet;
import java.util.Set;
import java.util.function.BinaryOperator;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import org.pubinv.numberspectra.expr.Expr;

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
    
    /**
     * Generate En based on E0 E1 E2 ... En-1
     * @param sets Sets in prder of cardinality.
     * @return The new set.
     */
    public static ExpressionSet generateE(ExpressionSet ...sets) {
    	int n = sets.length;
    	if (n == 0) {
    		return generateE1();
    	}
    	Set<Expr> expressions = new HashSet<>();
    	for(UnaryOperator<Expr> s: ArityCatalog.INSTANCE.getExpressionsofArity1()) {
    		for(Expr e: sets[n - 1].expressions) {
    			Expr apply;
				try {
					apply = s.apply(e);
				} catch (Exception e1) {
					continue;
				}
				expressions.add(apply);
    		}
    	}
    	for(BinaryOperator<Expr> s: ArityCatalog.INSTANCE.getExpressionsofArity2()) {
    		for(int k = 1; k < n - 1; k++) {
    			Set<Expr> s1 = sets[k].expressions;
    			Set<Expr> s2 = sets[n - k - 1].expressions;
        		for(Expr e1: s1) {
            		for(Expr e2: s2) {
            			Expr apply;
						try {
							apply = s.apply(e1, e2);
						} catch (Exception e) {
							continue;
						}
						expressions.add(apply);
            		}
        		}
    		}
    	}
    	return new ExpressionSet(expressions);
    }

	@Override
	public String toString() {
		return "ExpressionSet [expressions=" + expressions + "]";
	}

}
