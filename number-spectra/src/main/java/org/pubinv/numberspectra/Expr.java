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
	
	boolean isNegatable();
	Expr negate();
	
	double eval();
}
