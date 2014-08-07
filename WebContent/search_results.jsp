<?xml version="1.0" encoding="US-ASCII" ?>
<%@page import="servlets.DatabaseMethods"%>
<%@ page language="java" contentType="text/html; charset=US-ASCII"
	pageEncoding="US-ASCII"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII" />
<link rel="stylesheet" href="css/bootstrap.min.css" type="text/css" />
<link rel="stylesheet" href="css/search_results.css" type="text/css" />
<link rel="stylesheet" href="css/header.css" type="text/css" />
<script src="js/jquery-1.11.1.min.js"></script>
<script src="js/load_header.js"></script>
<title>Search Results</title>
</head>
<body>
	<%@ page import="java.util.*"%>
	<%@ page import="dbObjects.*"%>
	<div id="header"></div>
	<h2 class="text-center">Search Results</h2>
	<%
		DatabaseMethods db = new DatabaseMethods();
		HashSet<Report> resultSet = db.searchReports(request
				.getParameter("search_query"));
	%>
	<%
		if (!resultSet.isEmpty()) {
	%>
	<table border="1" class="normalborder">
		<thead class="normalborder">
			<th class="normalborder">Title</th>
			<th class="normalborder">User</th>
			<th class="normalborder">Address</th>
			<th class="normalborder">Content</th>
		</thead>
		<%
			for (Report r : resultSet) {
		%>
		<tr class="normalborder">
			<td class="normalborder"><a
				href="view_report?report_id=<%=r.get_id()%>"><%=r.getTitle()%></a></td>
			<td class="normalborder"><%=r.getUser()%></td>
			<td class="normalborder"><%=r.getAddress()%></td>
			<td class="normalborder"><a
				href='view_file?filename=<%=r.getFilename()%>' target='_blank'><%=r.getFilename()%></a></td>
		</tr>
		<%
			}
		%>
		<%
			} else {
		%>
		<p class="text-center">Whoops, there are no results. Please try
			again.</p>
		<%
			}
		%>
	</table>
</body>
</html>