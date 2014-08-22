<%@ page import="java.util.*"%>
<%@ page import="servlets.*"%>
<%@ page import="dbObjects.*"%>
<%@ page import="java.security.Principal" %>
<%@ page import="java.text.SimpleDateFormat" %>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="css/bootstrap.min.css" type="text/css" />
<link rel="stylesheet" href="css/header.css" type="text/css"/>
<link rel="stylesheet" href="css/index.css" type="text/css"/>
<title>My Account</title>
<script src="js/jquery-1.11.1.min.js"></script>
<script src="js/load_header.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII" />
<title>Login</title>
</head>
<body>
	<div id="header"></div>
	<div id="sidebar"></div>
	<% 
	Principal p = request.getUserPrincipal();
	String username = p.getName();
	DatabaseMethods db = new DatabaseMethods();
	User u = db.getUserByUsername(username);
	SimpleDateFormat sdf = new SimpleDateFormat("M/d/yyyy");
	%>
	<h2 class="text-center"><%=username%></h2>
	<p id="status_message" class="text-center" style='display:none'></p>
	<%
		String upload_file = request.getParameter("upload_file");
		String delete_report_id = request.getParameter("delete_report_id");
		if (delete_report_id != null) {
			DatabaseMethods dbMethods = new DatabaseMethods();
			boolean result = dbMethods.deleteReport(Long.parseLong(delete_report_id));
			if (result == true) {
				%><script>$("#status_message").text("The report was successfully deleted.");
				$('#status_message').fadeIn(300).delay(1500).fadeTo(300, 0);</script><%
			}
			else {
				%><script>$("#status_message").text("Whoops, we did not succeed in deleting the report.");
				$('#status_message').fadeIn(300).delay(1500).fadeTo(300, 0);</script><%
			}
		}
		else if (upload_file != null) {
			%><script>$("#status_message").text("<%= upload_file %>");
			$('#status_message').fadeIn(300).delay(1500).fadeTo(300, 0);</script><%
		}
	%>

	<h3 class="text-center"><%="Welcome " + u.getFirstName() + " " + u.getLastName() + ", member since " + sdf.format(u.getJoinDate()) + "." %></h3>
	<p>
	
	<h3 class="text-center">My Recent Reports</h3>
	<%
		HashSet<Report> hsr = db.getReportsByUser(username);
	%>
	<%
		if (hsr.isEmpty()) {
	%>
	<p class="text-center">You have not submitted any reports yet.</p>
	<%
		} else {
	%>
	<ul class="text-center my_reports">
		<%
			for (Report r : hsr) {
		%>
		<li><a href="members/view_report?report_id=<%=r.get_id()%>"><%=(r.getTitle() == null ? "No Title Provided" : r.getTitle())%></a></li>
		<%
			}
		%>
		<% } %>
	</ul>
</body>
</html>