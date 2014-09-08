<?xml version="1.0" encoding="US-ASCII" ?>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<link rel="stylesheet" href="/assignment5-webapplication/css/bootstrap.min.css" type="text/css" />
<link rel="stylesheet" href="/assignment5-webapplication/css/view_report.css" type="text/css" />
<link rel="stylesheet" href="/assignment5-webapplication/css/header.css" type="text/css" />
<link rel="stylesheet" href="/assignment5-webapplication/css/googlemapcode.css" type="text/css" />
<link rel="stylesheet" href="/assignment5-webapplication/js/jquery-ui/jquery-ui.css"/>
<script src="/assignment5-webapplication/js/jquery-1.11.1.min.js"></script>
<script src="/assignment5-webapplication/js/jquery-ui/jquery-ui.js"></script>
<script src="/assignment5-webapplication/js/load_header.js"></script>
<script src="/assignment5-webapplication/js/continentCenterCoords.js"></script>
<script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?
key=AIzaSyA5VLYkZvLXln72Q2FaNEj6O3H2F0yZsVY"></script>
<script src="/assignment5-webapplication/js/geocoder.js"></script>
<script src="/assignment5-webapplication/js/jquery.validate.min.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII" />
<meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
<title>Create Evacuation Event</title>

<script>
var map;
var markersArray = [];

function initialize() {
	  var mapOptions = {
	  	zoom: 3,
		center: new google.maps.LatLng(37.09024, -95.712891),
		mapTypeId: google.maps.MapTypeId.TERRAIN
	  };

  map = new google.maps.Map(document.getElementById("map-canvas"),
      mapOptions);
  center = map.getCenter();
  placeMarker(center);
  $("#latitude").val(center.lat());
  $("#longitude").val(center.lng());
  
//add a click event handler to the map object
  google.maps.event.addListener(map, "click", function(event)
  {
      // place a marker
      placeMarker(event.latLng);

      // display the lat/lng in your form's lat/lng fields
      $("#latitude").val(event.latLng.lat());
      $("#longitude").val(event.latLng.lng());
  });
  
}

function placeMarker(location) {
    // first remove all markers if there are any
    deleteOverlays();

    var marker = new google.maps.Marker({
        position: location, 
        map: map
    });

    // add marker in markers array
    markersArray.push(marker);

    //map.setCenter(location);
}

// Deletes all markers in the array by removing references to them
function deleteOverlays() {
    if (markersArray) {
        for (i in markersArray) {
            markersArray[i].setMap(null);
        }
    markersArray.length = 0;
    }
}

google.maps.event.addDomListener(window, 'load', initialize);

function setMapCoords(data, textStatus)
{
	if (data.results.length > 0)
	{
		var lat = data.results[0].geometry.location.lat;
		var lng = data.results[0].geometry.location.lng;
		$('#latitude').val(lat);
		$('#longitude').val(lng);
		var center = new google.maps.LatLng(lat, lng);
		map.setCenter(center);
		placeMarker(center)
		if (map.getZoom() < 12)
			map.setZoom(12);
	}
}
</script>

</head>
<body>
	<div id="header"></div>
	<div id="content-body-wrapper" class="content-body-wrapper">
    <div id="content-body" class="content-body">
	<div id="sidebar" class="sidebar"></div>
	<div id="content" class="content">
	<h2 class="text-center">Search Nearest Evacuation Event</h2>
	
<form id="searchEvent" name="searchEvent" action="searchevent" method="get"
		class="text-center">
				<input type="hidden" name="latitude" id="latitude"/>
				<input type="hidden" name="longitude" id="longitude"/>
		 Your Location:
		<br />
		<div class="text-center">
			Address: <input type="text" name="address" id="address" onkeydown="if (event.keyCode == 13) {$('#btnsearch').click();return false;}"/> <button id="btnsearch" type="button" onclick='translateAddress($("#address").val(), setMapCoords )'>Search</button> 
			
		</div>
		<br />
	<div id="map-canvas"></div>
	<br/>
		<input type="submit"
			value="Search" />
	</form>
	</div>
	</div>
	</div>
</body>
</html>