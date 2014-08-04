<?xml version="1.0" encoding="US-ASCII" ?>
<%@page import="testPackage.DatabaseMethods"%>
<%@ page language="java" contentType="text/html; charset=US-ASCII"
	pageEncoding="US-ASCII"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<link rel="stylesheet" href="css/bootstrap.min.css" type="text/css" />
<script src="js/jquery-1.11.1.min.js"></script>
<script src="js/load_header.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII" />
<title>View All Reports</title>
</head>
<body>
	<div id="header"></div>
	<h2 class="text-center">View All Reports</h2>
	<%@ page import="java.util.*"%>
	<%@ page import="dbObjects.*"%>
	<%
		DatabaseMethods db = new DatabaseMethods();
	%>
	<%
		HashSet<Report> allReports = db.getAllReports();
	%>
	<ul>
		<%
			if (allReports.isEmpty()) {
		%>
		<p>There are no reports in the system yet.</p>
		<%
			}
		%>
		<%
			for (Report r : allReports) {
		%>
		<li><a href="view_report?report_id=<%=r.get_id()%>"><%=r.getTitle()%></a></li>
		<%
			}
		%>
	</ul>
</body>
</html>