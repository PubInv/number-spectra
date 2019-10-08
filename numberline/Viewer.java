
public class Viewer {
    final double A;
    final double B;
    final int N;
    
    public Viewer(double a, double b, int n) {
		A = a;
		B = b;
		N = n;
	}

	// linear transformation:
    // vp = 0 => A
    // vp = N => B
    public static double toNL(int vp, double A, double B, int N) {
    	// if vp == 0, 0 * (B - A) / N + A = 0 + A = A
    	// if vp == N, N * (B - A) / N + A = B - A + A = B
    	return vp * (B - A) / N + A;
    }
    
    // opposite of toNL
    public static int toVP(double nl, double A, double B, int N) {
    	return (int)Math.round(((nl - A) / (B - A)) * N);
    }
    
    /**
     * Given vpl and vph, calculate new A',B' such that
     * A' => vpl and B' => vph
     */
    public Viewer zoom(int vpl,int vph) {
    	return new Viewer(toNL(vpl, A, B, N),toNL(vph, A, B, N), N);
    }
    
    public Viewer unzoom(int vpl,int vph) {
        return zoom(-vpl, N + (N - vph));
    }
}
