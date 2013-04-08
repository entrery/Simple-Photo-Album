<%@ page language="java" session="false" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Login Page</title>
<link rel="stylesheet" href="resources/css/photos.css" type="text/css" />

<script>
function validate(form) {
	var username = (form["name"].value).trim();
	var regex = /^[a-zA-Z0-9_]+$/;
	
	if(!regex.test(username)) {
		alert("Username should contain only a-z, A-Z, 0-9 and _");
		form["name"].focus();
		return false;
	}
	
	var realName = (form["realName"].value).trim();
	var regex2 = /^[a-zA-Z0-9]+$/;
	
	var pass = form["pass"].value;
	
	if(pass.length < 4) {
		alert("Invalid password");
		return false;
	}
	
	if(!regex2.test(realName)) {
		alert("Real name should contain only a-z, A-Z, 0-9 and ");
		form["realName"].focus();
		return false;
	}
	
	var email = (form["email"].value).trim();
	var regex3 = /^[_a-z0-9-]+(\.[_a-z0-9-]+)*@[a-z0-9-]+(\.[a-z0-9-]+)*(\.[a-z]{2,4})$/;
	
	if(!regex3.test(email)) {
		form["email"].focus();
		alert("Invalid email address !");
		return false;
	}
	
	return true;
}
</script>

</head>
<body>
<div class="mainContainer">
<div class="formdiv">
	<form id="regform" action="RegisterServlet" method="post" onsubmit="return validate(this);">
		<span class="commonInputSpan">Username:</span><br>
		<input type="text" name="name" /><br> 
		<span class="commonInputSpan">Password:</span><br>
		<input type="password" name=pass><br>
		<span class="commonInputSpan">Real name:</span><br>
		<input type="text" name=realName><br>
		<span class="commonInputSpan">Email:</span><br>
		<input type="text" name=email><br>
		<br> 
		<button type="submit" value="Register" id="register1" >Register
		</button>
		<br>
	</form> 
	</div>
</div>
	<br>
	<br>
	
	<%if(request.getParameter("error") != null) {
		%>
		<div id="errormessage" style="color:red; text-weight:bold;">This username already exists in the database !</div>
		<%
	}
	%>
</body>
</html>