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