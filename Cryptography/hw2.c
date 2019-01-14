
/*
 *  Function shanks():
 *
 *  This function uses Shanks algorithm to find a = log_alpha of beta mod p.
 *  The result is returned through the pointer ret to a big.
 */
void shanks(big alpha, big beta, big p, big pi, big ret) {
	int comp;
	big j,i,dummy;
	big t;
	big m;                    /* Variables Declaration */
	big pminus;
	big ein;    /* Internal Joke */
	pair list1,list2;
	
	t = mirvar(0);            /* Variables initialization */
	list1.value1 = mirvar(0);
	list1.value2 = mirvar(0);
	list2.value1 = mirvar(0);
	list2.value2 = mirvar(0);
	list1.next = NULL;
	list2.next = NULL;
	m = mirvar(0); 
	j = mirvar(0);
	ein = mirvar(1);
	pminus = mirvar(0);
	dummy = mirvar(0);
	
	decr(p,1,pminus);        /* Create a variable with p-1                         */
	nroot(pi,2,m);           /* Determine m = sqrt(pi)                             */
	incr(m,1,m);             /* Increase m by 1 to obtain m = ceil( sqrt(pminus))  */
	comp = (-1);
	while ( comp == (-1) ) {       /* Create 1st list with pairs (j, alpha^(mj)   */
		comp = compare(j,m);
		multiply(m,j,t);
		powmod(alpha,t,p,t); 
		insert(&list1,j,t);
		incr(j,1,j);
	}
	
	/*  Once the 1st list is created, compute each value of the second list   */
	/*  and look for it in the 1st list as it is created.  Therefore there is */
	/*  no such 'real' 2nd list.                                              */
	i = mirvar(0);
	comp = (-1);
	while ( comp == (-1) ) {
		comp = compare(i,m);
		powmod(alpha,i,p,t);
		xgcd(t,p,t,t,t);		/* alpha^(-i) */
		powmod2(t,ein,beta,ein,p,t);
		if (search (&list1, t, m,j)==1) {
			mad(m,j,i,pminus,dummy,t); 
			copy(t,ret);
			return;
		}
		incr(i,1,i);
	}
	printf("Matching pairs not found.  Error!\n");
}