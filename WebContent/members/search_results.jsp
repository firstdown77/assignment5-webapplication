<%@page import="servlets.DatabaseMethods"%>
<%@ page import="java.util.*"%>
<%@ page import="dbObjects.*"%>
<%@ page language="java" contentType="text/html; charset=US-ASCII"
	pageEncoding="US-ASCII"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII" />
<link rel="stylesheet" href="/assignment5-webapplication/css/bootstrap.min.css" type="text/css" />
<link rel="stylesheet" href="/assignment5-webapplication/css/search_results.css" type="text/css" />
<link rel="stylesheet" href="/assignment5-webapplication/css/header.css" type="text/css" />
<link rel="stylesheet" href="/assignment5-webapplication/css/googlemapcode.css" type="text/css" />
<script src="/assignment5-webapplication/js/jquery-1.11.1.min.js"></script>
<script src="/assignment5-webapplication/js/load_header.js"></script>
<script src="/assignment5-webapplication/js/continentCenterCoords.js"></script>
<script type="text/javascript"
	src="https://maps.googleapis.com/maps/api/js?key=AIzaSyA5VLYkZvLXln72Q2FaNEj6O3H2F0yZsVY">
</script>
<title>Search Results</title>
</head>
<body>
	<div id="header"></div>
	<h2 class="text-center">Search Results</h2>
	<%
		DatabaseMethods db = new DatabaseMethods();
		HashSet<Report> resultSet = db.searchReports(request
				.getParameter("search_query"));
		if (!resultSet.isEmpty()) {
	%>
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
		</div><br />
			<div id="map-canvas"></div><br /><br />
					<script type="text/javascript"> /* This script uses Java to display the map. */
		var map;
	      function initialize() {
	    	  var mapOptions = {
	    	  	zoom: 3,
	    		center: new google.maps.LatLng(
	    				ContinentCoords.choose.lat,
	    				ContinentCoords.choose.lon),
	    		mapTypeId: google.maps.MapTypeId.TERRAIN
	    	  };
	
	        map = new google.maps.Map(document.getElementById("map-canvas"),
	            mapOptions);
	        <%for (Report r : resultSet) {
	        	if (r.getLatitude() != null && r.getLongitude() != null) { %>
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
	        }%>
		}
		google.maps.event.addDomListener(window, 'load', initialize);
		</script> <!-- End script with Java -->
	<%
		} else {
	%>
			<p class="text-center">Whoops, there are no results. Please try
				again.</p>
	<%
		}
	%>
</body>
</html>