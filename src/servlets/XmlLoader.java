package servlets;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.stream.StreamResult;

import java.io.FileOutputStream;


import dbObjects.Event;
import dbObjects.Report;

public class XmlLoader extends HttpServlet {

	private static final long serialVersionUID = 751557336877762071L;

	private void addReport(StringBuilder sb, Report r)
	{
		sb.append("<report id='"+r.get_id()+"' username='"+r.getUser()+"'>");
		sb.append("<address>" + r.getAddress() + "</address>");
		sb.append("<longitude>" + r.getLongitude() + "</longitude>");
		sb.append("<latitude>" + r.getLatitude() + "</latitude>");
		sb.append("<radius>" + r.getRadius() + "</radius>");
		sb.append("<title>" + r.getTitle() + "</title>");
		sb.append("<textcontent>" + r.getTextcontent() + "</textcontent>");
		sb.append("</report>");
	}
	
	private void addEvent(StringBuilder sb, Event e)
	{
		sb.append("<event id='"+e.get_id()+"' username='"+e.getUsername()+"' capacity='"+e.getCapacity()+"'>");
		sb.append("<longitude>" + e.getLongitude() + "</longitude>");
		sb.append("<latitude>" + e.getLatitude() + "</latitude>");
		sb.append("<evacuationmeans>" + e.getEvacuationMeans() + "</evacuationmeans>");
		sb.append("</event>");
	}
	
	@Override
	public void doGet(HttpServletRequest req,
			HttpServletResponse response)
					throws ServletException, IOException {
		
		DatabaseMethods db = new DatabaseMethods();
		String longitude = req.getParameter("longitude");
		String latitude = req.getParameter("latitude");
		try
		{
			ArrayList<Report> reports = db.getAllReports();
			ArrayList<Event> events = db.getUpcomingEvents();
			StringBuilder sb = new StringBuilder();
			sb.append("<?xml version='1.0' encoding='UTF-8'?>\n");
			sb.append("<disasterevacuation>\n");
			sb.append("<reports>\n");
			for (Report r : reports)
			{
				addReport(sb, r);
			}
			sb.append("</reports>\n");
			sb.append("<events>\n");
			for (Event e : events)
			{
				addEvent(sb, e);
			}
			sb.append("</events>\n");
			sb.append("</disasterevacuation>\n");
			
			
			// Use the static TransformerFactory.newInstance() method to instantiate 
			  // a TransformerFactory. The javax.xml.transform.TransformerFactory 
			  // system property setting determines the actual class to instantiate --
			  // org.apache.xalan.transformer.TransformerImpl.
				TransformerFactory tFactory = TransformerFactory.newInstance();
				
				StringReader xml_sr = new StringReader(sb.toString());
		        StreamSource xml_ss = new StreamSource(xml_sr);
		        StringWriter out_sw = new StringWriter();
		        StreamResult out_sr = new StreamResult(out_sw);
		        
				// Use the TransformerFactory to instantiate a Transformer that will work with  
				// the stylesheet you specify. This method call also processes the stylesheet
		        // into a compiled Templates object.
		        String type = req.getParameter("type");
		        if (type == null)
		        	type = "reports";
		        String relativeWebPath;
		        if (type.equals("reports"))
		        	relativeWebPath = "kml.xsl";
		        else
		        	relativeWebPath = "kmlevents.xsl";
		        
		        String absoluteDiskPath = getServletContext().getRealPath(relativeWebPath);
		        Transformer transformer = tFactory.newTransformer(new StreamSource(absoluteDiskPath));

				// Use the Transformer to apply the associated Templates object to an XML document
				// (foo.xml) and write the output to a file (foo.out).
				transformer.transform(xml_ss, out_sr);
				req.setAttribute("xml", sb.toString());
				req.setAttribute("kml", out_sw.toString());
				RequestDispatcher dispatcher = req.getRequestDispatcher("/members/kml_view.jsp");
				dispatcher.forward(req, response);
			
		}
		catch (Exception exc)
		{
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}	
}



