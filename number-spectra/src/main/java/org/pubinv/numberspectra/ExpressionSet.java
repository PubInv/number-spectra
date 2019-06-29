package org.pubinv.numberspectra;

import java.util.Collections;
import java.util.Set;

public class ExpressionSet {
    public static ExpressionSet E0 = new ExpressionSet(Collections.emptySet());
    
    final Set<Expr> expressions;
    
    public ExpressionSet(Set<Expr> set) {
    	this.expressions = set;
    }
    
	@Override
	public String toString() {
		return "ExpressionSet [expressions=" + expressions + "]";
	}

}
