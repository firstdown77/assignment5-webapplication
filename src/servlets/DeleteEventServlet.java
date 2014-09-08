package servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DeleteEventServlet extends HttpServlet {
	
	private static final long serialVersionUID = 4096757649950448031L;
	
	@Override
	public void doGet(HttpServletRequest req,
			HttpServletResponse response)
					throws ServletException, IOException {
		// Try to get the session and if getting the session fails,
		// do not create a new session (the purpose of the 'false').
		DatabaseMethods db = new DatabaseMethods();
		String id = req.getParameter("event_id");
		try
		{
			if ((id != null)&&(id.length() > 0))
			{
				if (!db.deleteEvent(new Long(id)))
					response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			}
		}
		catch (Exception exc)
		{
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}	
}
