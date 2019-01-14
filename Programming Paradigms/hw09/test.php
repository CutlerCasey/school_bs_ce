<html>
<head>
    <title>Assignment 10</title>
</head>
<script type="text/php">
$fname = $lname = $email = "";
$page = $guess = $answer = 1;

if($_SERVER["REQUEST_METHOD"] == "GET") {
	$GLOBALS["$fname"] = test_input($_GET["fname"]);
	$GLOBALS["$lname"] = test_input($_GET["lname"]);
	$GLOBALS["$email"] = test_input($_GET["email"]);
}

if($_SERVER["REQUEST_METHOD"] == "POST") {
	$GLOBALS["$guess"] = test_input($_GET["guess"]);
	$GLOBALS["$answer"] = test_input($_GET["answer"]);
}

function test_input($data) {
	$data = trim($data);
	$data = stripslashes($data);
	$data = htmlspecialchars($data);
	return $data;
}

<script type="text/php">
if($GLOBALS["$page"] <= 1) {
	<script type="text/javascript">
	function formValidator(){
		// Make quick references to our fields
		var firstname = document.getElementById('fname');
		var addr = document.getElementById('lname');
		var email = document.getElementById('email');
		// Check each input in the order that it appears in the form!
		if(notEmpty(firstname, "Enter a first name")) {
			if(isAlphabet(firstname, "Please enter only letters for your first name")){
				if(notEmpty(lastname, "Enter a last name")) {
					if(isAlphabet(lastname, "Please enter only letters for your last")) {
						if(emailValidator(email, "Please enter a valid email address")){
							return true;
							;
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
		var alphaExp = /^[a-zA-Z]+$/;
		if(elem.value.match(alphaExp)) {
			return true;
		} else {
			alert(helperMsg);
			elem.focus();
			return false;
		}
	}
	
	function emailValidator(elem, helperMsg){
		var emailExp = /^[\w\-\.\+]+\@[a-zA-Z0-9\.\-]+\.[a-zA-z0-9]{2,4}$/;
		if(elem.value.match(emailExp)){
			return true;
		} else {
			alert(helperMsg);
			elem.focus();
			return false;
		}
	}
	</script>
	<body>
	<h1>Welcome to CSCE 3193 Assignment 10!</h1>
	
	<p>Please fill out the form below to begin a super fun game.</p>
	<form method="get" action="test.php" onsubmit="return formValidator()">
		Your first name: <input type="text" name="fname" id="fname" value="Casey"><br>
		Your last name: <input type="text" name="lname" id="lname" value="Cutler"><br>
		Your email address:  <input type="text" name="email" id="email" value="cbcutler@uark.edu"><br>
		<input type="submit" value="Submit">
	</form>
	<div id="msg"></div>
	</body>
	</script>
}
</script>

elseif($GLOBALS["$page"] == 2) {
	<script type="text/php">
		<body>
			<p>Hi there $GLOBALS["$fname"] $GLOBALS["$lname"]!Let's play a super fun game!</p>
			<p>I'm thinking of a number between from 1 to 5.  See if you can guess it!</p>
			<form method="post" action="test.php">
				Your guess: <input type="text" name="guess">
				<input type="hidden" name="answer" value="">
				<input type="submit" value="Guess!">
			</form>
		</body>
			</script>
} elseif($GLOBALS["$page"] == 3) {
	<script type="text/php">
		<body>
			<p>Oops!  Your guess, $guess, was wrong.  Please try again!</p>
			<form method="post" action="test.php">
				Your guess: <input type="text" name="guess">
				<input type="hidden" name="answer" value="1">
				<input type="submit" value="Guess!">
			</form>
		</body>
	</script>
} else {
	<script type="text/php">
		<body>
			<p>You got the right answer with $guess!</p>
			<p>$GLOBALS["$lname"], $GLOBALS["$fname"]</p><br>
			<p>Have a good day</p>
		</body>
	</script>
}
</script>
</html>