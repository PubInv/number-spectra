package org.pubinv.numberspectra;

public class Times implements Expr {
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((lhs == null) ? 0 : lhs.hashCode());
		result = prime * result + ((rhs == null) ? 0 : rhs.hashCode());
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
		Times other = (Times) obj;
		if (lhs == null) {
			if (other.lhs != null)
				return false;
		} else if (!lhs.equals(other.lhs))
			return false;
		if (rhs == null) {
			if (other.rhs != null)
				return false;
		} else if (!rhs.equals(other.rhs))
			return false;
		return true;
	}

	final Expr lhs;
	final Expr rhs;

	private Times(Expr lhs, Expr rhs) {
		super();
		this.lhs = lhs;
		this.rhs = rhs;
	}

	public static Expr make(Expr lhs, Expr rhs) {
		if (lhs.equals(Const.NAN)) return lhs;
		if (rhs.equals(Const.NAN)) return rhs;
		
		if (lhs.equals(Const.ZERO)) return lhs;
		if (lhs.equals(Const.ONE)) return rhs;
		
		if (rhs.equals(Const.ZERO)) return rhs;
		if (rhs.equals(Const.ONE)) return lhs;
		
		if (lhs.isNegatable()) {
			return Negate.make(make(lhs.negate(), rhs));
		}
		if (rhs.isNegatable()) {
			return Negate.make(make(lhs, rhs.negate()));
		}
		if (rhs instanceof Times) {
			Times rtimes = (Times) rhs;
			return make(make(lhs, rtimes.lhs), rtimes.rhs);
		} else if (rhs instanceof Const) {
			Rational rr = ((Const) rhs).rational;

			// K + K
			if (lhs instanceof Const) {
				return new Const(((Const) lhs).rational.multiply(rr));
			} else if (lhs instanceof Times) {
				Times timesLhs = (Times) lhs;
				// (K + e) + K
				if (timesLhs.lhs instanceof Const) {
					return make(timesLhs.rhs, new Const(((Const) (timesLhs.lhs)).rational.multiply(rr)));
				}
				// (e + K) + K
				if (timesLhs.rhs instanceof Const) {
					return make(timesLhs.lhs, new Const(((Const) (timesLhs.rhs)).rational.multiply(rr)));
				}
			}
		}
		return new Times(lhs, rhs);
	}

	@Override
	public String toString() {
		return "(* " + lhs + " " + rhs + ")";
	}
	
	@Override
	public boolean isNegatable() {
		return false;
	}
	
	@Override
	public Expr negate() {
		return Negate.make(this);
	}
	
	@Override
	public double eval() {
		return lhs.eval() * rhs.eval();
	}
}
