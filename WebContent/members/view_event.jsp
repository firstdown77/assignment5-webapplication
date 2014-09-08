<%@page import="servlets.DatabaseMethods"%>
<%@ page import="java.util.*"%>
<%@ page import="dbObjects.*"%>
<%@ page import="java.security.Principal"%>
<%@ page language="java" contentType="text/html; charset=US-ASCII"
	pageEncoding="US-ASCII"%>
<?xml version="1.0" encoding="US-ASCII" ?>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<link rel='stylesheet' href='/assignment5-webapplication/css/bootstrap.min.css' type='text/css'/>
<link rel='stylesheet' href='/assignment5-webapplication/css/header.css' type='text/css'/>
<link rel='stylesheet' href='/assignment5-webapplication/css/view_report.css' type='text/css'/> 
<link rel='stylesheet' href='/assignment5-webapplication/css/googlemapcode.css' type='text/css' /> 
<script src='/assignment5-webapplication/js/jquery-1.11.1.min.js'></script>
<script src='/assignment5-webapplication/js/load_header.js'></script>
<script type='text/javascript' src='https://maps.googleapis.com/maps/api/js?key=AIzaSyA5VLYkZvLXln72Q2FaNEj6O3H2F0yZsVY'></script>
<meta http-equiv='Content-Type' content='text/html; charset=US-ASCII' /> 
<meta name='viewport' content='initial-scale=1.0, user-scalable=no' />
<title>View Event</title></head>
<body>
<div id='header'></div>
<div id="content-body-wrapper" class="content-body-wrapper">
    <div id="content-body" class="content-body">
<div id='sidebar'></div>
<div id='content'>
<h1 class='text-center'>View Evacuation Event</h1>
<%
  DatabaseMethods db = new DatabaseMethods();
  Event e = new Event();
  if ((request.getParameter("action")!=null)&&request.getParameter("action").equals("create"))
  {
	  	Principal p = request.getUserPrincipal();
		String username = p.getName();
		String hour = request.getParameter("hour");
		if (hour.length() == 1) hour = "0"+hour;
		String minute = request.getParameter("minute");
		if (minute.length() == 1) minute = "0"+minute;
		
		String date = request.getParameter("datepicker") + " " + hour + ":" + minute;
		String means = request.getParameter("means");
		int capacity = Integer.parseInt(request.getParameter("capacity"));
		double longitude = Double.parseDouble(request.getParameter("longitude"));
		double latitude = Double.parseDouble(request.getParameter("latitude"));
		
		e.setDate(date);
		e.setLongitude(longitude);
		e.setLatitude(latitude);
		e.setEvacuationMeans(means);
		e.setCapacity(capacity);
		e.setUsername(username);
		e.set_id(db.insertEvent(e));
  }else if (request.getParameter("event_id")!=null)
  {
	  Long eventId = new Long(request.getParameter("event_id"));
	  e = db.getEventById(eventId);
  }
%>
<div>
	<p class='text-center report_details'>Date: <%=e.getDate()%></p>
	<p class='text-center report_details'>Evacuation Means: <%=e.getEvacuationMeans() %></p> 
	<p class='text-center report_details'>Capacity: <%=e.getCapacity()%></p>
</div>
<script type='text/javascript'>
var map;
function initialize() {
	var mapOptions = {
	zoom: 12,
	center: new google.maps.LatLng(<%=e.getLatitude() %>, <%=e.getLongitude()%>),
	mapTypeId: google.maps.MapTypeId.TERRAIN
	};
	map = new google.maps.Map(document.getElementById('map-canvas'),mapOptions);
	var CONVERTTOKM = 1000;
	var currCenter = new google.maps.LatLng(<%=e.getLatitude() %>, <%=e.getLongitude()%>);
	var populationOptions = {
	strokeColor: '#FF0000',
	strokeOpacity: 0.8,
	strokeWeight: 2,
	fillColor: '#FF0000',
	fillOpacity: 0.35,
	map: map,
	center: currCenter,
	};
	var marker = new google.maps.Marker({
	position: currCenter,
	map: map,
	title: "Evacuation Event"
});
}
google.maps.event.addDomListener(window, 'load', initialize);
</script>
<div id='map-canvas'></div>
 <p class='text-center report_details'>Created By: <%if ((e.getUsername()==null) || (e.getUsername().length() == 0)) %> Username Unavailable <%else %> <%=e.getUsername() %></p>
 <p class='text-center report_details'><a href='/assignment5-webapplication/members/delete_event?event_id=<%=e.get_id() %>'>Delete Event</a></p>
</div>
</div>
</div>
</body>
</html>