/**
* Assignment 1.1.24
*/
public class Euclid {

	public static int gcd(int p, int q) {
		System.out.println(p + ", " + q);
		if(q == 0)
			return p;
		int r = p % q;
		return gcd(q, r);
	}

	public static void main(String[] args) {
		System.out.println(gcd(Integer.parseInt(args[0]),Integer.parseInt(args[1])));
	}
}