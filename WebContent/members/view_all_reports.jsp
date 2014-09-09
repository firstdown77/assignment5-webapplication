<%@page import="servlets.DatabaseMethods"%>
<%@ page import="java.util.*"%>
<%@ page import="dbObjects.*"%>
<%@ page language="java" contentType="text/html; charset=US-ASCII"
	pageEncoding="US-ASCII"%>
<html>
<head>
<link rel="stylesheet" href="/assignment5-webapplication/css/bootstrap.min.css" type="text/css" />
<link rel="stylesheet" href="/assignment5-webapplication/css/header.css" type="text/css" />
<link rel="stylesheet" href="/assignment5-webapplication/css/googlemapcode.css" type="text/css" />
<link rel="stylesheet" href="/assignment5-webapplication/css/view_report.css" type="text/css" />
<script src="/assignment5-webapplication/js/jquery-1.11.1.min.js"></script>
<script src="/assignment5-webapplication/js/load_header.js"></script>
<script src="/assignment5-webapplication/js/continentCenterCoords.js"></script>
<script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?
key=AIzaSyA5VLYkZvLXln72Q2FaNEj6O3H2F0yZsVY"></script>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII" />
<meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
<%
		boolean events = false;
		boolean upcoming = false;
		DatabaseMethods db = new DatabaseMethods();
		double lng = -95.712891;
		double lat = 37.09024;
		String mode = request.getParameter("mode");
		if((mode != null)&&(mode.equals("events")))
			events = true;
		if((mode != null)&&(mode.equals("eventsUpcoming"))){
			upcoming=true;
			events=true;
		}
		if(!events){
%>
<title>View All Reports</title>
<%}else{ %>
<title>View Events</title>
<%} %>
</head>
<body>
	<div id="header"></div>
	<div id="content-body-wrapper" class="content-body-wrapper">
    <div id="content-body" class="content-body">
	<div id="sidebar"></div>
	<div id="content">
	<%if (!events){ %>
	<h2 class="text-center">View All Reports</h2>
	<%}else if (!upcoming){ %>
	<h2 class="text-center">View All Events</h2>
	<%}else{%>
	<h2 class="text-center">View Upcoming Events</h2>
	<%}
	ArrayList<Report> allReports = new ArrayList<Report>();
	ArrayList<Event> allEvents = new ArrayList<Event>();
		
	if (!events)	
		allReports = db.getAllReports();
	else if (upcoming)
		allEvents = db.getUpcomingEvents();
	else
		allEvents = db.getAllEvents();
	%>
	<%	boolean empty = false; 
	if (!events) {
		empty = allReports.isEmpty();
	}else{
		empty = allEvents.isEmpty();
	}
		if (!empty) {  
		if (events){
			Event e = allEvents.get(0);
			if (e != null)
			{
				lng = e.getLongitude().doubleValue();
				lat = e.getLatitude().doubleValue();
			}
		}else{
			Report r = allReports.get(0); 
			if (r != null)
			{
				lng = r.getLongitude().doubleValue();
				lat = r.getLatitude().doubleValue();
			}
		}
			%>
		<script type="text/javascript"> /* This script uses Java to display the map. */
		var map;
	      function initialize() {
	    	  var mapOptions = {
	    	  	zoom: 3,
	    		center: new google.maps.LatLng(<%=lat %>, <%=lng %>),
	    		mapTypeId: google.maps.MapTypeId.TERRAIN
	    	  };
	
	        map = new google.maps.Map(document.getElementById("map-canvas"),
	            mapOptions);
	        <% if (!events){
	        for (Report r : allReports) {
	        	if ((r.getLatitude() != null) && (r.getLongitude() != null)) { %>
	        	var CONVERTTOKM = 1000;
	        	var currCenter = new google.maps.LatLng(<%=r.getLatitude()%> , <%=r.getLongitude()%>);
	        	var currRadius = <%=r.getRadius()%> * CONVERTTOKM;
	        	var currTitle = "<%=r.getTitle()%>";
	            var populationOptions = {
	              strokeColor: '#FF0000',
	              strokeOpacity: 0.8,
	              strokeWeight: 2,
	              fillColor: '#FF0000',
	              fillOpacity: 0.35,
	              map: map,
	              center: currCenter,
	              radius: currRadius
	            };
	            // Add the circle for this city to the map.
	            cityCircle = new google.maps.Circle(populationOptions);
	            var marker = new google.maps.Marker({
	                position: currCenter,
	                map: map,
	                title: currTitle
	            });
	            
	            google.maps.event.addListener(marker, 'click', function() {
	                window.location.href = "view_report?report_id=<%=r.get_id()%>";
				});
				<%}
	        }
	        }else{
	        	for (Event e: allEvents) {
	        	if (e.getLatitude() != null && e.getLongitude() != null) { %>
	        	var currCenter = new google.maps.LatLng(<%=e.getLatitude()%> , <%=e.getLongitude()%>);
	        	var currTitle = "Evacuation <%=e.get_id()%>";
	            var marker = new google.maps.Marker({
	                position: currCenter,
	                map: map,
	                title: currTitle
	            });
	            
	            google.maps.event.addListener(marker, 'click', function() {
	                window.location.href = "view_event.jsp?event_id=<%=e.get_id()%>";
				});
	        	<%}
	        	}
	        }%>
		}
		google.maps.event.addDomListener(window, 'load', initialize);
		</script> <!-- End script with Java -->
		<div class="text-center">
			<select name="continents">
				<option value="choose">Choose a continent...</option>
				<option value="northamerica">North America</option>
				<option value="southamerica">South America</option>
				<option value="europe">Europe</option>
				<option value="asia">Asia</option>
				<option value="africa">Africa</option>
				<option value="australia">Australia</option>
			</select>
			<script>
				$('select').on('change', function() {
					if (this.value==="northamerica") {
						map.setCenter(new google.maps.LatLng(
								ContinentCoords.northamerica.lat,
								ContinentCoords.northamerica.lon));
					}
					else if (this.value==="southamerica") {
						map.setCenter(new google.maps.LatLng(
								ContinentCoords.southamerica.lat,
								ContinentCoords.southamerica.lon))
					}
					else if (this.value==="europe") {
						map.setCenter(new google.maps.LatLng(
								ContinentCoords.europe.lat, 
								ContinentCoords.europe.lon));
					}
					else if (this.value==="asia") {
						map.setCenter(new google.maps.LatLng(
								ContinentCoords.asia.lat, 
								ContinentCoords.asia.lon));
					}
					else if (this.value==="africa") {
						map.setCenter(new google.maps.LatLng(
								ContinentCoords.africa.lat,
								ContinentCoords.africa.lon));
					}
					else if (this.value==="australia") {
						map.setCenter(new google.maps.LatLng(
								ContinentCoords.australia.lat,
								ContinentCoords.australia.lon));
					}
				});
			</script>
		</div>
		<br />
	<%
	} else {
	%>
		<p class="text-center">There are no data in the system yet.</p>
		<%
			}
		%>
	<div id="map-canvas"></div>
	</div>
	</div>
	</div>
</body>
</html>