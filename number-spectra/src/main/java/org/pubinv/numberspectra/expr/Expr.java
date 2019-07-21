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
	 * Determines negateability.
	 * @return True if the expression can be negated.
	 */
	boolean isNegatable();
	
	/**
	 * Determines reciprocability.
	 * @return True if the expression can be reciprocated.
	 */
	boolean isReciprocatable();
	
	/**
	 * Negate the expression.
	 * @return An expression representing the negation of this expression
	 */
	Expr negate();
	
	/**
	 * Reciprociated the expression.
	 * @return An expression representing the reciprocal of this expression
	 */
	Expr reciprocate();
	
	/**
	 * Evaluates the expression.
	 * @return The double representation
	 */
	double eval();
}
