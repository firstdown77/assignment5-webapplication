<%@ page import="java.util.*"%>
<%@ page import="servlets.*"%>
<%@ page import="dbObjects.*"%>
<%@ page import="java.security.Principal" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="css/bootstrap.min.css" type="text/css" />
<link rel="stylesheet" href="css/header.css" type="text/css"/>
<link rel="stylesheet" href="css/index.css" type="text/css"/>
<link rel="stylesheet" href="/assignment5-webapplication/css/view_report.css" type="text/css" />
<title>My Account</title>
<script src="js/jquery-1.11.1.min.js"></script>
<script src="js/load_header.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII" />
<title>Login</title>
</head>
<body>
	<div id="header"></div>
	<div id="content-body-wrapper" class="content-body-wrapper">
    <div id="content-body" class="content-body">
	<div id="sidebar"></div>
	<div id="content">
	<% 
	Principal p = request.getUserPrincipal();
	String username = p.getName();
	DatabaseMethods db = new DatabaseMethods();
	User u = db.getUserByUsername(username);
	SimpleDateFormat sdf = new SimpleDateFormat("M/d/yyyy");
	%>
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
	<br /><br />
	<h3 class="text-center"><%= u.getFirstName() + " " + u.getLastName() + ", member since " + sdf.format(u.getJoinDate()) %></h3>
	<p>
	<br />
	<div style='width: 700px; margin:0 auto;'>
	<h4 class="text-center">My Reports</h4>
	<%
		List<Report> hsr = db.getReportsByUser(username);
		request.setAttribute("test", hsr);

	%>
		<center>
			<font size="4"> <display:table name="test" pagesize="5">
					<display:setProperty name="paging.banner.item_name" value="report" />
					<display:setProperty name="paging.banner.items_name"
						value="reports" />
					<display:setProperty name="paging.banner.one_item_found" value="" />
					<display:setProperty name="paging.banner.all_items_found" value="" />
					<display:setProperty name="basic.msg.empty_list" value="None" />
					<display:setProperty name="paging.banner.some_items_found"
					value="<span class='pagebanner'>{0} total {1}, displaying {2} to {3}. </span>" />
					<display:setProperty name="paging.banner.onepage" value="" />			
					<display:column property="title" title="<center><u>Title</u></center>" style="width:350px; text-align:center;"
						sortable="true"
						href="/assignment5-webapplication/members/view_report"
						paramId="report_id" paramProperty="_id" />&nbsp;
		<display:column property="address" title="<center><u>Address</u></center>"
						style="width:350px; text-align:center;" sortable="true" />&nbsp;

	</display:table>
			</font>
		</center>
	</div>
	</div>
	</div>
	</div>
</body>
</html>