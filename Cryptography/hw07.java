//https://code.google.com/a/eclipselabs.org/p/cryptocodes/source/browse/trunk/Crypto/src/
//https://sites.google.com/site/indy256/algo/euclid
//Run in Eclipse or Turing
import java.math.BigInteger;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.security.SecureRandom;
//import java.util.Arrays;

public class hw02 {
	//Define some BigInteger constants; this is handy for comparisons
	static final BigInteger ZERO = new BigInteger("0");
	static final BigInteger ONE = new BigInteger("1");
	static final BigInteger TWO = new BigInteger("2");
	static final BigInteger THREE = new BigInteger("3");
	static final BigInteger FOUR = new BigInteger("4");
	static final BigInteger FIVE = new BigInteger("5");
	static final BigInteger SEVEN = new BigInteger("7");
	static final BigInteger ELEVEN = new BigInteger("11");
	static long a1, a2;
	static long b1, b2;
	
	private static ArrayList<Integer> cache = null;
    static { cache = new ArrayList<Integer>(); cache.add(2); }

	private static long getLong() {
		Scanner input = new Scanner(System.in);
		do {
		    try {
		        return input.nextLong();
		    }
		    catch(InputMismatchException e) {
		    }
		    finally{
		    	input.nextLine(); // always advances (even after the break)
		    }
		    System.out.print("Input must be a number: ");
		} while(true);
	}
	
	public static long lpow(long base, long exp) {
	    long result = 1;
	    while(exp != 0) {
	        if((exp & 1) == 1)
	            result *= base;
	        exp >>= 1;
	        base *= base;
	    }
	    return result;
	}
	
	public static long power(long a, long m, long n) { // Find a^m mod n
		BigInteger base = BigInteger.valueOf(a);
		BigInteger exponent = BigInteger.valueOf(m);
		BigInteger module = BigInteger.valueOf(n);
		BigInteger p = base.modPow(exponent, module);
		return p.longValue();
	}
	
	public static long inverse(long a, long n) { // Find a^(-1) mod n
		BigInteger r1 = BigInteger.valueOf(a);
		BigInteger r2 = BigInteger.valueOf(n);
		BigInteger inv = r1.modInverse(r2);
		return inv.longValue();
	}
	
	public static long gcd(long a, long b) { // Find gcd(a,b);
		BigInteger r1 = BigInteger.valueOf(a);
		BigInteger r2 = BigInteger.valueOf(b);
		BigInteger g = r1.gcd(r2);
		return g.longValue();
	}
	
	public static long shank(long a, long b, long p) { //g^x = a mod p, p is a prime and x is returned
		int m = (int)Math.sqrt(p - 1);
		long[] first = new long[m];
		long ainv = inverse(a, p);
		first[0] = b;
		for (int i = 1; i < m; i++) first[i] = (first[i - 1] * ainv) % p;
		long am = power(a, m, p);
		long second = 1;
		long result = -1;
		for (int j = 0; j < m; j++ ) {
			second = power(am, j, p);
			for (int i = 0; i < m; i++) {
				if (second == first[i]) {
					result = (m * j + i);
					break;
				}
			}
			if (result>-1) break;
		}
		return result;
	}
	
	private static List<Long> primeFactors(long l) {
		long n = l;
		List<Long> primeFactors = new ArrayList<Long>();
		for(long i = 2; i <= n/i; i++) {
			while(n % i == 0) {
				primeFactors.add(i);
				n /= i;
			}
		}
		if(n > 1)
			primeFactors.add(n);
		return primeFactors;
	}
	
	private static long[] convertLongs(List<Long> integers) {
	    long[] ret = new long[integers.size()];
	    Iterator<Long> iterator = integers.iterator();
	    for (int i = 0; i < ret.length; i++)
	    {
	        ret[i] = iterator.next().intValue();
	    }
	    return ret;
	}
	
	public static long pohligHellman(long g, long a, long p) { //g^x = a mod p, p is a prime and x is returned
		BigInteger pHope = BigInteger.valueOf(p);
		long pKnown = p;
		if(primeProbability(pHope))
			pKnown = p - 1;
		List<Long> primefactors = primeFactors(pKnown);
		List<Long> primefactor = new ArrayList<Long>();
		List<Long> listX = new ArrayList<Long>();
		primefactor.add(primefactors.get(0));
		int k = 1, l = 1, z = 1; long count = 1;
		listX.add(count);
		while(l < primefactors.size()) {
			if(primefactor.get(k - 1) != primefactors.get(l)) {
				z++;
				count = 1;
				listX.add(count);
				primefactor.add(primefactors.get(l));
				k++;
			}
			else {
				count++;
				listX.set(z - 1, count);
			}
			l++;
		}
		
		long[] primes = convertLongs(primefactor);
		long[] blah = convertLongs(listX);
		long[] blah3 = new long[primes.length];
		long[] divPri = new long[primes.length];
		for (int i=0; i<primes.length; i++) {
			divPri[i] = 1;
			for(int j = 0; j < blah[i]; j++) {
				divPri[i] = divPri[i] * primes[i];
			}
			
			long e = (p-1) / divPri[i];
			long betaPow = power(a, e, p);
			long alphaPow = power(g, e, p);
			
			for (long j = 0; j < divPri[i]; j++) {
				long a1 = power(alphaPow, j, p);
				if (a1 == betaPow) {
					blah3[i] = j; 
					break;
				}
			}
		}
		
		//Chinese Remainder Theorem
		long M = 1;
		long[] m = new long[blah3.length];
		for(int i = 0; i < primes.length; i++) {
			for(int j = 0; j < blah[i]; j++) { 
				M = M * primes[i];
			}
		}
		for(int i = 0; i < primes.length; i++) {
			m[i] = inverse(M / divPri[i], divPri[i]);
		}
		
		long answer = 0;
		for(int i = 0; i < blah3.length; i++) {
			answer = (answer + blah3[i] * m[i] * M / divPri[i]) % M;
		}
		return answer;
	}

	public static boolean isPrime(int i) { //primes that are no greater than int.
        if (i == 1) return false; // by definition
		int max = (int) Math.floor(Math.sqrt(2000000));
        int limit = (int) Math.floor(Math.sqrt(i));
        if(cache == null) { populateCache(max); }
			populateCache(max); //only needs to run once for all low powers
        return doTest(i, limit);
    }
	
	private static void populateCache(int limit) { //isPrime use, only need to populateCache once.
        int start = cache.get(cache.size() - 1) + 1;
        for(int i = start; i <= limit; i++) {
            if(simpleTest(i)) cache.add(i);
        }
    }
	
	private static boolean simpleTest(int i) { //populateCache use
        int limit = (int) Math.floor(Math.sqrt(i));
        return doTest(i, limit);
    }
	
	private static boolean doTest(int i, int limit) { //isPrime & simpleTest use
        int counter = 0;
        while (counter < cache.size() && cache.get(counter) <= limit) {
            if (i % cache.get(counter) == 0) return false;
            counter++;
        }
        return true;
    }
	
	public static boolean primeProbability(BigInteger n) { //Implements the Rabin-Miller test.
		SecureRandom sr = new SecureRandom();
		int numPasses = 10;
		if(n.compareTo(TWO) < 0)
			return false;
		BigInteger b, x;
		BigInteger nMinusOne = n.subtract(ONE);
		if(numPasses < 1) throw new IllegalArgumentException("Number of bases must be positive!");
		//If the number is small, just factor it
		if(n.compareTo(new BigInteger("2000000")) < 0) {
			int smalln = n.intValue();
			if(isPrime(smalln)) return true;
			return false;
		}
		for(int i = 0; i < numPasses; i++) { //miller-rabin
			//Choose a random base smaller than n
			b = new BigInteger(n.bitLength() - 1, sr);
			//Test Fermat's condition first
			x = b.modPow(nMinusOne,n);
			if(!x.equals(ONE)) return false;//not prime
			//Divide n-1 by 2
			BigInteger[] dr = nMinusOne.divideAndRemainder(TWO);
			//Perform the root tests
			while(dr[1].equals(ZERO)) {
				x = b.modPow(dr[0], n);
				//if you get -1, this is a PASS; get out
				if(x.equals(nMinusOne)) break;//pass
				//Now, if its not -1 or 1, this is a FAIL, return 0
				if(!x.equals(ONE)) return false;//not prime
				//If its 1, so far its a pass
				//We can continue with the test; divide by 2
				dr=dr[0].divideAndRemainder(TWO);
			}
		}
		//Only way to get here is by passing all tests
		return true;
	}
	
	//Computes the least nonnegative residue of b mod m, where m>0.
	public static BigInteger lnr(BigInteger b, BigInteger m) {
		if(m.compareTo(ZERO) <= 0)
			throw new IllegalArgumentException("Modulus must be positive.");
		BigInteger answer = b.mod(m);
		return(answer.compareTo(ZERO) < 0) ? answer.add(m):answer;
	}
	
	//Pollard p-1 factorization-runs until a factor is found
	public static BigInteger pMinusOneFactor(BigInteger n) throws IllegalArgumentException {
		SecureRandom rand = new SecureRandom();
		BigInteger power = BigInteger.valueOf(1);
		BigInteger residue = lnr(BigInteger.valueOf(rand.nextInt()), n);
		BigInteger test = residue.subtract(ONE);
		BigInteger gcd = test.gcd(n);
		while(true) {
			while(gcd.equals(ONE)) {
				power = power.add(ONE);
				residue = residue.modPow(power, n);
				test = residue.subtract(ONE);
				gcd = test.gcd(n);
			}
			if(gcd.equals(n)) {
				power = BigInteger.valueOf(1);
				residue = lnr(BigInteger.valueOf(rand.nextInt()), n);
				test = residue.subtract(ONE);
				gcd = test.gcd(n);
			}
			else return gcd;
		}
	}
	
	private static long[] newXab(long[] xab, long alpha, long beta, long p) {
		long num = xab[0] % 3, pMinOne = p - 1;
		long[] XAB = xab;
		//System.out.println("num1: " + num + " x: " + xab[0] + " aa: " + xab[1] + " b: " + xab[2] + " X: " + XAB[0] + " A: " + XAB[1] + " B: " + XAB[2]);
		if(num == 0) {
			XAB[0] = (xab[0] * xab[0]) % p;
			XAB[1] = (xab[1] * 2) % pMinOne;
			XAB[2] = (xab[2] * 2) % pMinOne;
		}
		else if(num == 1) {
			XAB[0] = (xab[0] * alpha) % p;
			XAB[1] = (xab[1] + 1) % pMinOne;
		}
		else if(num == 2) {
			XAB[0] = (xab[0] * beta) % p;
			XAB[2] = (xab[2] + 1) % pMinOne;
		}
		//System.out.println("num2: " + num + " x: " + xab[0] + " aa: " + xab[1] + " b: " + xab[2] + " X: " + XAB[0] + " A: " + XAB[1] + " B: " + XAB[2]);
		return XAB;
	}
	
	public static long pollardsRho(long g, long a, long p) { //g^x = a mod p, p is a prime and x is returned
		BigInteger pHope = BigInteger.valueOf(p);
		if(!primeProbability(pHope) || g < 1 || a < 1)
			return -1;
		long[] xab = {1, 0, 0};
		long[] XAB = {xab[0], xab[1], xab[2]};
		//System.out.print("\n");
		for(long i = 1; i < (p-1); i++) {
			xab = newXab(xab, g, a, p);
			//System.out.println("i1-" + i + " x: " + xab[0] + " aa: " + xab[1] + " b: " + xab[2] + " X: " + XAB[0] + " A: " + XAB[1] + " B: " + XAB[2]);
			XAB = newXab(XAB, g, a, p);
			//System.out.println("i2-" + i + " x: " + xab[0] + " aa: " + xab[1] + " b: " + xab[2] + " X: " + XAB[0] + " A: " + XAB[1] + " B: " + XAB[2]);
			XAB = newXab(XAB, g, a, p);
			//System.out.println("i3-" + i + " x: " + xab[0] + " aa: " + xab[1] + " b: " + xab[2] + " X: " + XAB[0] + " A: " + XAB[1] + " B: " + XAB[2] + "\n");
			if(xab[0] == XAB[0]) {
				//long x = power(g, xab[0], p);
				//long y = power(xab[1], xab[2], p) * power(XAB[1], XAB[2], p);
				//if(x == y)
					return xab[0];
			}
		}
		return -1;
	}

	private static long LdoubleAndAdd(long N, int xNum) { //double and add algorithm
		long lambda = 0;
		long inverse = 0;
		
		if (a1 == 0 & b1 == 0) { }
		else if (a2 == 0 & b2 == 0) {
			a2 = a1;
			b2 = b1;
		} else if (a1 == a2 & b1 == (b2 * -1)) {
			a2 = 0;
			b2 = 0;
		}
		else {
			if(a1 == a2 & b1 == b2) {
				inverse = inverse((2 * b1), N);
				if(inverse == 0) {
					return a2 - a1;
				}
				lambda = inverse * (a1 * a1 * 3 + xNum) % N;
			}
			else {
				inverse = inverse((a2 - a1), N);
				if(inverse == 0) {
					return a2 - a1;
				}
				lambda = (b2 - b1 + N) % N;
				lambda = (lambda * inverse + N) % N;
			}
			a2 = ((lambda * lambda) % N - a1 - a2 + N) % N;
			b2 = ((lambda * ((a1 - a2 + N) % N)) % N - b1 + N) % N;
		}
		return 0;
	}
	
	private static long doubleAndAdd(BigInteger N, int xNum) { //double and add algorithm
		long lambda = 0;
		long inverse = 0;
		
		if (a1 == 0 & b1 == 0) { }
		else if (a2 == 0 & b2 == 0) {
			a2 = a1;
			b2 = b1;
		} else if (a1 == a2 & b1 == (b2 * -1)) {
			a2 = 0;
			b2 = 0;
		}
		else {
			if(a1 == a2 & b1 == b2) {
				inverse = inverse((2 * b1), N);
				if(inverse == 0) {
					return a2 - a1;
				}
				lambda = inverse * (a1 * a1 * 3 + xNum) % N;
			}
			else {
				inverse = inverse((a2 - a1), N);
				if(inverse == 0) {
					return a2 - a1;
				}
				lambda = (b2 - b1 + N) % N;
				lambda = (lambda * inverse + N) % N;
			}
			a2 = ((lambda * lambda) % N - a1 - a2 + N) % N;
			b2 = ((lambda * ((a1 - a2 + N) % N)) % N - b1 + N) % N;
		}
		return 0;
	}
	
	private static BigInteger inverseNum(long g, long p) {
		if(g < 0)
			g += p;
		for (BigInteger i = ONE; i < p; i++) {
			if ((g * i) % p == 1) {
				return i;
			}
		}
		return ZERO;
	}

	private static BigInteger[] extendedEuclid(BigInteger a, BigInteger num) {
		BigInteger[] answer = new BigInteger[3];
		if(num.compareTo(ZERO) != 0) {
			BigInteger q = a.divide(num);
			answer = extendedEuclid(num, a.mod(num));
			BigInteger temp = answer[1].subtract(answer[2].multiply(q));
			answer[1] = answer[2];
			answer[2] = temp;
		}
		else {
			answer[0] = a;
			answer[1] = ONE;
			answer[2] = ZERO;
		}
		return answer;
	}
	
	public static long ellipCurve(long a, long b, int xMul, int mod, BigInteger num) { //B ≡ b2 − a3 − A · a (mod N)
		a1 = a2 = a; b1 = b2 = b;
		if(num.compareTo(new BigInteger("1386495")) < 0) {
			long[] ans = new long[3];
			long result;
			long n = num.longValueExact();
			do { //result should not be 0 at some point
				result = LdoubleAndAdd(n, xMul);
				if(result != 0) {
					if(result < 0)
						ans[0] = gcd(result + n, n);
					else
						ans[0] = gcd(result, n);
					return ans[0];
				}
			} while(true); //been awhile since last used a do/while so why not and in this case it is the same as a while
		}
		else {
			BigInteger[] ans = new BigInteger[3];
			BigInteger result;
			while(true) {
				result = BigInteger.valueOf(doubleAndAdd(num, xMul));
				if(result.compareTo(ZERO) != 0) {
					if(result.compareTo(ZERO) < 0)
						ans = extendedEuclid(result.add(num), num);
					else
						ans = extendedEuclid(result, num);
					return ans[0].longValueExact();
				}
			}
		}
	}
	
	public static void main(String[] args) {
		long g = 0, a = 0, p = 0; //BigInteger.valueOf(a);
		int count = 1;
		populateCache(2000000);
		while(count > 0 && count < 6) {
			System.out.println("Types by things to calculate.");
			System.out.print("1. Shank's\n2. Pohlig-Hellman\n3. Miller-Rabin\n");
			System.out.print("4. Pollard’s p - 1 test\n5. Pollard's Rho\n6. Lenstra Elliptic Factorization");
			System.out.print("Any other number to quit\n");
			System.out.print("Please enter a number for what you want: ");
			count = (int) getLong();
			
			if(count == 1) { //Shank's
				for(int num = 0; num < 3; num++) {
					if(num == 0) {
						//(a) 11^x = 21 in F71. x = 37
						g = 11; a = 21; p = 71;
					}
					else if(num == 1) {
						//(b) 156x = 116 in F593. x = 59
						g = 156; a = 116; p = 593;
					}
					else if(num == 2) {
						//(c) 650x = 2213 in F3571. x = 319
						g = 650; a = 2213; p = 3571;
					}
					System.out.print(num + " g: " + g); System.out.print(" a: " + a); System.out.print(" p: " + p);
					System.out.println(" x = " + shank(g, a, p));
					System.out.println("test:" + power(g, shank(g, a, p), p));
				}
			}
			else if(count == 2) { //Pohlig-Hellman
				for(int num = 0; num < 3; num++) {
					if(num == 0) {
						//(a) p = 433, g = 7, a = 166. x = 47
						System.out.print("a. ");
						g = 7; a = 166; p = 433;
					}
					else if(num == 1) {
						//(b) p = 746497, g = 10, a = 243278. x = 223755
						System.out.print("b. ");
						g = 10; a = 243278; p = 746497;
					}
					else if(num == 3) {
						System.out.print("c. ");
						//(c) p = 1291799, g = 17, a = 192988. (Hint. p - 1 has a factor of 709.) x = 984414
						g = 2; a = 39183497; p = 41022299;
					}
					else if(num == 2){
						//(d) p = 1291799, g = 17, a = 192988. (Hint. p - 1 has a factor of 709.) x = 984414
						System.out.print("d. ");
						g = 17; a = 192988; p = 1291799;
					}
					System.out.print("g: " + g); System.out.print(" a: " + a); System.out.print(" prime: " + p);
					long reference, finishm, pohHel, sha;
					reference = System.nanoTime();
					pohHel = pohligHellman(g, a, p);
					finishm = System.nanoTime();
					System.out.println(" x = " + pohHel);
					System.out.println(((double)(finishm-reference)) / 1000000000.0 + "secs");  //in seconds
					System.out.println("test1: " + power(g, pohHel, p));
					reference = System.nanoTime();
					sha = shank(g, a, p);
					finishm = System.nanoTime();
					System.out.println("shank x = " + sha);
					System.out.println(((double)(finishm-reference)) / 1000000000.0 + "secs");  //in seconds
					System.out.println("test2: " + power(g, sha, p));
				}
			}
			else if(count == 3) { //primeProbability
				int count1 = 0;
				long reference, finishm;
				boolean prob;
				/* (a) n = 1105. (Yes, 5 divides n, but this is just a warm-up exercise!)
				(b) n = 294409 (c) n = 294439
				(d) n = 118901509 (e) n = 118901521
				(f) n = 118901527 (g) n = 118915387 */
				while(count1 < 7) {
					if(count1 == 0)
						p = 1105;
					else if(count1 == 1)
						p = 294409;
					else if(count1 == 2)
						p = 294439;
					else if(count1 == 3)
						p = 118901509;
					else if(count1 == 4)
						p = 118901521;
					else if(count1 == 5)
						p = 118901527;
					else if(count1 == 6)
						p = 321197185;
					BigInteger pHope = BigInteger.valueOf(p);
					reference = System.nanoTime();
					prob = primeProbability(pHope); //p is BigInteger, g is intNum of test, a is a rnd num
					finishm = System.nanoTime();
					System.out.println("num: " + pHope + " is " + prob + " with a time of " + ((double)(finishm-reference)) / 1000000000.0 + "secs");  //in seconds
					count1++;
				}
			}
			else if(count == 4) { //pMinusOneFactor
				int count1 = 0;
				g = 10;
				long reference, finishm;
				BigInteger pMinOne;
				while(count1 < 3) {
					/* (a) n = 1739 (b) n = 220459 (c) n = 48356747 */
					if(count1 == 0)
						p = 1739;
					else if(count1 == 1)
						p = 220459;
					else if(count1 == 2)
						p = 48356747;
					BigInteger pHope = BigInteger.valueOf(p);
					reference = System.nanoTime();
					pMinOne = pMinusOneFactor(pHope); //p is BigInteger, g is intNum of test, a is a rnd num
					finishm = System.nanoTime();
					System.out.println(((double)(finishm-reference)) / 1000000000.0 + "secs");  //in seconds
					System.out.println("num: " + pHope + " pMinOne: " +  pMinOne.longValue());
					count1++;
				}
			}
			else if(count == 5) { //Pollard's Rho
				for(int num = 0; num < 5; num++) {
					if(num == 0) {
						System.out.print("a. ");
						g = 2; a = 2495; p = 5011;
					}
					else if(num == 1) {
						System.out.print("b. ");
						g = 17; a = 14226; p = 17959;
					}
					else if(num == 2){
						System.out.print("d. ");
						g = 5953042; a = 5953042; p = 15239131;
					}
					else if(num == 3) {
						System.out.print("test1 37869. ");
						g = 19; a = 24717; p = 48611;
					}
					else if(num == 4) {
						System.out.print("test2 1010. ");
						g = 2; a = 5; p = 1019;
					}
					System.out.print("g: " + g); System.out.print(" a: " + a); System.out.print(" prime: " + p);
					long reference, finishm, polRho;
					reference = System.nanoTime();
					polRho = pollardsRho(g, a, p);
					finishm = System.nanoTime();
					System.out.print(" x = " + polRho + " time: ");
					System.out.println(((double)(finishm-reference)) / 1000000000.0 + "secs");  //in seconds
				}
			}
			else if(count == 6) { //Lenstra's Elliptic Factorization
				int B = 0, A = 0;
				BigInteger Num = new BigInteger("73"); //have almost always liked the number 73 for multi-reasons my whole life. It was not till I was about 21 that I found some of the more complex math for more reasons why.
				for(int num = 0; num < 6; num++) {
					if(num == 2) {
						System.out.print("a. ");
						a = 2; g = 5; A = 4; B = 9; Num = new BigInteger("589");
					}
					else if(num == 3) {
						System.out.print("b. ");
						a = 2; g = 12; A = 4; B = 128; Num = new BigInteger("26167");
					}
					else if(num == 4){
						System.out.print("c. ");
						a = 1; g = 1; A = 3; B = -3; Num = new BigInteger("1386493");
					}
					else if(num == 5) {
						System.out.print("d. ");
						a = 7; g = 4; A = 18; B = -453; Num = new BigInteger("2810284457");
					}
					else if(num == 0) {
						System.out.print("test book pg304. ");
						a = 38; g = 112; A = 3; B = 7; Num = new BigInteger("187");
					}
					else if(num == 1) {
						System.out.print("test book pg304. ");
						a = 1512; g = 3166; A = 14; B = 19; Num = new BigInteger("6887");
					}
					System.out.print("a: " + a); System.out.print(" b: " + g); System.out.print(" A: " + A); System.out.print(" B: " + B); System.out.print(" N: " + Num);
					long reference, finishm, ellCur;
					reference = System.nanoTime();
					ellCur = ellipCurve(a, g, A, B, Num); //g == b & p == N, just alreay had them so might as well
					finishm = System.nanoTime();
					System.out.print(" gcd = " + ellCur + " time: ");
					System.out.println(((double)(finishm-reference)) / 1000000000.0 + "secs");  //in seconds
				}			
			}
			System.out.println("");
		}
		return;
		lambda = inverse.multiply(BigInteger.valueOf(a1)).multiply(BigInteger.valueOf(a1)).multiply(THREE))% N;
	}
}

