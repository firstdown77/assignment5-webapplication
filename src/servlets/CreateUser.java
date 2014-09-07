package servlets;

import java.io.IOException;
import java.sql.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dbObjects.User;

@WebServlet("/create_user")
public class CreateUser extends HttpServlet {
	
	private static final long serialVersionUID = 4096757649950448031L;
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// Try to get the session and if getting the session fails,
		// do not create a new session (the purpose of the 'false').
		String firstname = req.getParameter("firstname");
		String lastname = req.getParameter("lastname");
		String username = req.getParameter("username");
		String password = req.getParameter("password");
		if (lastname.equals("Last Name") || lastname.equals("")) {
			lastname = null;
			resp.setStatus(509);
			return;
		}
		if (firstname.equals("First Name") || username.equals("")) {
			firstname = null;
			resp.setStatus(509);
			return;
		}
		if (username.equals("Username") || username.equals("")) {
			username = null;
			resp.setStatus(509);
			return;
		}
		if (password.equals("Password") || password.equals("")) {
			password = null;
			resp.setStatus(509);
			return;
		}
		DatabaseMethods db = new DatabaseMethods();
		User u = new User(null, username, password, firstname, lastname, new Date(System.currentTimeMillis()));
		boolean result = db.createUserWrapper(u);
		if (result == false) {
			resp.setStatus(500);
		}
		return;
	}	
}
