package servlets;

import java.io.IOException;
import java.security.Principal;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/delete_account")
public class DeleteAccount extends HttpServlet {
	
	private static final long serialVersionUID = 4096757649950448031L;
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// Try to get the session and if getting the session fails,
		// do not create a new session (the purpose of the 'false').
		HttpSession session = req.getSession(false);
		session.invalidate();
		DatabaseMethods db = new DatabaseMethods();
		Principal p = req.getUserPrincipal();
		String username = p.getName();
		db.deleteUser(username);
		return;
	}	
}
