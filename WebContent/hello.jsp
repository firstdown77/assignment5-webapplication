<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="css/bootstrap.min.css" type="text/css"/>
<title>My Account</title>
<script src="js/jquery-1.11.1.min.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII" />
<title>Login</title>
<script>$(function(){
	  $("#header").load("header.html"); 
	});
</script>
</head>
<body>
<div id="header"></div>
<h2 class="text-center">My Account</h2>
<%@ page import="java.util.*" %>
<h2><%= request.getParameter("firstname") %> <%= request.getParameter("lastname") %></h2>
<p>
Username: <%= request.getParameter("username") %> <br />
Password: <%= request.getParameter("password") %>

<ul>
	<li>View All Users</li>
	<li>View All Reports</li>
	<li>View All Evacuation Events</li>
	<li>Delete Account</li>
	<li>Create New Report</li>
	<li>My Reports</li>
	<li>My Registrations for Evacuation Events</li>
	<% if (request.getParameter("username").equals("firstdown77")) { %>
		<li>Create Evacuation Event</li>
	<% } %>
</ul>
</p>
</body></html>