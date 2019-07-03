package org.pubinv.numberspectra;

public final class Power implements Expr {
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + lhs.hashCode();
		result = prime * result + rhs.hashCode();
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
		Power other = (Power) obj;
		if (!rhs.equals(other.rhs))
			return false;
		return true;
	}
	final Expr lhs;
	final Expr rhs;
	private Power(Expr lhs, Expr rhs) {
		super();
		this.lhs = lhs;
		this.rhs = rhs;
	}
	
	public static Expr make(Expr lhs, Expr rhs) {
		if (lhs.equals(Const.NAN)) return lhs;
		if (rhs.equals(Const.NAN)) return rhs;
		
		// A ^ 1 = A
		if (rhs.equals(Const.ONE)) return lhs;
		// A ^ 0 = 1
		if (rhs.equals(Const.ZERO)) return Const.ONE;
		
		// A ^ (-B)  = 1 / (A ^ B)
		if (rhs.isNegatable()) {
			return Reciprocal.make(make(lhs, rhs.negate()));
		}
		
		if (lhs instanceof Const && rhs instanceof Const) {
			Const lhsConst = (Const) lhs;
			Const rhsConst = (Const) rhs;
			Rational rl = lhsConst.rational;
			Rational rr = rhsConst.rational;
			if (rr.isInteger() && rr.compareTo(Rational.of(10)) < 0) {
				int xp = rr.intValue();
				return new Const(Rational.of(rl.p.pow(xp), rl.q.pow(xp)));
			}
		}
		
		// 1 ^ B = 1
		if (lhs.equals(Const.ONE)) return lhs;
		
		if (rhs.equals(Const.NEGATIVE_INFINITY) || rhs.equals(Const.PLUS_INFINITY)) {
			return rhs;
		}
		if (lhs.equals(Const.NEGATIVE_INFINITY) || lhs.equals(Const.PLUS_INFINITY)) {
			return lhs;
		}
		
		return new Power(lhs, rhs);
	}
	

	
	@Override
	public String toString() {
		return "(** "+lhs+" "+rhs+")";
	}
	
	public static void main(String[] args) {
		System.out.println(Math.pow(0.0, -1));
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
		return Math.pow(lhs.eval(), rhs.eval());
	}
}
