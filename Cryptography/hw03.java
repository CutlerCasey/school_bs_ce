//Run in Eclipse or Turing
import java.math.BigInteger;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.security.SecureRandom;

public class hw02 {
	//Define some BigInteger constants; this is handy for comparisons
	static final BigInteger ZERO=new BigInteger("0");
	static final BigInteger ONE=new BigInteger("1");
	static final BigInteger TWO=new BigInteger("2");
	static final BigInteger THREE=new BigInteger("3");
	static final BigInteger FOUR=new BigInteger("4");
		
	public static long getLong() {
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
	
	public static long shank(long a, long b, long p) { //g^x = a mod p, p is a prime and x is returned
		int m = (int)Math.sqrt(p - 1);
		long[] first = new long[m];
		long ainv = inverse(a, p);
		first[0] = b;
		for (int i = 1; i < m; i++) first[i] = (first[i - 1] * ainv) % p;
		long am = power(a, m, p);
		long second = 1;
		long result = -1;
		//System.out.println("m = " + m);
		//System.out.println("a^m = " + am);
		//System.out.println("a inverse = " + ainv);
		for (int j = 0; j < m; j++ ) {
			second = power(am, j, p);
			for (int i = 0; i < m; i++) {
				if (second == first[i]) {
					//System.out.println("i = " + i);
					//System.out.println("j = " + j);
					result = (m * j + i);
					break;
				}
			}
			if (result>-1) break;
		}
		return result;
	}
	
	public static List<Long> primeFactors(long l) {
		long n = l;
		List<Long> primeFactors = new ArrayList<Long>();
		for (long i = 2; i <= n/i; i++) {
			while (n % i == 0) {
				primeFactors.add(i);
				n /= i;
			}
		}
		if(n > 1)
			primeFactors.add(n);
		return primeFactors;
	}
	
	public static long[] convertLongs(List<Long> integers) {
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
		if(primeProbability(pHope, 10, sr1) > .5)
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
	
	//Implements the Rabin-Miller test.
	//Number of different bases to try is passed in as an int
	//If the BigInteger passes all tests, returns the probability it is prime as a double.
	//Returns zero if the BigInteger is determined to be composite.
	public static double primeProbability(BigInteger n,  int numPasses, SecureRandom sr) {
		if(n.compareTo(TWO) < 0)
			return 0;
		BigInteger b, x;
		BigInteger nMinusOne = n.subtract(ONE);
		if(numPasses < 1) throw new IllegalArgumentException("Number of bases must be positive!");
		//If the number is small, just factor it
		if (n.compareTo(new BigInteger("2000000")) < 0) {
			int smalln = n.intValue();
			for(int i = 2; i <= Math.sqrt(smalln); i++)
				if(smalln % i == 0)
					return 0;
			return 1;
		}
		for(int i = 0; i < numPasses; i++) {
			//Choose a random base smaller than n
			b = new BigInteger(n.bitLength() - 1, sr);
			//Test Fermat's condition first
			x = b.modPow(nMinusOne,n);
			if(!x.equals(ONE)) return 0.0;//not prime
			//Divide n-1 by 2
			BigInteger[] dr = nMinusOne.divideAndRemainder(TWO);
			//Perform the root tests
			while(dr[1].equals(ZERO)) {
				x = b.modPow(dr[0], n);
				//if you get -1, this is a PASS; get out
				if(x.equals(nMinusOne)) break;//pass
				//Now, if its not -1 or 1, this is a FAIL, return 0
				if(!x.equals(ONE)) return 0.0;//not prime
				//If its 1, so far its a pass
				//We can continue with the test; divide by 2
				dr=dr[0].divideAndRemainder(TWO);
			}
		}
		//Only way to get here is by passing all tests
		return 1.0 - Math.pow(0.25, numPasses);
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
	
	public static Biginteger gcdAB(long num1, long num2, long num3) {
		List<Long> primefactors = primeFactors(num1); int z = 0, count = 0;
		List<Long> bigNum = new ArrayList<Long>();
		List<Long> listX1 = new ArrayList<Long>();
		while(l < primefactors.size()) {
			if(bigNum.get(k - 1) != primefactors.get(l)) {
				z++; count = 1;	listX.add(count); bigNum.add(primefactors.get(l));
				k++;
			}
			else {
				count++;
				listX1.set(z - 1, count);
			}
			l++;
		}
		for(int i = 0; i < listX.size(); i++) {
			if(listX1[i] % 2 == 1)
				System.out.println("error.");
		}
		List<Long> primefactors = primeFactors(num2); z = 0;
		List<Long> bigNum = new ArrayList<Long>();
		List<Long> listX2 = new ArrayList<Long>();
		while(l < primefactors.size()) {
			if(bigNum.get(k - 1) != primefactors.get(l)) {
				z++; count = 1; listX.add(count); bigNum.add(primefactors.get(l));
				k++;
			}
			else {
				count++;
				listX2.set(z - 1, count);
			}
			l++;
		}
		for(int i = 0; i < listX2.size(); i++) {
			if(listX2[i] % 2 == 1)
				System.out.println("error.");
		}
	}
	
	public static void main(String[] args) {
		long g = 0, a = 0, p = 0; //BigInteger.valueOf(a);
		int count = 1;
		while(count > 0 && count < 5) {
			System.out.println("Types by things to calculate.");
			System.out.print("1. Shank's\n2. Pohlig-Hellman\n3. prime factorization test\n");
			System.out.print("4. Rabin-Miller test\n5. Pollard’s p - 1 test\n");
			System.out.print("Please enter a number for what you want: ");
			count = (int) getLong();
			
			if(count == 1) { //Shank's
				/* int num = 0;
				while(num < 3) {
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
				} */
				
				System.out.println("b^x = sf in Fprime");
				System.out.print("Give a base: ");
				g = getLong();
				System.out.print("Give a gcd: ");
				a = getLong();
				System.out.print("Give a prime: ");
				p = getLong();
				System.out.print("g: " + g); System.out.print(" a: " + a); System.out.print(" p: " + p);
				System.out.println(" x = " + shank(g, a, p));
				System.out.println("test:" + power(g, shank(g, a, p), p));
			}
			else if(count == 2) { //Pohlig-Hellman
				int num = 0;
				while(num < 4) {
					if(num == 0) {
						//(a) p = 433, g = 7, a = 166. x = 47
						System.out.print("a. ");
						g = 7; a = 166; p = 433;
					}
					else if (num == 1) {
						//(b) p = 746497, g = 10, a = 243278. x = 223755
						System.out.print("b. ");
						g = 10; a = 243278; p = 746497;
					}
					else if (num == 2) {
						System.out.print("c. ");
						//(c) p = 1291799, g = 17, a = 192988. (Hint. p - 1 has a factor of 709.) x = 984414
						g = 2; a = 39183497; p = 41022299;
					}
					else {
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
					System.out.print("\n");
					num++;
				}
				
				/* System.out.println("b^x = sf in Fprime");
				System.out.print("Give a base: ");
				g = getLong();
				System.out.print("Give a gcd: ");
				a = getLong();
				System.out.print("Give a prime: ");
				p = getLong();
				System.out.print("g: " + g); System.out.print(" a: " + a); System.out.print(" p: " + p);
				System.out.println(" x = " + pohligHellman(g, a, p));
				System.out.println("test:" + power(g, shank(g, a, p), p)); */
			}
			else if(count == 3) { //primeProbability
				int count1 = 0;
				g = 10;
				SecureRandom sr1 = new SecureRandom();
				long reference, finishm;
				double prob;
				/* (a) n = 1105. (Yes, 5 divides n, but this is just a warm-up exercise!)
				(b) n = 294409 (c) n = 294409
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
						p = 118915387;
					BigInteger pHope = BigInteger.valueOf(p);
					reference = System.nanoTime();
					prob = primeProbability(pHope, (int)g, sr1); //p is BigInteger, g is intNum of test, a is a rnd num
					finishm = System.nanoTime();
					System.out.println(((double)(finishm-reference)) / 1000000000.0 + "secs");  //in seconds
					System.out.println("num: " + pHope + " passes done: " + g + " primeProbability: " +  prob + "%");
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
			else if(count == 5) { //Large factorization
				/* (a) N = 61063
				18822 ≡ 270 (mod 61063) and 270 = 2 · 33 · 5
				18982 ≡ 60750 (mod 61063) and 60750 = 2 · 35 · 53 */
				
				/* (b) N = 52907
				3992 ≡ 480 (mod 52907) and 480 = 25 · 3 · 5
				7632 ≡ 192 (mod 52907) and 192 = 26 · 3
				7732 ≡ 15552 (mod 52907) and 15552 = 26 · 35
				9762 ≡ 250 (mod 52907) and 250 = 2 · 53 */
				
				/* (c) N = 198103
				11892 ≡ 27000 (mod 198103) and 27000 = 23 · 33 · 53
				16052 ≡ 686 (mod 198103) and 686 = 2 · 73
				23782 ≡ 108000 (mod 198103) and 108000 = 25 · 33 · 53
				28152 ≡ 105 (mod 198103) and 105 = 3 · 5 · 7 */
				
				/* (d) N = 2525891
				15912 ≡ 5390 (mod 2525891) and 5390 = 2 · 5 · 72 · 11
				31822 ≡ 21560 (mod 2525891) and 21560 = 23 · 5 · 72 · 11
				47732 ≡ 48510 (mod 2525891) and 48510 = 2 · 32 · 5 · 72 · 11
				52752 ≡ 40824 (mod 2525891) and 40824 = 23 · 36 · 7
				54012 ≡ 1386000 (mod 2525891) and 1386000 = 24 · 32 · 53 · 7 · 11 */
				
			}
			System.out.println("");
		}
		return;
	}
}

