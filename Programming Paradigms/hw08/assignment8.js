////////////////////////////////////////////////////////////
//  CSCE 3193
//  Fall 2014
//  Assignment 8
//  Name:  
////////////////////////////////////////////////////////////


//  You must complete the implementation of the three functions below.
//  In all cases where it is given that the arguments to a function are
//  numbers (or an array of numbers), you can assume that the correct types
//  of values are passed as arguments and don't have to check for the wrong types.


////////////////////////////////////////////////////////////
//  Recursion

function ReverseArray(arr) {
	//  Write the body of ReverseArray so that it takes an arbitrary
	//  array "arr" as an argument and returns an array with the elements
	//  of arr in the exact opposite order.  For example, if arr = [1,2,3,4]
	//  then this function must return the array [4,3,2,1].
	//  THIS FUNCTION MUST USE RECURSION OR 60% WILL BE DEDUCTED.  Also, for
	//  full credit you must not define inner functions (i.e. ReverseArray must
	//  be the recursive function) and it must not use more than one parameter.
	if(!arr.length)
		return []; //needed for nothing case
    else if(arr.length > 1)
		return [ReverseArray(arr.slice(1)), arr[0]];
	else
		return arr[0];
	
	//recusive does not seem worth it to me. More lines of code and wasting the stack.
	/*if(!arr.length)
		return [];
	while(arr.length > 1) {
		if(arr.length > 1)
		arr.slice(1);
	}
	return arr;*/
}

////////////////////////////////////////////////////////////
//  Function composition
function GetFunctionForArrayPairs(func) {
	//  Write the body of GetFunctionForArrayPairs so that it takes a
	//  function "func" as an argument and returns a new function.
	//
	//  Input:  The argument "func" can be assumed to be a function which takes
	//  exactly two numbers as input and returns exactly one number.
	//  
	//  Output:  The function which is returned by GetFunctionForArrayPairs must
	//  take as input an array of numbers and return a new array which
	//  is the result of applying "func" to each consecutive pair of elements
	//  in the argument array.  For instance, if the input array was [1,2,3],
	//  then func should be applied to the pairs (1,2), (2,3), and (3,1).  (We
	//  wrap from the end of the array to the beginning.)
	//
	//  Example:  Assume that the function ManipulateTwoNumbers as defined in
	//  assignment8.html was the argument to GetFunctionForArrayPairs.  Then,
	//  if the function returned from GetFunctionForArrayPairs received the array
	//  [1,2,3,4,5] as input, it should return the array [2,6,12,20,5]
	var arg3 = [];
    var funcDis = function(arrI) {
		for(var i = 0; i < arrI.length; i++) {
			if(i == arrI.length - 1)
				arg3.push(func(arrI[i], arrI[0]));
			else
				arg3.push(func(arrI[i], arrI[i + 1]));
		}
        return arg3;
    }
    return funcDis;
}


////////////////////////////////////////////////////////////
//  Common closure
function GetAttachedFunctions() {
	//  Write the body of GetAttachedFunctions so that it takes no arguments and
	//  returns an array of exactly two functions.  These two functions must be
	//  contained within the same closure.  The closure should contain two variables
	//  which are accessible to both functions.  The first should be called "counter"
	//  and should be initialized to 0.  The second should be called "arr" and should
	//  be initialized to an empty array.
	//  Assuming that the variable "funcs" was used to store the return value from
	//  GetAttachedFunctions, (i.e."var funcs = GetAttachedFunctions();"), then the
	//  first function in funcs, which can be referenced as funcs[0], must take exactly
	//  two numbers as arguments (arg1 and arg2).  If the value of "counter" is less
	//  than 3, it should add (arg1 + arg2) and put that sum onto the end of the array "arr".
	//  Otherwise, it should not change "arr".  Then, it should return the array "arr".
	//  The second function returned from GetAttachedFunctions, funcs[1] from the above
	//  example, must take no arguments and simply increment the value of "counter" by 1
	//  and return nothing.
	
	//  Example:  If the following series of function calls were made, the correct resulting
	//  output is given.
	//
	//	var funcs = GetAttachedFunctions();
	//  outStr = funcs[0](1, 2) + "<br>";			// outStr will equal "3"
	//  outStr = funcs[0](3, 4) + "<br>";			// outStr will equal "3, 7"
	//  funcs[1]();									// counter will now equal 1
	//  funcs[1]();									// counter will now equal 2
	//  outStr = funcs[0](5, 6) + "<br>";			// outStr will equal "3, 7, 11"
	//  funcs[1]();									// counter will now equal 3
	//  outStr = funcs[0](7, 8) + "<br>";			// outStr will equal "3, 7, 11"
	var that = this;
	this.counter = 0;
	var arg = [0,1];
    this.arr = [];
    arg[0] = function(arg1, arg2) {
		//console.log("funcs[0] " + counter);
        if(that.counter < 3)
            that.arr.push(arg1 + arg2);
		//console.log(arr);
        return arr;
    }
    arg[1] = function() {
		//console.log("funcs[1] " + counter);
        that.counter++;
    }
    return arg;
}

