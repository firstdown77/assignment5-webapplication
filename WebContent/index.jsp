<%@page import="servlets.DatabaseMethods"%>
<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" href="css/bootstrap.min.css" type="text/css"/>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<title>Login</title>
</head>
<body>
<% if(request.getParameter("btnSubmit") != null) {
    DatabaseMethods db = new DatabaseMethods();
    boolean loginSuccess = db.verifyPassword(request.getParameter("username"),
    		request.getParameter("password"));
    if (loginSuccess) {
    	%><script>alert('Login successful');</script><%
    } else {
    	%><script>alert('Login failed')</script><%
    }
} else { %>
<h2 class="text-center">Log In</h2><br />
<p class="text-center">Or, <a href="register.jsp">Register</a></p>
<form name="input" action="my_account.jsp" method="post" class="text-center">
	<input type="text" name="username" onfocus="if (this.value==='Username') this.value=''" value="Username"
	 onblur="if (this.value=='') this.value='Username'"/><br /><br />
	<input type="password" name="password" onfocus="if (this.value==='Password') this.value=''" value="Password"
	 onblur="if (this.value=='') this.value='Password'"/><br /><br />
	<input type="submit" id="btnSubmit" name="btnSubmit" value="Log In" />
</form>
<% } %>
</body>
</html>