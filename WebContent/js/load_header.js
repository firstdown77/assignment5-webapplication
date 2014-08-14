$(function(){
	  $("#header").load("/assignment5-webapplication/members/header.html"); 
});

function sendLogout() {
	$.ajax({
		url: "/assignment5-webapplication/logout",
		type: "post",
	    async: false,
	    success: function(data) {
		}
	});
}