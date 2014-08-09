/*
 * Function to geocode.
 */
function doGeoCode(address, lat, lon) {
	//If no latitude or longitude were provided and an address was provided,
	//then normal geocode, i.e. convert address to geo-coordinates.
	if ((($('#latitude').val() === "Latitude") || ($('#longitude').val() === "Longitude"))
			&& ($('#address').val() !== "Address")) {
		var result = $
		.ajax({
			url : "https://maps.googleapis.com/maps/api/geocode/json?address="
				+ address
				+ "&key=AIzaSyBPihpCUn2xSaRqNv9KM1_MEAf-iS6sJ-Q",
				type : "get",
				async : false,
				data : {}
		}).responseText;
		var lat = JSON.parse(result).results[0].geometry.location.lat;
		var lng = JSON.parse(result).results[0].geometry.location.lng;
		$('#latitude').val(lat);
		$('#longitude').val(lng);
	}
	//If no address was provided and a latitude and longitude were provided,
	//then reverse geocode, i.e. convert geo-coordinates to address.
	else if (($('#address').val() === "Address")
			&& ($('#latitude').val() !== "Latitude")
			&& ($('#longitude').val() !== "Longitude")) {
		var result = $
		.ajax({
			url : "https://maps.googleapis.com/maps/api/geocode/json?latlng=40.714224,-73.961452&key=AIzaSyBPihpCUn2xSaRqNv9KM1_MEAf-iS6sJ-Q",
			type : "get",
			async : false,
			data : {}
		}).responseText;
		var formatted_address = JSON.parse(result).results[0].formatted_address;
		$('#address').val(formatted_address);
	}
}