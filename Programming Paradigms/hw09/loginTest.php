<?php
session_start()
?>
<!DOCTYPE html>

<?php

if (isset($_REQUEST["welcomeColor"]))
{
	$welColor = $_REQUEST["welcomeColor"];

	setcookie("welColor2", $welColor, time()+(86400*30), "/");
}
else
{
	$welColor = isset($_COOKIE["welColor2"]) ? $_COOKIE["welColor2"] : "white";
}
?>

<html>

<head>
<script>
function newVerify()
{
	if (document.forms["newForm"]["newuname"].value.length > 0 && document.forms["newForm"]["newpass"].value.length > 0 && document.forms["newForm"]["newpass"].value ==document.forms["newForm"]["newvpass"].value)
	{	
		return true;
	}

	return false;
}

</script>
</head>

<?php
if ($_SERVER["REQUEST_METHOD"] == "POST")
{
	$userFile = fopen("userFile.txt", "r") or die("Unable to open file!");
	$username = $_POST["uname"];
	$password = $_POST["pass"];

	while(!feof($userFile))
	{
		$line = fgets($userFile);
		
		$line_arr = explode(";", $line);

		if ($line_arr[0] == $username && $line_arr[1] == $password)
		{
			$_SESSION["username"] = $username;
			$_SESSION["bkcolor"] = $line_arr[2];
		}
	}

	$login_message = "<span style='color:red'> Invalid username or password. </span>";
}

if (isset($_REQUEST["homepageColor"]))
{

	$homeColor = $_REQUEST["homepageColor"];

	$_SESSION["bkcolor"] = $homeColor;

	$userFile = fopen("userFile.txt", "r") or die("Unable to open file!");
	$numUsers = 0;

	while(!feof($userFile))
	{
		$line[$numUsers] = fgets($userFile);
		$numUsers++;
	}

	fclose($userFile);

	$userFile = fopen("userFile.txt", "w") or die("Unable to open file!");

	for ($i = 0; $i < $numUsers; $i++)
	{
		$line_arr = explode(";", $line[$i]);

		if ($line_arr[0] == $_SESSION["username"])
		{
			fwrite($userFile, $line_arr[0]);
			fwrite($userFile, ";");
			fwrite($userFile, $line_arr[1]);
			fwrite($userFile, ";");
			fwrite($userFile, $homeColor);
			fwrite($userFile, "\n");
		}
		else
		{
			fwrite($userFile, $line[$i]);
		}
	}
	fclose($userFile);

}


if ($_GET["logout"] == "true")
{
	session_unset();
	session_destroy();
}

if (isset($_REQUEST["newuname"]))
{
	$userFile = fopen("userFile.txt", "a") or die("Unable to open file!");


	fwrite($userFile, $_REQUEST["newuname"]);
	fwrite($userFile, ";");
	fwrite($userFile, $_REQUEST["newpass"]);
	fwrite($userFile, ";");
	fwrite($userFile, "white");
	fwrite($userFile, "\n");

	fclose($userFile);

	$_SESSION["username"] = $_REQUEST["newuname"];
	$_SESSION["bkcolor"] = "white";
}

if(!isset($_SESSION["username"]))
{
	echo "<body style='background-color:" . $welColor . "'>";
	echo "<h1> Welcome to FriendNook </h1>";
	
	echo "<p> Please login to continue. </p>";

	echo $login_message;	

	echo "<form action='loginTest2.php' method='post'>"
	     . "Username: <input type='text' name='uname'><br>"
	     . "Password: <input type='password' name='pass'><br>"
	     . "<input type='submit'> <br>"
	     . "</form>";
	echo "<br><br><br>";	
	echo  "<form action='loginTest2.php' method='get'>"
	     . "Background color: <input type='text' name='welcomeColor'><br>"
	     . "<input type='submit'> <br>"
	     . "</form>";
	echo "<h3> Create a new user. </h3>";

	echo "<form name='newForm' action='loginTest2.php' method='post' onsubmit='return newVerify()'>"
	     . "Username: <input type='text' name='newuname'><br>"
	     . "Password: <input type='password' name='newpass'><br>"
	     . "Verify Password: <input type='password' name='newvpass'><br>"
	     . "<input type='submit'> <br>"
	     . "</form>"
	     . "</body>";
}
else
{
	echo "<body style='background-color:" . $_SESSION["bkcolor"] . "'>";
	
	echo "<h1> Welcome " . $_SESSION["username"] . "</h1>";

	echo "<p>You have no friends.</p>";

	echo "<form action='loginTest2.php' method='get'>"
		. "<input type='hidden' name='logout' value='true'>"
		. "<input type='submit' value='logout'>"
		. "</form>";
	echo  "<form action='loginTest2.php' method='get'>"
	     . "Background color: <input type='text' name='homepageColor'><br>"
	     . "<input type='submit'> <br>"
	     . "</form>"
	     . "</body>";
}
?>
</html>
