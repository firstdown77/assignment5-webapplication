/**
 * Method for sending a request to a servlet to create
 * a new user in the system.
 */

function createUser(event, username, password, firstname, lastname) {
	var result = $.ajax({
		url: "/assignment5-webapplication/create_user",
		type: "post",
	    async: false,
        data: {
            username: username,
            password: password,
            firstname: firstname,
            lastname: lastname
	    },
	    success: function(data) {
		},
		error: function(data) {
			event.preventDefault();
			if (data.status === 509) {
				$("#status_message").fadeTo(0, 1);
				$("#status_message").html("Whoops, registration didn't work.  Make sure all the fields are completed.");
			}
			else if (data.status === 500) {
				$("#status_message").fadeTo(0, 1);
				$("#status_message").html("Whoops, registration didn't work. Try a different username.");
			}
			$('#status_message').fadeIn(300).delay(1500).fadeTo(300, 0);
			
		}
	});
	return result.status != 500;
}