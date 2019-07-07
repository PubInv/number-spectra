package org.pubinv.numberspectra.expr;

import java.math.BigInteger;

public class RootAndRemainder {
	public final BigInteger rem;
	public final BigInteger root;
	
	public RootAndRemainder(BigInteger root, BigInteger rem) {
		super();
		this.rem = rem;
		this.root = root;
	}
	public RootAndRemainder(BigInteger rem) {
		super();
		this.rem = rem;
		this.root = BigInteger.ONE;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + rem.hashCode();
		result = prime * result + root.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RootAndRemainder other = (RootAndRemainder) obj;
		return rem.equals(other.rem) && root.equals(other.root);
	}

	@Override
	public String toString() {
		return "RootAndRemainder [rem=" + rem + ", root=" + root + "]";
	}

	public static RootAndRemainder extractRoot(BigInteger x, int n) {
		for(BigInteger i = BigInteger.valueOf(2); ; i = i.add(BigInteger.ONE)) {
			BigInteger pn = i.pow(n);
			if (pn.compareTo(x.abs()) > 0) {
				break;
			}
			BigInteger[] divrem = x.divideAndRemainder(pn);
			if (divrem[1].equals(BigInteger.ZERO)) {
				return new RootAndRemainder(i, divrem[0]);
			}
		}
		return new RootAndRemainder(x);
	}
}