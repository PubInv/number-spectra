package org.pubinv.numberspectra.expr;

/**
 * Base for all expressions.
 */
public interface Expr {
	@Override
	int hashCode();
	@Override
	boolean equals(Object o);
	@Override
	String toString();
	
	/**
	 * Evaluates the expression.
	 * @return The double representation
	 */
	double eval();
	
	
	/**
	 * Evaluates rational form of expression of it exists.
	 * @return The double representation
	 */
	Expr reduce();
}
