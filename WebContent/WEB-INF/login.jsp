<%@page import="servlets.DatabaseMethods"%>
<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" href="css/bootstrap.min.css" type="text/css"/>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<script src="js/jquery-1.11.1.min.js"></script>
<title>Login</title>
</head>
<body>
<br /><br /><br /><br />
<p id="status_message" class="text-center" style='display:none'></p>
	<%
	if (request.getParameter("logout") != null && request.getParameter
			("logout").equals("true")) {
	%>
		<script>
		$("#status_message").html("<br />You successfully logged out.");
		$('#status_message').fadeIn(300).delay(1500).fadeTo(300, 0);
		</script>
	<%
	} else if (request.getParameter("login_failed") != null && request.getParameter
	("login_failed").equals("true")) {
	%>
		<script>
		$("#status_message").html("<br />Whoops, your username or password were"
				+ " incorrect.  Please try again.");
		$('#status_message').fadeIn(300).delay(1500).fadeTo(300, 0);
		</script>
	<%
	}
	%>
<h2 class="text-center">Log In</h2><br />
<p class="text-center">Or, <a href="entry/register.jsp">Register</a></p>
<form name="input" action="j_security_check" method="post" class="text-center">
	<input type="text" name="j_username" onfocus="if (this.value=='Username') this.value=''" value="Username"
	 onblur="if (this.value=='') this.value='Username'"/><br /><br />
	<input type="password" name="j_password" onfocus="if (this.value=='Password') this.value=''" value="Password"
	 onblur="if (this.value=='') this.value='Password'"/><br /><br />
	<input type="submit" id="btnSubmit" name="btnSubmit" value="Log In" />
</form>
</body>
</html>