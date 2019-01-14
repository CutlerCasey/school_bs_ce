<!DOCTYPE html>
<html>
<body>


<?php 
//page after guess
if (!empty($_POST)) {
	
if($_POST["guess"]==$_POST["value"]) {		
?>
You got it!
<?php 
}
else {
	?>
	
Your guess was incorrect. Please try again.
Please guess a number between 1 and 5.
<form name="guess form" action="assignment10.php" method="POST" >
<input type="text" name="guess" value="" ><br>
<input type="hidden" name="value" value="<?php  echo($_POST["value"])?>">
<input type="submit" name="submit" value="Submit" >
</form>

<?php 

}

}
//Page after form submission
else if (!empty($_GET)) {

echo("Hi $_GET[fname] $_GET[lname]!");
?>

Please guess a number between 1 and 5.
<form name="guess form" action="assignment10.php" method="POST" >
<input type="text" name="guess" value="" ><br>
<input type="hidden" name="value" value="<?php  echo(rand(1,5)) ?>">
<input type="submit" name="submit" value="Submit" >
</form>


<?php


// Opening page
} else { ?>

<script type="text/javascript">
function validate() {
    var x = document.forms["form"]["fname"].value;
    if (x == null || x == "") {
        alert("First name must be filled out");
        return false;
    }
    var x = document.forms["form"]["lname"].value;
    if (x == null || x == "") {
        alert("Last name must be filled out");
        return false;
    }
    var x = document.forms["form"]["email"].value;
	 var atpos = x.indexOf("@");
    var dotpos = x.lastIndexOf(".");
    if (atpos< 1 || dotpos<atpos+2 || dotpos+2>=x.length) {
        alert("Not a valid e-mail address");
        return false;
    }
    
    return true;
}

</script>

<form  name="form" action="assignment10.php" method="GET" onsubmit="return validate()">
First name: <input type="text" name="fname"><br>
Last name: <input type="text" name="lname"><br>
Email <input type="text" name="email"><br>
<input type="submit" name="submitted" value="Submit"></form>
<?php } ?>

</body>
</html> 