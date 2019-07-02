package org.pubinv.numberspectra;

/**
 * Base for all expressions.
 */
public interface Expr {
	@Override
	public int hashCode();
	@Override
	public boolean equals(Object o);
	@Override
	String toString();
	
	boolean isNegatable();
	Expr negate();
	
	double eval();
}
