//Run in Eclipse or Turing
import java.math.BigInteger;

public class hw02 {
	public static long power(long a, long m, long n) {
		// Find a^m mod n
		BigInteger base = BigInteger.valueOf(a);
		BigInteger exponent = BigInteger.valueOf(m);
		BigInteger module = BigInteger.valueOf(n);
		BigInteger p = base.modPow(exponent, module);
		return p.longValue();
	}
	public static long inverse(long a, long n) {
		// Find a^(-1) mod n
		BigInteger r1 = BigInteger.valueOf(a);
		BigInteger r2 = BigInteger.valueOf(n);
		BigInteger inv = r1.modInverse(r2);
		return inv.longValue();		
	}
	public static long gcd(long a, long b) {
		// Find gcd(a,b);
		BigInteger r1 = BigInteger.valueOf(a);
		BigInteger r2 = BigInteger.valueOf(b);
		BigInteger g = r1.gcd(r2);
		return g.longValue();
	}
	
	// shank's algorithm
	public static long shank(long a, long b, long p) {
		// To find x such that a^x = b mod p, p is a prime
		int m = (int)Math.sqrt(p - 1);
		long[] first = new long[m];
		long ainv = inverse(a, p);
		first[0] = b;
		for (int i = 1; i < m; i++) first[i] = (first[i - 1] * ainv) % p;
		long am = power(a, m, p);
		long second = 1;
		long result = -1;
		System.out.println("m = " + m);
		System.out.println("a^m = " + am);
		System.out.println("a inverse = " + ainv);
		for (int j = 0; j < m; j++ ) {
			second = power(am, j, p);
			for (int i = 0; i < m; i++) {
				if (second == first[i]) {
					System.out.println("i = " + i);
					System.out.println("j = " + j);
					result = (m * j + i);
					break;
				}
			}
			if (result>-1) break;
		}
		return result;
	}
	
	public void PrimeSieve {
		int N = Integer.parseInt(args[0]);
		// initially assume all integers are prime 
		boolean[] isPrime = new boolean[N + 1]; 
		for (int i = 2; i <= N; i++) {
			isPrime[i] = true;
		}
		// mark non-primes <= N using Sieve of Eratosthenes
		for (int i = 2; i*i <= N; i++) {
			// if i is prime, then mark multiples of i as nonprime
			// suffices to consider mutiples i, i+1, ..., N/i
			if (isPrime[i]) {
				for (int j = i; i*j <= N; j++) {
					isPrime[i*j] = false;
				}
			}
		}
		// count primes
		int primes = 0;
		for (int i = 2; i <= N; i++) {
			if (isPrime[i]) primes++;
		}
		System.out.println("The number of primes <= " + N + " is " + primes);
	}
	
	public void PrimeSieve {
		// A sieve method for finding prime numbers goes as follows:
        // Keep a list of all possible primes from 0 to 1000.
        // We don't really need entries for 0 and 1, but we'll just
        // ignore them, since it makes indexing easier.
        boolean[] isPrime = new boolean[1001];
        
        // Initialize all entries to true; we'll set to false once we've
        // proved that it's not prime.
        Arrays.fill(isPrime, true);
        
        // Then for each value (from 2 on up), cross off all multiples
        // of that value in our list.
        for (int test = 2; test < isPrime.length; test++) {
            if (isPrime[test]) {
                for (int nonprime = 2 * test; nonprime < isPrime.length; nonprime = nonprime + test) {
                    isPrime[nonprime] = false;
                }
            }
        }
        
        // After crossing off all multiples, what's left has to be prime.
        for (int test = 2; test < isPrime.length; test++) {
            if (isPrime[test]) {
                System.out.println(test);
            }
        }
    }
	
	private void beginSieve(int mLimit) {
		primeList = new BitSet(mLimit>>1);
		primeList.set(0,primeList.size(),true);
		int sqroot = (int) Math.sqrt(mLimit);
		primeList.clear(0);
		for(int num = 3; num <= sqroot; num+=2) {
			if(primeList.get(num >> 1)) {
				int inc = num << 1;
				for(int factor = num * num; factor < mLimit; factor += inc) {
					//if( ((factor) & 1) == 1) { 
                    primeList.clear(factor >> 1); 
                //} 
				} 
			} 
		} 
	} 
	
	public boolean isPrime(int num) {
		if( num < maxLimit) {
			if( (num & 1) == 0)
				return ( num == 2);
			else
				return primeList.get(num>>1);
		}
		return false;
	}
	
	public static void main(String[] args) {
		BigInteger a, b, c; //BigInteger.valueOf(a);
		int count = 1;
		while(count > 0 && count < 9) {
			Scanner input = new Scanner(System.in);
			System.out.println("1. Shank's");
			System.out.println("Please enter a number for what you want.");
			count = Int.parseInt(str);
			
			if(count == 1) { //Shank's
				//(a) 11x = 21 in F71. x = 37
				System.out.println("b^x = sf in Fprime");
				System.out.print("Give a base: "); a = Long.parseLong(str);
				System.out.print("Give a mod: "); b = Long.parseLong(str);
				System.out.print("Give a prime: "); c = Long.parseLong(str);
				System.out.print("b: " + BigInteger.valueOf(a)); System.out.print(" sf: " + BigInteger.valueOf(b)); System.out.print(" prime: " + BigInteger.valueOf(c));
				System.out.println(" x = " + shank(BigInteger.valueOf(a), BigInteger.valueOf(b), BigInteger.valueOf(c)));
			}
			
			System.out.println("");
		}
	}
}