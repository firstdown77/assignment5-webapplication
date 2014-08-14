package servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/validate_login")
public class UserValidationServlet extends HttpServlet {

	private static final long serialVersionUID = -5096885812312001683L;
	
	@Override
	public void doPost(HttpServletRequest request,
			HttpServletResponse response)
					throws ServletException, IOException {
//		DatabaseMethods db = new DatabaseMethods();
//		db.verifyPassword(request.getParameter("username"), request.getParameter("password"));
	}

}
