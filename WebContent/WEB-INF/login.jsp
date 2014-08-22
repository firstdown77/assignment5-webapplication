<%@page import="servlets.DatabaseMethods"%>
<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"%>
<html>
<head>
<script src="/assignment5-webapplication/js/send_create_user_request.js"></script>
<link rel="stylesheet" href="css/bootstrap.min.css" type="text/css"/>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<script src="js/jquery-1.11.1.min.js"></script>
<title>Login</title>
</head>
<body>
<br /><br /><br /><br />
<p id="status_message" class="text-center">Welcome to the Disaster Evacuation Application.</p>
	<%
	if (request.getParameter("logout") != null && request.getParameter
			("logout").equals("true")) {
	%>
		<script>
		$("#status_message").html("You successfully logged out.");
		$('#status_message').fadeIn(300).delay(1500).fadeTo(300, 0);
		</script>
	<%
	} else if (request.getParameter("login_failed") != null && request.getParameter
	("login_failed").equals("true")) {
	%>
		<script>
		$("#status_message").html("Whoops, your username or password were"
				+ " incorrect.  Please try again.");
		$('#status_message').fadeIn(300).delay(1500).fadeTo(300, 0);
		</script>
	<%
	} else if (request.getParameter("action") != null && request.getParameter(
			"action").equals("delete_account")) {
	%>
		<script>
		$("#status_message").html("You successfully deleted your account.");
		$('#status_message').fadeIn(300).delay(1500).fadeTo(300, 0);
		</script>
	<%
	}
	%>
<h2 class="text-center">Log In</h2><br />
<form name="input" action="j_security_check" method="post" class="text-center">
	<input type="text" name="j_username" onfocus="if (this.value=='Username') this.value=''" value="Username"
	 onblur="if (this.value=='') this.value='Username'"/><br /><br />
	<input type="password" name="j_password" onfocus="if (this.value=='Password') this.value=''" value="Password"
	 onblur="if (this.value=='') this.value='Password'"/><br /><br />
	<input type="submit" id="btnSubmit" name="btnSubmit" value="Log In" />
</form>
<br />
<h2 class="text-center">Or, Register</h2>
<form name="input" class="text-center" action="j_security_check" method="post">
	<input type="text" name="firstname" onfocus="if (this.value=='First Name') this.value=''"
		value="First Name"
		onblur="if (this.value=='') this.value='First Name'" /><br /> <br />
	<input type="text" name="lastname" onfocus="if (this.value=='Last Name') this.value=''"
		value="Last Name" onblur="if (this.value=='') this.value='Last Name'" />
	<br /> <br /> <input type="text" name="j_username"
		onfocus="if (this.value=='Username') this.value=''" value="Username"
		onblur="if (this.value=='') this.value='Username'" /><br /> <br />
	<input type="password" name="j_password" onfocus="if (this.value=='Password') this.value=''"
		value="Password" onblur="if (this.value=='') this.value='Password'" /><br />
	<br /> <input type="submit" onclick="createUser(j_username.value, j_password.value, firstname.value, lastname.value)" value="Register" />
</form>
</body>
</html>