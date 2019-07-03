package org.pubinv.numberspectra;

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
	 * Determines negateability.
	 * @return True if the expression can be negated.
	 */
	boolean isNegatable();
	
	/**
	 * Negate the expression.
	 * @return An expression representing the negation of this expression
	 */
	Expr negate();
	
	/**
	 * Evaluates the expression.
	 * @return The double representation
	 */
	double eval();
}
