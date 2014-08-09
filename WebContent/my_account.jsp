<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="css/bootstrap.min.css" type="text/css" />
<link rel="stylesheet" href="css/header.css" type="text/css"/>
<title>My Account</title>
<script src="js/jquery-1.11.1.min.js"></script>
<script src="js/load_header.js"></script>

<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII" />
<title>Login</title>
</head>
<body>
	<%@ page import="java.util.*"%>
	<%@ page import="servlets.*"%>
	<%@ page import="dbObjects.*"%>
	<div id="header"></div>
	<h2 class="text-center"><%=request.getParameter("username")%></h2>
	<p id="status_message" class="text-center" style='display:none'></p>
	<%
		if (request.getParameter("delete_report_id") != null) {
			DatabaseMethods dbMethods = new DatabaseMethods();
			boolean result = dbMethods.deleteReport(Long.parseLong(request.getParameter("delete_report_id")));
			if (result == true) {
				%><script>$("#status_message").text("The report was successfully deleted.");
				$('#status_message').fadeIn(400).delay(1500).fadeOut(400);</script><%
			}
			else {
				%><script>$("#status_message").text("Whoops, we did not succeed in deleting the report.");
				$('#status_message').fadeIn(400).delay(1500).fadeOut(400);</script><%
			}
		}
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
	<%
		if (hsr.isEmpty()) {
	%>
	<p>You have not submitted any reports yet.</p>
	<%
		} else {
	%>
	<ul>
		<%
			for (Report r : hsr) {
		%>
		<li><a href="view_report?report_id=<%=r.get_id()%>"><%=(r.getTitle() == null ? "No Title Provided" : r.getTitle())%></a></li>
		<%
			}
		%>
		<% } %>
	</ul>
</body>
</html>