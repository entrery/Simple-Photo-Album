<%@ page language="java" session="false" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>	
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Login Page</title>
<link rel="stylesheet" href="resources/css/photos.css" type="text/css" />

<script type="text/javascript">

function validate(form) {
	var categoryName = (form["name"].value).trim();
	var regex = /^[a-zA-Z0-9_]+$/;
	
	if(regex.test(categoryName)) {
		return true;
	} else {
		alert("Username should contain only a-z, A-Z, 0-9 and _");
		form["name"].focus();
		return false;
		
	}
}
</script>


</head>
<body>

<div class="mainContainer">
	<div class="formdiv">
		<form id="loginform" action="LoginServlet" method="post" onsubmit="return validate(this);">
			<span class="commonInputSpan">Name: </span><br>
			<input type="text" name="name" /><br> 
			<span class="commonInputSpan">Password:</span><br>
			<input type="password" name=pass><br>
				<br>
			 <button type="submit" value="Login" id="login" >Login
			</button>
			<br>
		</form> 
	</div>
	<br>
	<a href="registerPage.jsp" id="register">register</a>
</div>
<br>
<%if(request.getParameter("error") != null) {
		%>
		<div id="errormessage" style="color:red; text-weight:bold;">Invalid credentials !</div>
		<%
	}
	%>
</body>
</html>    