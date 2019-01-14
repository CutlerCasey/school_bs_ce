<?php
	//The following webpage was used to figure out how to do a semi-secure cookie array
	//and six lines of code do come from it almost directly, but were changed to fit closer to my naming needs
	//comments about each line and my understanding of it is there also
	//http://stackoverflow.com/questions/19068363/storing-and-retrieving-an-array-in-a-php-cookie
	session_start()
?>

<!DOCTYPE html>

<?php
	$_SESSION["firstName"] = ""; $_SESSION["lastName"] = "";
	$_SESSION["eMail"] = "";
	$_SESSION["answer"] = 6; $_SESSION["guess"] = 12345;
	if($_SERVER["REQUEST_METHOD"] == "GET") {
		$_SESSION["firstName"] = testInput($_GET["fname"]);
		$_SESSION["lastName"] = testInput($_GET["lname"]);
		$_SESSION["eMail"] = testInput($_GET["email"]);
		$_SESSION["answer"] = rand(1,5);
	}
	else if($_SERVER["REQUEST_METHOD"] == "POST") {
		//grabbing the whole cookie that was already set
		$cookie = $_COOKIE["info"];
		//json_encode does add '/' to your array and you have to pull them out before decoding
		$cookie = stripslashes($cookie);
		//after this line pullCookie = previous cookieArray, and can be used to for data
		$pullCookie = json_decode($cookie, true);
		
		$_SESSION["firstName"] = $pullCookie[0]; $_SESSION["lastName"] = $pullCookie[1];
		$_SESSION["eMail"] = $pullCookie[2];
		$_SESSION["answer"] = testInput($_POST["answer"]);
		$_SESSION["guess"] = testInput($_POST["guess"]);
	}
	//making an array
	$cookieArray = array($_SESSION["firstName"], $_SESSION["lastName"], $_SESSION["eMail"]);
	//json_encode is a way to secure against php injections
	$json = json_encode($cookieArray);
	//guessing json_encode() is an array itself that has most of the fields of setcookie in it.
	setcookie("info", $json);
	
	//wanted to test my variables
	//echo '<script type="text/javascript">console.log("1.fn: '.$_SESSION["firstName"].' ln: '.$_SESSION["lastName"].' em: '.$_SESSION["eMail"].' an: '.$_SESSION["answer"].' gu: '.$_SESSION["guess"].'");</script>';
	
	function testInput($data) { //saw this on stackoverflow, but not sure what page since it was about a year ago
		//htmlspecialchars works for most things, but the following will secure more types of php injection attacks
		//and since you can move the variables in to a function without issues this works.
		//can semi prove this with htmlspecialchars("") it self is a function.
		$data = trim($data);
		$data = stripslashes($data);
		$data = htmlspecialchars($data);
		return $data;
	}
?>

<html>
<head>
    <title>Assignment9</title>
</head>
<body>
	<?php
		//variables that take up more memory, but make it the variables smaller for the coder
		$fN = $_SESSION["firstName"]; $lN = $_SESSION["lastName"]; $eM = $_SESSION["eMail"];
		$ans = $_SESSION["answer"]; $gue = $_SESSION["guess"];
		$website = $_SERVER["PHP_SELF"]; //I do like this one, since it is not something I thought of till later
		
		//echo '<script type="text/javascript">console.log("2.fn: '.$_SESSION["firstName"].' ln: '.$_SESSION["lastName"].' em: '.$_SESSION["eMail"].' an: '.$_SESSION["answer"].' gu: '.$_SESSION["guess"].'");</script>';
		//did not want anyone to look at the code and see the javascript functions of other pages
		if($fN == "" || $lN == "" || $eM == "") { //testing against if any is 
			echo '
				<script type="text/javascript">
					function formValidator(){
						// Make quick references to our fields
						var firstname = document.getElementById("fname"); //console.log("firstname");
						var lastname = document.getElementById("lname"); //console.log("lastname");
						var emai = document.getElementById("email"); //console.log("emai");
						//Check each input in the order that it appears in the form!
						if(notEmpty(firstname, "Enter a first name.")) {
							if(isAlphabet(firstname, "Please enter only letters for your first name.")){
								if(notEmpty(lastname, "Enter a last name.")) {
									if(isAlphabet(lastname, "Please enter only letters for your last.")) {
										if(notEmpty(emai, "Enter an e-mail adress.")) {
											if(emailValidator(emai, "Please enter a valid email address."))
												return true;
										}
									}
								}
							}
						}
						return false;
					}
					function notEmpty(elem, helperMsg) {
						if(elem.value.length == 0){
							alert(helperMsg);
							elem.focus(); // set the focus to this input
							return false;
						}
						return true;
					}
					function isAlphabet(elem, helperMsg) {
						var alphaExp = /^[a-zA-Z]+$/;  //taken from some where for testing legal values
						if(elem.value.match(alphaExp)) {
							return true;
						}
						else {
							alert(helperMsg);
							elem.focus();
							return false;
						}
					}
					function emailValidator(elem, helperMsg){
						var emailExp = /^[\w\-\.\+]+\@[a-zA-Z0-9\.\-]+\.[a-zA-z0-9]{2,4}$/; //taken from some where for testing legal values
						if(elem.value.match(emailExp)) {
							//emailExp.indexOf is not a function, yet it works to test if false for the right reasons.
							if(emailExp.indexOf("@") == 1) {
								if(emailExp.indexOf(".") > 0)
									return true;
							}
						}
						alert(helperMsg);
						elem.focus();
						return false;
					}
				</script>
	
				<h1>Welcome to CSCE 3193 Assignment9!</h1>
				<p>Please fill out the form below to begin a super fun game.</p>
				<form method="GET" action="' . $website . '" onsubmit="return formValidator()">
					Your first name: <input type="text" id="fname" name="fname" value="Casey"><br>
					Your last name: <input type="text" id="lname" name="lname" value="Cutler"><br>
					Your email address:  <input type="text" id="email" name="email" value="cbcutler@uark.edu"><br>
					<input type="submit" value="Submit">
				</form>
				<div id="msg"></div>
			';
		}
		else if($gue > 5 || $gue < 1) { //could have used any number, since this takes you back here to the start if some how put in a number yourself
			echo '
				<script type="text/javascript">
					function isNumeric() {
						var elem = document.getElementById("guess");
						var numericExpression = /^[0-9]+$/; //got from some where else for testing
						if(elem.value.length != 0 && elem.value.match(numericExpression) && elem.value > 0 && elem.value < 6)
							return true; //but I test serveral things
						else{
							alert("Please enter a number from 1 to 5.");
							elem.focus(); //set the focus to this input
							return false;
						}
					}
				</script>
				
				<p>Hi there ' . $lN . ', ' . $fN . '!  Let us play a super fun game!</p>
				<p>I am thinking of a number between from 1 to 5.  See if you can guess it!</p>
				<form method="POST" action=' . $website . ' onsubmit="return isNumeric()">
					Your guess: <input type="text" id="guess" name="guess">
					<input type="hidden" name="answer" value="' . $ans . '">
					<input type="submit" value="Guess!">
				</form>
			';
		}
		else if($ans != $gue) { //guess I chould test if $gue and $ans are numbers and not something else
			echo '
				<script type="text/javascript">
					function isNumeric() {
						var elem = document.getElementById("guess");
						var numericExpression = /^[0-9]+$/;
						if(elem.value.length != 0 && elem.value.match(numericExpression) && elem.value > 0 && elem.value < 6)
							return true;
						else{
							alert("Please enter a number from 1 to 5.");
							elem.focus(); // set the focus to this input
							return false;
						}
					}
				</script>
				
				<p>Oops!  Your guess, ' . $gue . ', was wrong.  Please try again!</p>
				<form method="POST" action="' . $website . '" onsubmit="return isNumeric()">
					Your guess: <input type="text" id="guess" name="guess">
					<input type="hidden" name="answer" value="' . $ans . '">
					<input type="submit" value="Guess!">
				</form>
			';
		}
		else if ($gue == $ans) {
			echo '
				<p>You got the right answer with ' . $gue . '!</p>
				<p>Please ' . $lN . ', ' . $fN . ' have a good day</p>
			';
			//saw allot of 1 instead of time()-(some positive number), I do see it being faster, but could at a later date cause breaking code due to some unseen issue.
			setcookie('info', $GLOBALS["json"], time()-3600); //killing the cookie, since no longer needed
		}
		else {
			echo '<p>This should not happen, if it does you are most likely a hacker!.</p>'; //guess it is possible that there was some type of system glicth I do not know of.
			setcookie('info', $GLOBALS["json"], time()-3600); //killing the cookie, since error
		}
	?>
	</body>
</html>