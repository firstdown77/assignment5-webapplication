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
		DatabaseMethods db = new DatabaseMethods();
		User u = new User(null, username, password, firstname, lastname, new Date(System.currentTimeMillis()));
		db.createUserWrapper(u);
		return;
	}	
}
