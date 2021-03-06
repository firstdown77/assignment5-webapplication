<%@page import="servlets.DatabaseMethods"%>
<%@ page import="java.util.*"%>
<%@ page import="dbObjects.*"%>
<%@ page import="servlets.*"%>
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
  Principal p = request.getUserPrincipal();
  boolean registered = false;
  boolean free = true;
  int registeredCount = 0;
  if ((request.getParameter("action")!=null)&&request.getParameter("action").equals("registered"))
  {
	  e = db.getRegisteredEvent(p.getName());
	  registered = true;
	  free = false;
  }
  else if ((request.getParameter("action")!=null)&&request.getParameter("action").equals("create"))
  {
	  	
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
		e.setRegistered(new User[0]);
		e.set_id(db.insertEvent(e));
  }else if (request.getParameter("event_id")!=null)
  {
	  Long eventId = new Long(request.getParameter("event_id"));
	  e = db.getEventById(eventId);
  }
  if ((registered) && (e != null))
	  registeredCount = e.getRegistered().length - 1;
  else if (e != null)
	  registeredCount = e.getRegistered().length;
%>

<%if (e == null){ %>
<div>
	<p class='text-center report_details'>No event data</p>
	<p class='text-center report_details'><a href="javascript:history.back()">Return</a></p>
</div>
<%}else{ %>
<div>
	<p class='text-center report_details'>Date: <%=e.getDate()%></p>
	<p class='text-center report_details'>Evacuation Means: </p>
	<p class='text-center report_details'><div id="divMeans" class="text-center  report_details "><%=e.getEvacuationMeans() %></div></p> 
	<p class='text-center report_details'>Capacity: <%=e.getCapacity()%></p>
</div>
<script type='text/javascript'>
var map;
var gusername;

function addUser()
{
	var name = $("#username").val();
	gusername = name;
	if (name != null)
		{

	$.ajax({
		url : "registerUserEvent?username="+name+"&event_id=<%=e.get_id().longValue() %>",
			type : "get",
			async : true,
			data : {},
			success: addUserCallback,
			error: userNotFound
	});
		}
}

function addUserCallback()
{
	$("#userStatusMessage").html("User added");	
	$("#divUsers").append("<div id='user"+gusername+"'>User Name: "+gusername+"&nbsp;&nbsp;&nbsp;<a href='javascript:unregisterList("+gusername+")'>Unregister</a></div>");
}

function userNotFound()
{
	$("#userStatusMessage").html("User couln't be added. Possibly registered already, not found or capacity exceeded");
}

function unregister()
{
	$.ajax({
		url : "registerUserEvent?action=unregister&username=<%=p.getName()%>&event_id=<%=e.get_id().longValue()%>",
			type : "get",
			async : true,
			data : {},
			success: unregisterCallback
	});
}

function unregisterList(name)
{
	$.ajax({
		url : "registerUserEvent?action=unregister&username="+name+"&event_id=<%=e.get_id().longValue()%>",
			type : "get",
			async : false,
			data : {},
	});
	$("#userStatusMessage").html("User unregistered");
	$("#user"+name).remove();
}

function register()
{
	$.ajax({
		url : "registerUserEvent?username=<%=p.getName() %>&event_id=<%=e.get_id().longValue() %>",
			type : "get",
			async : true,
			data : {},
			success: registerCallback,
			error: registererror
	});
}

function registererror(data, status)
{
	$("#statusMessage").html("Register unsuccessful");
}

function registerCallback(data, status)
{
	$("#statusRegistered").html("Status: registered");
	$("#statusMessage").html("Register successful");
	$("#buttondiv").html("<p class='text-center report_details'><button id='btnUnregister' type='button' onclick='unregister()'>Unregister</button></p>");
}

function unregisterCallback(data, status)
{
	$("#statusRegistered").html("Status: not registered");
	$("#statusMessage").html("Unregister successful");
	$("#buttondiv").html("<p class='text-center report_details'><button id='btnRegister' type='button' onclick='register()'>Register</button></p>");
}

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
<%
if (!registered)
{
	Event e2 = db.getRegisteredEvent(p.getName());
	if (e2 != null)
	{
		free = false;
		if (e2.get_id() == e.get_id())
		{
			registered = true;
		}
	}
}
%>

 <p id="statusMessage" class='text-center report_details statusmsg'></p>
 <p id="statusRegistered" class='text-center report_details'>Status: <%if (registered){ %>registered <%} else {%> not registered <%} %></p>
 <div id="buttondiv">
 <%if ((registered)){ %>
 <p class='text-center report_details'><button id="btnUnregister" type="button" onclick='unregister()'>Unregister</button></p>
 <%} else if ((free)&&(e.getRegistered().length < e.getCapacity())){ %>
 <p class='text-center report_details'><button id="btnRegister" type="button" onclick='register()'>Register</button></p>
 <%} %>
 </div>
 
 <%if (p.getName().equals(UserVariables.adminUsername)) { %>
 <p class='text-center report_details'>
 Users:</p>
 <p id="userStatusMessage" class='text-center report_details statusmsg'></p>
 <p class='text-center report_details'>
 Add user by username: <input type="text" id="username"/><button type="button" onclick="addUser()">Add</button></p>
 <div id="divUsers" class="text-center  report_details scroll">
<%
for (int i = 0; i < e.getRegistered().length; i++)
{
	User uu = e.getRegistered()[i];
	if (!uu.getUsername().equals(p.getName())){
	%>
		<div id="user<%=uu.getUsername()%>">User Name: <%=uu.getUsername() %>&nbsp;&nbsp;&nbsp;<a href="javascript:unregisterList(<%=uu.getUsername()%>)">Unregister</a></div>
	<%
	}
}
%>
 
 </div>
 <%}else{%>
 <p id="nonadmincount" class='text-center report_details'>
 Users registered except self: <%=registeredCount%></p>
 <%}%>
 <p class='text-center report_details'>Created By: <%if ((e.getUsername()==null) || (e.getUsername().length() == 0)) %> Username Unavailable <%else %> <%=e.getUsername() %></p>
 <%if (p.getName().equals(UserVariables.adminUsername)) { %>
 <p class='text-center report_details'><a href='/assignment5-webapplication/members/delete_event?event_id=<%=e.get_id() %>'>Delete Event</a></p>
 <%} %>

</div>
</div>
</div>
 <%} %>
</body>
</html>