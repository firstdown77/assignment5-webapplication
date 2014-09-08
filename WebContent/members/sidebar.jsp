<%@ page import="java.util.*"%>
<%@ page import="servlets.*"%>
<%@ page import="java.security.Principal" %>

<ul class="sidebar">
	<!-- <li>My Registrations</li> -->
	<li><a href="/assignment5-webapplication/members/create_report.html">Create Report</a></li>
	<li><a href="/assignment5-webapplication/members/view_all_reports.jsp">All Reports</a></li>
	<li><a href="/assignment5-webapplication/members/view_all_users.jsp">All Users</a></li>
	<!-- <li>All Evacuation Events</li> -->
	<%
		Principal p = request.getUserPrincipal();
		String username = p.getName();
		if (username.equals(UserVariables.adminUsername)) {
	%>
	<!-- <li>Create Evacuation Event</li> -->
	<li><a href="/assignment5-webapplication/members/upload_file.html">Upload Initial Data</a></li>
	<li><a href="/assignment5-webapplication/members/create_event.html">Create Evacuation Event</li>
	<%
		} else { /* Don't let the admin delete his account */
    %>
    	<li><a onclick="deleteAccount()" href="/assignment5-webapplication?action=delete_account">Delete Account</a></li>
    <%
		}
	%>
</ul>