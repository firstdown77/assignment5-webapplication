<%@page import="servlets.DatabaseMethods"%>
<%@ page import="java.util.*"%>
<%@ page import="dbObjects.*"%>
<%@ page language="java" contentType="text/html; charset=US-ASCII"
	pageEncoding="US-ASCII"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII" />
<meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
<link rel="stylesheet" href="css/bootstrap.min.css" type="text/css" />
<link rel="stylesheet" href="css/search_results.css" type="text/css" />
<link rel="stylesheet" href="css/header.css" type="text/css" />
<link rel="stylesheet" href="css/googlemapcode.css" type="text/css" />
<script src="js/jquery-1.11.1.min.js"></script>
<script src="js/load_header.js"></script>
<script src="js/continentCenterCoords.js"></script>
<script type="text/javascript"
	src="https://maps.googleapis.com/maps/api/js?key=AIzaSyA5VLYkZvLXln72Q2FaNEj6O3H2F0yZsVY">
</script>
<title>Advanced Search Results</title>
</head>
<body>
	<div id="header"></div>
	<h2 class="text-center">Advanced Search Results</h2>
	<%
		DatabaseMethods db = new DatabaseMethods();
		String query = request.getParameter("search_query");
		String[] checked = request.getParameterValues("advancedoption");
		HashSet<String> possibleValues = new HashSet<String>(Arrays.asList(
				"title", "address", "filename", "textcontent", "username"));
		for (String s : checked) {
			if (!possibleValues.contains(s)) {
				throw new Exception("There is an illegal value in the checked list.");
			}
		}
		HashSet<Report> resultSet = db.advancedSearchReports(query, checked);
		if (!resultSet.isEmpty()) {
	%>
			<table border="1" class="normalborder">
				<thead class="normalborder">
					<tr>
						<th class="normalborder">Title</th>
						<th class="normalborder">User</th>
						<th class="normalborder">Address</th>
						<th class="normalborder">Text Content</th>
						<th class="normalborder">File Content</th>
		
					</tr>
				</thead>
				<%
					for (Report r : resultSet) {
				%>
				<tr class="normalborder">
					<td class="normalborder"><a
						href="view_report?report_id=<%=r.get_id()%>"><%=(r.getTitle() == null ? "No Title Provided" : r.getTitle())%></a></td>
					<td class="normalborder"><%=(r.getUser() == null ? "Username Unavailable" : r.getUser())%></td>
					<td class="normalborder"><%=(r.getAddress() == null ? "No Address Provided" : r.getAddress())%></td>
					<td class="normalborder"><%=(r.getTextcontent() == null ? "No Text Content Provided" : r.getTextcontent())%></td>
					<td class="normalborder"><%=(r.getFilename() == null ? "No Title Provided" : "<a href='view_file?filename=" + r.getFilename() + "' target='_blank'>" + r.getFilename() + "</a>") %></td>
				</tr>
				<%
					}
				%>
			</table><br />
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
				var selectedCountry = "<%= request.getParameter("continents")%>";
				$('select').val(selectedCountry);
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
	    				ContinentCoords[selectedCountry].lat,
	    				ContinentCoords[selectedCountry].lon),
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