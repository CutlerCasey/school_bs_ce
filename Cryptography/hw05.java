//Run in Eclipse or Turing
import java.math.BigInteger;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.security.SecureRandom;
//import java.util.Arrays;

public class hw05 {
	//Define some BigInteger constants; this is handy for comparisons
	static final BigInteger ZERO = new BigInteger("0");
	static final BigInteger ONE = new BigInteger("1");
	static final BigInteger TWO = new BigInteger("2");
	static final BigInteger THREE = new BigInteger("3");
	static final BigInteger FOUR = new BigInteger("4");
	static final BigInteger FIVE = new BigInteger("5");
	static final BigInteger SEVEN = new BigInteger("7");
	static final BigInteger ELEVEN = new BigInteger("11");
	
	private static ArrayList<Integer> cache = null;
    static {
        cache = new ArrayList<Integer>();
        cache.add(2);
    }

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
		SecureRandom sr1 = new SecureRandom();
		BigInteger pHope = BigInteger.valueOf(p);
		long pKnown = p;
		if(primeProbability(pHope, sr1))
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
	
	public static boolean primeProbability(BigInteger n, SecureRandom sr) { //Implements the Rabin-Miller test.
		int numPasses = 100;
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
	
	public static void main(String[] args) {
		long g = 0, a = 0, p = 0; //BigInteger.valueOf(a);
		int count = 1;
		populateCache(2000000);
		while(count > 0 && count < 7) {
			System.out.println("Types by things to calculate.");
			System.out.print("1. Shank's\n2. Pohlig-Hellman\n3. Miller-Rabin\n");
			System.out.print("4. Pollard’s p - 1 test\n");
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
				SecureRandom sr1 = new SecureRandom();
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
					prob = primeProbability(pHope, sr1); //p is BigInteger, g is intNum of test, a is a rnd num
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
			else if(count == 5) {//3.33
				a = 11; p = 493;
				for(g = 23; g < 38; g++) {
					
				}
			}
			else if(count == 6) { //3.35
				a = 17; p = 19079;
				for(int i = 0; i < 3; i++) {
					if(i == 0)
						g = 3030; //i
					else if(i == 1)
						g = 6892;
					else if(i == 2)
						g = 18312;
					long blah = power(a, g, p);
					System.out.println("a" + i + ".power(" + a + ", " + g + ", " + p + ") = " + blah);
					for(int j = 0; j < 3; j++) {
						int blah2 = 0;
						if(j == 0) blah2 = 2;
						else if(j == 1) blah2 = 3;
						else if(j == 2) blah2 = 5;
						long blah3 = pohligHellman(blah, blah2, p -1); //g^x = a mod p
						System.out.println("b" + i + "-" + blah2 + ". " + blah3);
					}
				}
			}
			else if(count == 7) { //3.41
				for(int i = 0; i < 3; i++) {
					a = 56843; p = 32411;
					if(i == 0)
						g = 1794677960;
					else if(i == 1)
						g = 525734818;
					else if(i == 2)
						g = 420526487;
					System.out.print("a" + i + ". jacabian(" + g + " / " + a + ") = ");
					if(power(g, (p - 1) / 2, p) == 1) {
						System.out.println(1);
					}
					else {
						System.out.println(0);
					}
					
					a = 2013; p = 3149;
					List<Long> primeFact = primeFactors(p);
					if(i == 0)
						g = 2322;
					else if(i == 1)
						g = 719;
					else if(i == 2) {
						g = 202;
					}
					if(power(g, (primeFact.get(0) - 1) / 2, primeFact.get(0)) == 1) {
						System.out.print("b" + i + ". power(" + g + ", 2, " + primeFact.get(0) + ") = ");
						System.out.println(power(g, 2, primeFact.get(0)));
					}
					else {
						double ab = Math.sqrt(a);
						System.out.print("b" + i + ". " + a + " * " + g + " ^ 2 mod(" + primeFact.get(0) + ") = ");
						System.out.println(power((long) (ab * g), 2, primeFact.get(0)));
					}
					
					a = 568980706; p = 781044643;
					int j = 1;
					if(i == 0)
						g = 705130839;
					else if(i == 1)
						g = 631364468;
					else if(i == 2) {
						g = 67651321; j = 0;
					}
					if(j == 1) {
						System.out.print("c" + i + ". power(" + g + ", 2, " + p + ") = ");
						System.out.println(power(g, 2, p));
					}
					else {
						double ab = Math.sqrt(a);
						System.out.print("c" + i + ". " + a + " * " + g + " ^ 2 mod(" + p + ") = ");
						System.out.println(power((long) (ab * g), 2, p));
					}
				}
			}
			System.out.println("");
		}
		return;
	}
}

