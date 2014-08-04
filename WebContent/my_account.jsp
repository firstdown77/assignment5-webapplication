<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="css/bootstrap.min.css" type="text/css" />
<title>My Account</title>
<script src="js/jquery-1.11.1.min.js"></script>
<script src="js/load_header.js"></script>

<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII" />
<title>Login</title>
</head>
<body>
	<div id="header"></div>
	<h2 class="text-center"><%=request.getParameter("username")%></h2>
	<%@ page import="java.util.*"%>
	<%@ page import="testPackage.*"%>
	<%@ page import="dbObjects.*"%>
	<%
		UserVariables.username = request.getParameter("username");
	%>

	<h2><%=request.getParameter("firstname")%>
		<%=request.getParameter("lastname")%></h2>
	<ul>
		<li>View All Users</li>
		<li><a href="view_all_reports.jsp">View All Reports</a></li>
		<li>View All Evacuation Events</li>
		<li>Delete Account</li>
		<li><a href="create_report.html">Create New Report</a></li>
		<li>My Registrations for Evacuation Events</li>
		<%
			if (request.getParameter("username").equals(UserVariables.adminUsername)) {
		%>
		<li><a href="create_evacuation_event.html">Create Evacuation
				Event</a></li>
		<%
			}
		%>
	</ul>
	<p>
	<h3>My Reports</h3>
	<%
		DatabaseMethods db = new DatabaseMethods();
	%>
	<%
		HashSet<Report> hsr = db.getReportsByUser(request.getParameter("username"));
	%>
	<ul>
		<%
			if (hsr.isEmpty()) {
		%>
		<p>You have not submitted any reports yet.</p>
		<%
			} else {
		%>
		<%
			for (Report r : hsr) {
		%>
		<li><a href="view_report?report_id=<%=r.get_id()%>"><%=r.getTitle()%></a></li>
		<%
			}
		%>
		<% } %>
	</ul>
</body>
</html>