/**
 * Method for sending a request to a servlet to create
 * a new user in the system.
 */

function createUser(username, password, firstname, lastname) {
	$.ajax({
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
		}
	});
}