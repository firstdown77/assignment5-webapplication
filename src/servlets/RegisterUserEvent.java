package servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dbObjects.User;

public class RegisterUserEvent extends HttpServlet {

	private static final long serialVersionUID = 3478376736274418268L;

	@Override
	public void doGet(HttpServletRequest req,
			HttpServletResponse response)
					throws ServletException, IOException {
		// Try to get the session and if getting the session fails,
		// do not create a new session (the purpose of the 'false').
		DatabaseMethods db = new DatabaseMethods();
		String id = req.getParameter("event_id");
		String username = req.getParameter("username");
		String action = req.getParameter("action");
		try
		{
			if (username != null)
			{
				User u = db.getUserByUsername(username);
				if ((action != null)&&(action.equals("unregister")))
				{
					if (u != null)
					{
						db.unregisterFromEvent(Integer.parseInt(id), u.getUser_id().intValue());
					}else{
						response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					}
				}
				else if ((id != null)&&(id.length() > 0))
				{
					if (u != null)
					{
						if (db.addUserToEvent(Integer.parseInt(id), u.getUser_id().intValue()) < 1)
							response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					}else{
						response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					}
				}
				response.setStatus(HttpServletResponse.SC_OK);
			}else{
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			}
			
		}
		catch (Exception exc)
		{
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}	
}

