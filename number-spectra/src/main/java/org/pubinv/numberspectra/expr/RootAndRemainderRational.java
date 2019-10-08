package org.pubinv.numberspectra.expr;

import java.math.BigInteger;

import org.pubinv.numberspectra.Rational;

public class RootAndRemainderRational {
	public final Rational rem;
	public final Rational root;
	
	public RootAndRemainderRational(Rational root, Rational rem) {
		super();
		this.rem = rem;
		this.root = root;
	}
	public RootAndRemainderRational(Rational rem) {
		super();
		this.rem = rem;
		this.root = Rational.ONE;
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
		RootAndRemainderRational other = (RootAndRemainderRational) obj;
		return rem.equals(other.rem) && root.equals(other.root);
	}

	@Override
	public String toString() {
		return "RootAndRemainderRational [rem=" + rem + ", root=" + root + "]";
	}

	public static RootAndRemainderRational extractRoot(Rational x, BigInteger n) {
		RootAndRemainder rp = RootAndRemainder.extractRoot(x.p, n);
		RootAndRemainder rq = RootAndRemainder.extractRoot(x.q, n);
		return new RootAndRemainderRational(Rational.of(rp.root, rq.root), Rational.of(rp.rem, rq.rem));
	}
}