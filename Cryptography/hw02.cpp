//includes: most not needed, but was from some of my other code and might be needed later
#include <iostream>
#include <iomanip>
#include <string>
#include <stdio.h>
#include <time.h>
#include <stdlib.h>
#include <limits>
#include <string.h>
#include <unistd.h>
#include <fcntl.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <unistd.h>
#include <errno.h>
#include <inttypes.h>
#include <vector>
//some stuff that would help, but does not work on turing
#include <gmpxx.h> //mpq_t x; //g++ -lgmp
//#include <tuple>

//define/typedef: wanting to expand my knowledge base here a bit
typedef std::vector<uint64_t> Roots(uint64_t p, uint64_t q);
#define MOD 2 //modules rate
#define ISODD(x) ((x) & 0x1)
#define ISEVEN(x) (!ISODD(x))

//classes that just help figure out some stuff
int getInt(); //test for geting ints from the user
void testPrims(); //shows max size of the primitive variable types

//working stuff that matters
//both of the Euclidian work just slightly differently and saving for later, would like x to be > 0, but you can just flip after.
void extendedEuclidAlgortithm(uint64_t& a, int64_t &u, uint64_t& b, int64_t &v, uint64_t &gcd); //a * u + b * v = gcd(a, b)
void extended_euclid(uint64_t a, uint64_t b, uint64_t& x, uint64_t& y, uint64_t& d); //a * u + b * v = gcd(a, b)
uint64_t gcd_binary(uint64_t a, uint64_t b); //gcd(a, b) in a binary way
uint64_t fastExp(uint64_t base, uint64_t exp); //base ^ exp = ans
uint64_t fastExpMod(uint64_t base, uint64_t exp, uint64_t mod); //base ^ exp (mod mod) = ans
uint64_t modinv(uint64_t u, uint64_t v); //u^1 (mod v) = ans
//uint64_t primitiveRoots(uint64_t *roots, unit64_t prime);
//void improved_sieve(uint64_t N); //looking for prime numbers, will look into later.

//testing stuff that matters for later
uint64_t tonelli_shanks(uint64_t n, uint64_t p); //r * r = n [mod p]: n < prime return r
//primes
uint64_t LargestPrime(uint64_t Number); //looking for prime numbers, will look into later.
bool Is_Prime(uint64_t Number); //looking for prime numbers, will look into later.
//not sure
uint64_t mul_inv(uint64_t a, uint64_t b);
//chinese remainder
uint64_t chinese_remainder(uint64_t *n, uint64_t *a, uint64_t len);
//roots
bool InQR(uint64_t y, uint64_t p) { return fastExpMod(y,(p-1)/2,p) == 1; }
bool InQR(uint64_t y, uint64_t p, uint64_t q) { return InQR(y,p) && InQR(y,q); }
uint64_t Map(uint64_t u, uint64_t v, uint64_t p, uint64_t q);
uint64_t Inverse(uint64_t n, uint64_t a);

//main code to run
int main() {
	uint64_t a, b, gcd; //for the extendedEuclidAlgortithm [u & v also for Knuth u^-1 (mod v)]
	int64_t u, v;

	uint64_t base, exp; //Fast Expentional/powering
	uint64_t ans, mod; //for large numbers.
	int num = 1; //looping test/what I want to do.

	std::cout << std::endl;
	while((num < 6 && num >= 0)) { //loop for testing functions
		a = u = b = v = gcd = base = exp = 0;
		ans = mod = 0;

		printf("1. Extended Euclidian Algortithm\n2. Fast Expentional\n");
		printf("3. Chinese reminder\n");
		//printf("3. modinv\n"); //printf("4. Finding primivitive squares\n");
		printf("5. tonelli_shanks\n");
		printf("0. Test size of primitives\n");
		printf("Any other number to quit\n");
		printf("Please give a number: ");
		num = getInt();

		if(num == 1) { //gcd and the extendedEuclidAlgortithm
			printf("a * u + b * v = gcd(a, b)\n");
			printf("Please give an a: ");
			uint64_t tempa = a = getInt();

			printf("Please give a b: ");
			uint64_t tempb = b = getInt();

			extendedEuclidAlgortithm(a, u, b, v, gcd);
			printf("extendedEuclidAlgortithm--(a = %"PRId64") * (u = %"PRId64") + (b = %"PRId64") * (v = %"PRId64") = gcd(%"PRId64", %"PRId64") = %"PRId64"\n", a, u, b, v, a, b, gcd);

			//another way to do it
			/* u = v = gcd = 0;
			a = tempa; b = tempb;
			extended_euclid(a, b, x, y, gcd);
			printf("extended_euclid--(a = %"PRId64") * (u = %"PRId64") + (b = %"PRId64") * (v = %"PRId64") = gcd(%"PRId64", %"PRId64") = %"PRId64"\n", a, x, b, y, a, b, gcd); */

			a = tempa; b = tempb; gcd = 0;
			gcd = gcd_binary(a, b);
			printf("gcd_binary--gcd(%"PRId64", %"PRId64") = %"PRId64"\n", a, b, gcd);
		}
		else if(num == 2) { //FastExp
			printf("base ^ exp = answer\n");
			printf("Please give an base: ");
			base = getInt();

			printf("Please give an exp: ");
			exp = getInt();

			printf("Please give a number for the mod: ");
			mod = getInt();

			ans = fastExpMod(base, exp, mod);
			printf("FastExpMod--(base = %"PRId64") ^ (exp = %"PRId64") (mod [mod = %"PRId64"])= (answer = %"PRId64")\n", base, exp, mod, ans);
		}
		else if(num == 5) { //iversion mod
			printf("u^1 (mod v) = ans\n");
			printf("Please give an u: ");
			u = getInt();

			printf("Please give an v: ");
			v = getInt();

			ans = modinv(u, v);
			printf("(u^-1 = %"PRId64") [mod(v = %"PRId64")] = (ans = %"PRId64")\n", u, v, ans);
		}
		/* else if(num == 4) { //finding primivitive squares from x to y
			printf("Please give a number to share: ");
			base = getInt();

			printf("Please give a number to: ");
			exp = getInt();
			uint64_t tempRoots[exp];
			uint64_t buckets[exp];
			for(uint64_t a = 2; a <= exp; a++) {
				if(Is_Prime(a)) {
					uint64_t roots[a];
					primitiveRoots(roots, a);
					if(a > 2) {
						;
					}
					for(uint64_t i = 0; i < a; i++) {
						tempRoots[i] = roots[i];
						tempRoots[i + 1] = NULL;
					}
				}
			}

		} */
		else if(num == 3) { //chinese_remainder
			uint64_t n[] = { 3, 5, 7 };
			uint64_t a[] = { 2, 3, 2 };
			printf("When n are not pairwise coprime, the \nprogram crashes due to division by zero, which \nis one way to convey error.\n");
			int count = 0;
			while(count < 3) {
				printf("Please give an n[%d]: ", count);
				n[count] = getInt();
				count++;
			}
			count = 0;
			while(count < 3) {
				printf("Please give an a[%d]: ", count);
				a[count] = getInt();
				count++;
			}
			printf("%"PRId64"\n", chinese_remainder(n, a, sizeof(n) / sizeof(n[0])));
		}
		else if(num == 5) { //tonelli_shanks
			printf("r * r = n [mod p] where n < p and p is a prime\n");
			printf("Please give an n: ");
			a = getInt();
			
			while(!(Is_Prime(b))) {
				printf("Please give a prime number: ");
				b = getInt();
			}
			
			gcd = tonelli_shanks(a, b);
			printf("(n = %"PRId64") [mod(p = %"PRId64")] = (r = %"PRId64")^2\n", a, b, gcd);
		}
		else if(num == 0) { //primivitive variable test: everything tested on turing, but incase.
			testPrims();
		}
		std::cout << std::endl;
	}
	return 0;
}

uint64_t gcd_binary(uint64_t a, uint64_t b) { //done for uint64_t
	//Returns gcd(a, b)
    unsigned int r, t, k;
	if(a < 0)
		a *= -1;
	if(b < 0)
		b *= -1;
    //1. [Reduce size once]
    if (a < b) { //Exchange a and b
        t = a;
        a = b;
        b = t;
    }
    //If b = 0 output a and stop
    if (0 == b) {
        return a;
    }
    //Set r <-- a mod b, a <-- b, b <-- r
    r = a % b;
    a = b;
    b = r;
    //If b = 0 output a and stop
    if (0 == b) {
        return a;
    }
    //2. [Compute power of 2]
    k = 0;  //g = 2^k <-- 1
    while (ISEVEN(a) && ISEVEN(b)) {
        //While a and b are even
        a >>= 1;  //a <-- a/2
        b >>= 1;  //b <-- b/2
        k++;      //g <-- 2g
    }
    while (a != 0) {
        //3. [Remove initial powers of 2]
        while (ISEVEN(a)) {
            a >>= 1;  /* a <-- a/2 until a is odd */
        }
        while (ISEVEN(b)) {
            b >>= 1;  /* b <-- b/2 until b is odd */
        }
        //4. [Subtract and halve] t <-- |a - b|/2
        if (b > a)
            t = (b - a) >> 1;
        else
            t = (a - b) >> 1;
        //If a >= b then a <-- t, otherwise b <-- t
        if (a >= b)
            a = t;
        else
            b = t;
        //5. [Loop until a = 0]
    }
    //Output (2^k.b) and stop
    return (b << k);
}

void extended_euclid(uint64_t a, uint64_t b, int64_t& x, int64_t& y, uint64_t& d) {
	//calculates a * *x + b * *y = gcd(a, b) = *d
	uint64_t q, r, x1, x2, y1, y2;
	if (b == 0) {
		d = a, x = 1, y = 0;
		return;
	}
	x2 = 1, x1 = 0, y2 = 0, y1 = 1;
	while (b > 0) {
		q = a / b, r = a - q * b;
		x = x2 - q * x1, y = y2 - q * y1;
		a = b, b = r;
		x2 = x1, x1 = x, y2 = y1, y1 = y;
	}
	d = a, x = x2, y = y2;
}

void extendedEuclidAlgortithm(uint64_t& a, int64_t &u, uint64_t& b, int64_t &v, uint64_t &gcd) {
	int64_t lastu, lastv, quotient, temp;
	uint64_t temp1 = a, temp2 = b, tempa = a, tempb = b;
    //begin function
    u = 0;
    v = 1;
    lastu = 1;
    lastv = 0;
	while(temp2 != 0) {
        quotient = temp1 / temp2;
        temp = temp1 % temp2;
        temp1 = temp2;
        temp2 = temp;

        temp = u;
		u = lastu - quotient * u;
		lastu = temp;

        temp = v;
		v = lastv - quotient * v;
		lastv = temp;
    }
	a = tempa; b = tempb;
	if(lastu < 0) {
		u = b + lastu; v = lastv - a;
	}
	else {
		u = lastu; v = lastv;
	}
	gcd = temp1;
	return;
}

uint64_t modinv(uint64_t u, uint64_t v) {
    uint64_t inv, u1, u3, v1, v3, t1, t3, q;
    uint64_t iter;
    //Step X1. Initialise
    u1 = 1;
    u3 = u;
    v1 = 0;
    v3 = v;
    //Remember odd/even iterations
    iter = 1;
    //Step X2. Loop while v3 != 0
    while (v3 != 0) {
		//Step X3. Divide and "Subtract"
        q = u3 / v3;
        t3 = u3 % v3;
        t1 = u1 + q * v1;
        //Swap
        u1 = v1; v1 = t1; u3 = v3; v3 = t3;
        iter = -iter;
    }
    //Make sure u3 = gcd(u,v) == 1
    if (u3 != 1)
        return 0;   //Error: No inverse exists
    //Ensure a positive result
    if (iter < 0)
        return inv = v - u1;
    else
        return inv = u1;
}

uint64_t fastExp(uint64_t base, uint64_t exp) { //done for uint64_t
	uint64_t res = 1;
	uint64_t a = base, x = exp;
	//int count = 1;
	if(x == 0)
		return 1;
	else if(x == 1)
		return a;
	while(x > 0) {
		if(ISODD(x)) {
			res *= a;
			if(!(res > 0 && res < std::numeric_limits< uint64_t >::max())) {
				printf("This causes an overflow in uint64_t.\n");
				return res;
			}
			else {
				if(x == 1)
					return res;
				x -= 1;
			}
		}
		a *= a;
		x /= 2;
	}
	//printf("res = %"PRId64"\n", res);
	return res;
}

uint64_t fastExpMod(uint64_t base, uint64_t exp, uint64_t mod) {
	uint64_t b = 1, tempBase = base;
	while(exp > 0) {
		if (ISODD(exp))
			b = (b * tempBase) % mod;
		tempBase = (tempBase * tempBase) % mod;
		exp = exp / 2;
	}
	return b;
}

/* void improved_sieve(uint64_t N) { //for finding primes later
	uint64_t M = (N - 1) / 2;
	uint64_t x = (floor (sqrt(N)) - 1) / 2, i, j;
	vector<bool> S(M + 1, 0);
	for(i = 1; i <= x; i++) {
		if(!S[i]) {
			for (j = 2 * i * (i + 1); j <= M; j += (2 * i + 1))
				S[j] = 1;
		}
	}
	long long s=2;
	for (i = 1; i <= M; i++) {
		if(!S[i])
			s+=(2*i+1);
	}
	printf("%lld\n",s); //change to return ll
} */

bool Is_Prime(uint64_t Number) { // function that checks if the number is prime
    uint64_t i = 2; // for iteration
    bool is_prime = 1; // the variable that says if number is prime or not
    float half = Number / 2; // just so Number/2 wouldn't be calculated every iteration

    while( is_prime && i <= half ) { // the while only makes max half iterations
        if ( Number % i==0 )
			is_prime = 0;
        i++;
    }

    return is_prime; // returns 1 if it is prime and 0 if it isn't prime
}

uint64_t LargestPrime(uint64_t Number) { // function that finds the maximum prime number smaller than Number
	uint64_t MaxPrime = 0; // variable that stores the max prime number that the function will return
	bool found_maxp = 0; // variable that keeps track if the max prime number has been found
	uint64_t i = Number;

	while ( !found_maxp && i>2 ) { // the while only makes max number-2 iterations
	    if ( Is_Prime(i) ) {
          found_maxp = 1; // the max prime number has been found, no need to search next iteration
	      MaxPrime = i; // the max prime number is of course i
	    }
        i--;
	}

	return MaxPrime; // returns the max prime smaller or equal to Number
}

uint64_t mul_inv(uint64_t a, uint64_t b) { // returns x where (a * x) % b == 1
	uint64_t b0 = b, t, q;
	uint64_t x0 = 0, x1 = 1;
	if (b == 1) return 1;
	while (a > 1) {
		q = a / b;
		t = b, b = a % b, a = t;
		t = x0, x0 = x1 - q * x0, x1 = t;
	}
	if (x1 < 0) x1 += b0;
	return x1;
}

uint64_t chinese_remainder(uint64_t *n, uint64_t *a, uint64_t len) {
	uint64_t p, i, prod = 1, sum = 0;
	for (i = 0; i < len; i++)
		prod *= n[i];
	for (i = 0; i < len; i++) {
		p = prod / n[i];
		sum += a[i] * mul_inv(p, n[i]) * p;
	}
	return sum % prod;
}

/* vector<uint64_t> Roots(uint64_t p, uint64_t q) { //If the input number is semi-prime and you have its (two) prime factors at hand, then you can use this:
    vector<uint64_t> roots;
    uint64_t zstar = p * q;
    for (uint64_t y = 1; y<zstar; y++) {
        if (gcd_binary(zstar, y) == 1 && InQR(y, p, q)) {
            uint64_t yp = fastExpMod(y, (p + 1) / 4, p);
            uint64_t yq = fastExpMod(y, (q + 1) / 4, q);
            uint64_t r1 = Map(0 + yp, 0 + yq, p, q);
            uint64_t r2 = Map(0 + yp, q - yq, p, q);
            uint64_t r3 = Map(p - yp, 0 + yq, p, q);
            uint64_t r4 = Map(p - yp, q - yq, p, q);
            roots.push_back(r1);
            roots.push_back(r2);
            roots.push_back(r3);
            roots.push_back(r4);
        }
    }
    return roots;
} */

/* unit64_t primitiveRoots(uint64_t *roots, unit64_t prime) {
	unit64_t x = 1, y = 1, z = 0;
	while(x < prime){
		if(gcd_binary(x, prime) == 1) {
			y = 1;
			while(fastExpMod(x, y, prime) != 1) {
				y += 1;
				if(y == fastExpMod(x, y, prime)) {
					roots[z] += x;
					z++;
				}
			}
		}
		x += 1;
	}
} */

uint64_t Map(uint64_t u, uint64_t v, uint64_t p, uint64_t q) {
    uint64_t a = q * Inverse(p, q);
    uint64_t b = p * Inverse(q, p);
    return (u * a + v * b)%(p * q);
}

uint64_t Inverse(uint64_t n, uint64_t a) {
    int64_t  x1 = 1;
    int64_t  x2 = 0;
    int64_t  y1 = 0;
    int64_t  y2 = 1;
    uint64_t r1 = n;
    uint64_t r2 = a;
    while (r2 != 0) {
        uint64_t r3 = r1%r2;
        uint64_t q3 = r1/r2;
        int64_t  x3 = x1-q3*x2;
        int64_t  y3 = y1-q3*y2;

        x1 = x2;
        x2 = x3;
        y1 = y2;
        y2 = y3;
        r1 = r2;
        r2 = r3;
    }
    return (uint64_t)(y1>0? y1:y1+n);
}

uint64_t tonelli_shanks(uint64_t n, uint64_t p) { //Takes as input an odd prime p and n < p and returns r such that r * r = n [mod p].
	uint64_t s = 0; uint64_t q = p - 1;
	while ((q & 1) == 0) { q /= 2; ++s; }
	if (s == 1) {
		uint64_t r = fastExpMod(n, (p+1)/4, p);
		if ((r * r) % p == n) return r;
		return 0;
	}
	// Find the first quadratic non-residue z by brute-force search
	uint64_t z = 1;
	while (fastExpMod(++z, (p-1)/2, p) != p - 1);
	uint64_t c = fastExpMod(z, q, p);
	uint64_t r = fastExpMod(n, (q+1)/2, p);
	uint64_t t = fastExpMod(n, q, p);
	uint64_t m = s;
	while (t != 1) {
		uint64_t tt = t;
		uint64_t i = 0;
		while (tt != 1) {
			tt = (tt * tt) % p;
			++i;
			if (i == m) return 0;
		}
		uint64_t b = fastExpMod(c, fastExpMod(2, m-i-1, p-1), p);
		uint64_t b2 = (b * b) % p;
		r = (r * b) % p;
		t = (t * b2) % p;
		c = b2;
		m = i;
	}
	if ((r * r) % p == n) return r;
	return 0;
}

void testPrims() { //just to wonder size
	std::cout << std::setw(25) << "Max Char: " << std::setw(21) << (int) std::numeric_limits< char >::max() << " via " << sizeof(char)<< " bytes" << std::endl;
	std::cout << std::setw(25) << "Max Short: " << std::setw(21) << std::numeric_limits< short >::max() << " via " << sizeof(short)<< " bytes" << std::endl;
	std::cout << std::setw(25) << "Max Int: " << std::setw(21) << std::numeric_limits< int >::max() << " via " << sizeof(int)<< " bytes" << std::endl;
	std::cout << std::setw(25) << "Max unsigned: " << std::setw(21) << std::numeric_limits< unsigned >::max() << " via " << sizeof(unsigned)<< " bytes" << std::endl;
	std::cout << std::setw(25) << "Max long: " << std::setw(21) << std::numeric_limits< long >::max() << " via " << sizeof(long)<< " bytes" << std::endl;
	std::cout << std::setw(25) << "Max float: " << std::setw(21) << std::numeric_limits< float >::max() << " via " << sizeof(float)<< " bytes" << std::endl;
	std::cout << std::setw(25) << "Max double: " << std::setw(21) << std::numeric_limits< double >::max() << " via " << sizeof(double)<< " bytes" << std::endl;
	std::cout << std::setw(25) << "Max long double: " << std::setw(21) << std::numeric_limits< long double >::max() << " via " << sizeof(long double)<< " bytes" << std::endl;
	std::cout << std::setw(25) << "Max long long: " << std::setw(21) << std::numeric_limits< long long >::max() << " via " << sizeof(long long)<< " bytes" << std::endl;
	std::cout << std::setw(25) << "Max unsigned long long: " << std::setw(21) << std::numeric_limits< unsigned long long >::max() << " via " << sizeof(unsigned long long)<< " bytes" << std::endl;
	std::cout << std::setw(25) << "int32_t: " << std::setw(21) << std::numeric_limits< int32_t >::max() << " via " << sizeof(int32_t)<< " bytes" << std::endl;
	std::cout << std::setw(25) << "int64_t: " << std::setw(21) << std::numeric_limits< int64_t >::max() << " via " << sizeof(int64_t)<< " bytes" << std::endl;
	std::cout << std::setw(25) << "uint32_t: " << std::setw(21) << std::numeric_limits< uint32_t >::max() << " via " << sizeof(uint32_t)<< " bytes" << std::endl;
	std::cout << std::setw(25) << "uint64_t: " << std::setw(21) << std::numeric_limits< uint64_t >::max() << " via " << sizeof(uint64_t)<< " bytes" << std::endl;
}

int getInt() { //getting an int
	int x;
    std::cin >> x;
    while(std::cin.fail()) {
        std::cout << "Error" << std::endl;
        std::cin.clear();
        std::cin.ignore(256,'\n');
        std::cin >> x;
    }
	return x;
}

