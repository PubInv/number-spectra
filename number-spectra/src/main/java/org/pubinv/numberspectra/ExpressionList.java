package org.pubinv.numberspectra;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ExpressionList {
    public static ExpressionList E0 = new ExpressionList();
    
    public List<Set<Expr>> expressions = new ArrayList<>();
    
	@Override
	public String toString() {
		return "ExpressionList [expressions=" + expressions + "]";
	}
}
