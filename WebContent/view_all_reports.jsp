<?xml version="1.0" encoding="US-ASCII" ?>
<%@page import="servlets.DatabaseMethods"%>
<%@ page language="java" contentType="text/html; charset=US-ASCII"
	pageEncoding="US-ASCII"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<link rel="stylesheet" href="css/bootstrap.min.css" type="text/css" />
<link rel="stylesheet" href="css/header.css" type="text/css" />
<script src="js/jquery-1.11.1.min.js"></script>
<script src="js/load_header.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII" />
<meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
<style type="text/css">
html {
	height: 100%;
}

body {
	height: 100%;
	margin: 0;
	padding: 0;
}

#map-canvas {
	height: 60%;
	width: 60%;
	margin-left: auto;
	margin-right: auto;
}
</style>
<script type="text/javascript"
	src="https://maps.googleapis.com/maps/api/js?key=AIzaSyA5VLYkZvLXln72Q2FaNEj6O3H2F0yZsVY">
</script>

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
	<script type="text/javascript">
	var map;
      function initialize() {
    	  var mapOptions = {
    	  	zoom: 3,
    		center: new google.maps.LatLng(37.09024, -95.712891),
    		mapTypeId: google.maps.MapTypeId.TERRAIN
    	  };

        map = new google.maps.Map(document.getElementById("map-canvas"),
            mapOptions);
        var count = 0;
        <%for (Report r : allReports) {%>
        	count++;
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
	<%}%>
		}
		google.maps.event.addDomListener(window, 'load', initialize);
	</script>
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
					map.setCenter(new google.maps.LatLng(37.09024, -95.712891));
				}
				else if (this.value==="southamerica") {
					map.setCenter(new google.maps.LatLng(-8.837410, -55.502930))
				}
				else if (this.value==="europe") {
					map.setCenter(new google.maps.LatLng(54.421909, 15.320313));
				}
				else if (this.value==="asia") {
					map.setCenter(new google.maps.LatLng(33.652606, 100.878906));
				}
				else if (this.value==="africa") {
					map.setCenter(new google.maps.LatLng(1.971095, 27.180162));
				}
				else if (this.value==="australia") {
					map.setCenter(new google.maps.LatLng(-25.547440, 133.838349));
				}
			});
		</script>
	</div>
	<br />
	<div id="map-canvas" />
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