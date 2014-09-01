$(function(){
	  $("#header").load("/assignment5-webapplication/members/header.html");
	  $("#sidebar").load("/assignment5-webapplication/members/sidebar.jsp");

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

function deleteAccount() {
	if (confirm('Are you sure you want to delete your account?')) {
		$.ajax({
			url: "/assignment5-webapplication/delete_account",
			type: "post",
			async: false,
			success: function(data) {
			}
		});
	}
}