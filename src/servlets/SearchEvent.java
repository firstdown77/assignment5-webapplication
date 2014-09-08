package servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dbObjects.Event;

public class SearchEvent extends HttpServlet {

	private static final long serialVersionUID = 2764781108153808574L;

	@Override
	public void doGet(HttpServletRequest req,
			HttpServletResponse response)
					throws ServletException, IOException {
		// Try to get the session and if getting the session fails,
		// do not create a new session (the purpose of the 'false').
		DatabaseMethods db = new DatabaseMethods();
		String longitude = req.getParameter("longitude");
		String latitude = req.getParameter("latitude");
		try
		{
			Event e = db.searchNearestEvent(Double.parseDouble(longitude), 
					Double.parseDouble(latitude));
			if (e != null)
			{
				response.sendRedirect("view_event.jsp?event_id="+e.get_id());
			}else{
				response.sendRedirect("view_event.jsp?event_id=-1");
			}
		}
		catch (Exception exc)
		{
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}	
}


