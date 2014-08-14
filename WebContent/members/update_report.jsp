<%@ page language="java" contentType="text/html; charset=US-ASCII"
	pageEncoding="US-ASCII"%>
<%@ page import="java.util.*"%>
<%@ page import="servlets.*"%>
<%@ page import="dbObjects.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" href="/assignment5-webapplication/css/bootstrap.min.css" type="text/css" />
<link rel="stylesheet" href="/assignment5-webapplication/css/header.css" type="text/css" />
<style>
#textcontentdiv * {
	vertical-align: top;
}
</style>
<script src="/assignment5-webapplication/js/jquery-1.11.1.min.js"></script>
<script src="/assignment5-webapplication/js/load_header.js"></script>
<script src="/assignment5-webapplication/js/geocoder.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII" />
<title>Update Report</title>
</head>
<body>
	<div id="header"></div>
	<h2 class="text-center">Update Report</h2>
	<%
		Report r;
		if (request.getParameter("update_report_id") == null) {
			r = null;
			System.out.println("No update report id");
		} else {
			DatabaseMethods db = new DatabaseMethods();
			r = db.getReportById(Long.parseLong(request
					.getParameter("update_report_id")));
			
		}
	%>

	<form name="input"
		action="view_report?action=update&report_id=<%=request.getParameter("update_report_id") %>"
		method="post" class="text-center" enctype="multipart/form-data">
		Title: &nbsp;<input type="text" name="title" id="title"
			onfocus="if (this.value==='Title') this.value=''"
			value="<%= (r.getTitle() == null ? "Title" : r.getTitle()) %>"
			onblur="if (this.value=='') this.value='Title'" /><br /> <br />
			Address: &nbsp;<input
			type="text" size="50" name="address" id="address"
			onfocus="if (this.value==='Address') this.value=''"
			value="<%= (r.getAddress() == null ? "Address" : r.getAddress()) %>"
			onblur="if (this.value=='') this.value='Address'" /> &nbsp;&nbsp; Or
		&nbsp;&nbsp;
		<div style="display: inline; width: 10px;">
			<input type="text" name="latitude" id="latitude"
				onfocus="if (this.value==='Latitude') this.value=''"
				value="<%= (r.getLatitude() == null ? "Latitude" : r.getLatitude()) %>"
				onblur="if (this.value=='') this.value='Latitude'" /> <input
				type="text" style="position: relative;" name="longitude"
				id="longitude" onfocus="if (this.value==='Longitude') this.value=''"
				value="<%= (r.getLongitude() == null ? "Longitude" : r.getLongitude()) %>"
				onblur="if (this.value=='') this.value='Longitude'" />
		</div>
		<br /> <br />Radius: &nbsp;<input type="text" name="radius"
			onfocus="if (this.value==='Radius (km)') this.value=''"
			value="<%= (r.getRadius() == null ? "Radius (km)" : r.getRadius()) %>"
			onblur="if (this.value=='') this.value='Radius (km)'" /><br /><br />
			<div id="textcontentdiv">
		Text Content: &nbsp;<textarea name="textcontent" rows="4" cols="50"
			onfocus="if (this.value==='Write report content...') this.value=''"
			onblur="if (this.value=='') this.value='Write report content...'"><%= (r.getTextcontent() == null ? "Write report content..." : r.getTextcontent()) %></textarea>
		</div><br /> <br />
		<center>
			Current File: <a
				href='view_file?filename=<%=r.getFilename() %>' target='_blank'><%=r.getFilename()%></a><br /><br />
			Upload New File: &nbsp;&nbsp;<input type="file"
				style="display: inline;" name="content"
				onfocus="if (this.value==='Content') this.value=''" value="Content"
				onblur="if (this.value=='') this.value='Content'" />
		</center>
		<br /> <br /> <input type="submit"
			onclick="doGeoCode(document.getElementById('address').value, document.getElementById('latitude').value, document.getElementById('longitude').value)"
			value="Update Report" />
	</form>

</body>
</html>