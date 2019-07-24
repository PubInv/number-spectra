package org.pubinv.numberspectra;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.BinaryOperator;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import org.pubinv.numberspectra.expr.Const;
import org.pubinv.numberspectra.expr.Expr;

public class ExpressionSet {
    final Map<Expr,Set<Expr>> expressions;
    
    public ExpressionSet(Map<Expr,Set<Expr>> set) {
    	this.expressions = set;
    }
    
    public void add(Expr e) {
    	Rational l = e.evalConst();
    	Expr k;
    	if (l != null) {
    		k = new Const(l);
    	} else {
    		k = e;
    	}
    	add(k, Collections.singleton(e));
    }
    
    public void add(Expr k, Set<Expr> l) {
     	Set<Expr> list = expressions.get(k);
    	if (list == null) {
    		expressions.put(k, list = new HashSet<>());
    	}
    	list.addAll(l);
    }
   
    public static ExpressionSet generateE0() {
    	return new ExpressionSet(new HashMap<>());
    }
    
    public static ExpressionSet generateE1() {
    	ExpressionSet es = new ExpressionSet(new HashMap<>());
    	for(Supplier<Expr> s: ArityCatalog.INSTANCE.getExpressionsofArity0()) {
    		es.add(s.get());
    	}
    	return es;
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
    	ExpressionSet es = new ExpressionSet(new HashMap<>());
    	for(UnaryOperator<Expr> s: ArityCatalog.INSTANCE.getExpressionsofArity1()) {
    		for(Entry<Expr,Set<Expr>> entry: sets[n - 1].expressions.entrySet()) {
    			Expr apply;
				try {
					apply = s.apply(entry.getKey());
				} catch (Exception e1) {
					continue;
				} catch (Throwable e1) {
					throw new RuntimeException("Error with "+s+"("+entry.getKey()+")",e1);
				}
				es.add(apply);
    		}
    	}
    	for(BinaryOperator<Expr> s: ArityCatalog.INSTANCE.getExpressionsofArity2()) {
    		for(int k = 1; k < n - 1; k++) {
    			Map<Expr,Set<Expr>> s1 = sets[k].expressions;
    			Map<Expr,Set<Expr>> s2 = sets[n - k - 1].expressions;
        		for(Entry<Expr,Set<Expr>> e1: s1.entrySet()) {
            		for(Entry<Expr,Set<Expr>> e2: s2.entrySet()) {
            			Expr apply;
						try {
							apply = s.apply(e1.getKey(), e2.getKey());
						} catch (Exception e) {
							continue;
						} catch (Throwable e) {
							throw new RuntimeException("Error with "+s+"("+e1.getKey()+","+e2.getKey()+")", e);
						}
						es.add(apply);
            		}
        		}
    		}
    	}
    	return es;
    }

	@Override
	public String toString() {
		return "ExpressionSet [expressions=" + expressions + "]";
	}

}
