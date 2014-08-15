<%@ page language="java" contentType="text/html; charset=US-ASCII"
	pageEncoding="US-ASCII"%>
<%@ page import="javax.crypto.Cipher"%>
<%@ page import="javax.crypto.KeyGenerator"%>
<%@ page import="javax.crypto.SecretKey"%>
<%@ page import="servlets.*"%>
<%@ page import="dbObjects.*"%>
<%@ page import="java.sql.Date"%>
<%@ page import="java.util.Arrays"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet"
	href="/assignment5-webapplication/css/bootstrap.min.css"
	type="text/css" />
<link rel="stylesheet" href="/assignment5-webapplication/css/header.css"
	type="text/css" />
<script src="/assignment5-webapplication/js/send_create_user_request.js"></script>

<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII" />
<title>Register</title>
</head>
<body>
	<h2 class="text-center">Register</h2>
	<br />
	<form name="input" class="text-center" action="/assignment5-webapplication/j_security_check" method="post">
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
		<br /> <input type="submit" onclick="createUser()" value="Register" />
	</form>
</body>
</html>