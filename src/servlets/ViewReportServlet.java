package servlets;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.security.Principal;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import dbObjects.Report;

@WebServlet("/members/view_report")
@MultipartConfig
public class ViewReportServlet extends HttpServlet {

	private static final long serialVersionUID = 8522932174797318262L;

	//This method is used upon existing report selection.  
	//It is also called by the doPost() method below.
	@Override
	public void doGet(HttpServletRequest request,
			HttpServletResponse response)
					throws ServletException, IOException {
		
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		long reportID;
		String updateMessage = "";
		if (request.getAttribute("updateResult") != null) {
			Boolean updateResult = (Boolean) request.getAttribute("updateResult");
			if (updateResult == true) {
				updateMessage = "Success.  You have updated the report.";
			}
			else if (updateResult == false) {
				updateMessage = "Whoops, the result was not updated successfully.";
			}
		}
		if (request.getParameter("report_id") == null) {
			reportID = (Long) request.getAttribute("report_id");
		}
		else {
			reportID = Long.parseLong(request.getParameter("report_id"));
		}
		DatabaseMethods db = new DatabaseMethods();
		Report r = db.getReportById(reportID);
		
	    out.println
	      (
	    "<?xml version='1.0' encoding='US-ASCII' ?>\n" +
	    "<html xmlns='http://www.w3.org/1999/xhtml'>\n" +
	       "<head>\n" +
	       "<link rel='stylesheet' href='/assignment5-webapplication/css/bootstrap.min.css' type='text/css'/>\n" +
	       "<link rel='stylesheet' href='/assignment5-webapplication/css/header.css' type='text/css'/>\n" + 
	       "<link rel='stylesheet' href='/assignment5-webapplication/css/view_report.css' type='text/css'/>\n" + 
	       "<link rel='stylesheet' href='/assignment5-webapplication/css/googlemapcode.css' type='text/css' />" + 
	       "<script src='/assignment5-webapplication/js/jquery-1.11.1.min.js'></script>\n" +
	       "<script src='/assignment5-webapplication/js/load_header.js'></script>\n" + 
	       "<script type='text/javascript' src='https://maps.googleapis.com/maps/api/js?" + 
	       "key=AIzaSyA5VLYkZvLXln72Q2FaNEj6O3H2F0yZsVY'></script>\n" +
	       "<meta http-equiv='Content-Type' content='text/html; charset=US-ASCII' />\n" + 
	       "<meta name='viewport' content='initial-scale=1.0, user-scalable=no' />" +
	       "<title>View Report</title></head>\n" +
	       "<body bgcolor=\"#fdf5e6\">\n" +
	       "<div id='header'></div>\n" +
	       "<div id='content-body-wrapper' class='content-body-wrapper'>\n" +
	       "<div id='content-body' class='content-body'>\n" +
	   		"<div id='sidebar'></div>\n" +
	   		"<div id='content'>\n" +
	       "<h1 class='text-center'>" + (r.getTitle() == null ? "Untitled" : r.getTitle()) +"</h1>\n" +
	       "<p class='text-center' id='status_message' style='display:none'>" + updateMessage + "</p>\n" + 
	       "<script>$('#status_message').fadeIn(300).delay(1500).fadeTo(300, 0);</script>\n" +
	       "<div style='width: 715px; margin:0 auto;';>" + 
	       "<p class='text-center report_details'>" + (r.getTextcontent() == null ? "" : r.getTextcontent()) + "</p>\n\n" +
	       "<p class='text-center report_details'>" + (r.getFilename() == null ? "" : "<a href='view_file?id=" + r.get_id() + 
	    		   "' target='_blank'>" + r.getFilename() + "</a></p>") +
	       "<p class='text-center report_details'>" + (r.getAddress() == null ? "No Address" : r.getAddress()) + "</p>\n"
	       		+ "</div>\n"
	       		+ "<script type='text/javascript'>\n"
	       		+ "var map;\n"
	       		+ "function initialize() {\n"
	       		+ "var mapOptions = {\n"
	       		+ "zoom: 6,\n"
	       		+ "center: new google.maps.LatLng(" + r.getLatitude() + ", " + r.getLongitude()+ "),\n"
	       		+ "mapTypeId: google.maps.MapTypeId.TERRAIN\n"
	       		+ "};\n"
	       		+ "map = new google.maps.Map(document.getElementById('map-canvas'),mapOptions);\n"
	       		+ "var CONVERTTOKM = 1000;\n"
	       		+ "var currCenter = new google.maps.LatLng(" + r.getLatitude() + ", " + r.getLongitude() + ");\n"
	       		+ "var currRadius = " + r.getRadius() + " * CONVERTTOKM;\n"
	       		+ "var currTitle = '" + r.getTitle() + "';\n"
	       		+ "var populationOptions = {\n"
	       		+ "strokeColor: '#FF0000',\n"
	       		+ "strokeOpacity: 0.8,\n"
	       		+ "strokeWeight: 2,\n"
	       		+ "fillColor: '#FF0000',\n"
	       		+ "fillOpacity: 0.35,\n"
	       		+ "map: map,\n"
	       		+ "center: currCenter,\n"
	       		+ "radius: currRadius\n"
	       		+ "};\n"
	       		+ "cityCircle = new google.maps.Circle(populationOptions);\n"
	       		+ "var marker = new google.maps.Marker({\n"
	       		+ "position: currCenter,\n"
	       		+ "map: map,\n"
	       		+ "title: currTitle\n"
	       		+ "});\n"
	       		+ "google.maps.event.addListener(marker, 'click', function() {\n"
	       		+ "window.location.href = 'view_report?report_id=" + r.get_id() + "';\n"
	       		+ "});\n"
	       		+ "}\n"
	       		+ "google.maps.event.addDomListener(window, 'load', initialize);\n"
	       		+ "</script>\n"
	       		+ "<div id='map-canvas'></div>\n"
	 	        + "<p class='text-center report_details'>Created By: " + (r.getUser() == null ? "Username Unavailable" : r.getUser()) 
	 	        + "</p>\n");
	    Principal p = request.getUserPrincipal();
	    if ( (r.getUser().equals(p.getName())) || (p.getName().equals(UserVariables.adminUsername)) ) 
	    {
	    	out.println("<p class='text-center'><a href='/assignment5-webapplication/members/update_report.jsp?update_report_id=" + r.get_id() + "'>Update Report</a> | <a href='/assignment5-webapplication?username=&delete_report_id=" + r.get_id() + "'>Delete Report</a>\n"
		        +	"</p>");
	    }
	    	out.println("</div>\n"
	    			+"</div>\n"
	    			+"</div>\n"
	       		+ "</body></html>");
	            // Add the circle for this city to the map.
	}
	
	//This method is used upon report creation.
	@Override
	public void doPost(HttpServletRequest request,
			HttpServletResponse response)
					throws ServletException, IOException {
		
		response.setContentType("text/html");
//		PrintWriter out = response.getWriter();
		String title;
		Double latitude;
		Double longitude;
		Double radius;
		String address;
		String textcontent;
		Long report_id = null;
		if (request.getParameter("report_id") != null) {
			report_id = Long.parseLong(request.getParameter("report_id"));
		}
		try {
			latitude = Double.parseDouble(request.getParameter("latitude"));
		}
		catch (NumberFormatException e) {
			latitude = null;
		}
		try {
			longitude = Double.parseDouble(request.getParameter("longitude"));
		}
		catch (NumberFormatException e) {
			longitude = null;
		}
		try {
			radius = Double.parseDouble(request.getParameter("radius"));
		}
		catch (NumberFormatException e) {
			radius = null;
		}
		if (!request.getParameter("address").equals("Address")) {
			address = request.getParameter("address");
		}
		else {
			address = null;
		}
		if (!request.getParameter("textcontent").equals("Write report content...")) {
			textcontent = request.getParameter("textcontent");
		}
		else {
			textcontent = null;
		}
		if (!request.getParameter("title").equals("Title")) {
			title = request.getParameter("title");
		}
		else {
			title = null;
		}
		Principal p = request.getUserPrincipal();
		String username = p.getName();
		Part filePart = request.getPart("content");
		InputStream fileContent = filePart.getInputStream();
		String fileName = getFilename(filePart);
		Report r = new Report(report_id, username, address, latitude, longitude,
				title, radius, textcontent, fileContent, fileName);
		String action = request.getParameter("action");
		DatabaseMethods db = new DatabaseMethods();
		if (action.equals("create")) {
			report_id = db.insertReport(r);
		}
		else if (action.equals("update")) {
			boolean updateResult = db.updateReport(r);
			request.setAttribute("updateResult", updateResult);
		}
		request.setAttribute("report_id", report_id);
		doGet(request, response);
	}

	private String getFilename(Part part) {
		for (String cd : part.getHeader("content-disposition").split(";")) {
			if (cd.trim().startsWith("filename")) {
				String filename = cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
				String toReturn = filename.substring(filename.lastIndexOf('/') + 1).substring(filename.lastIndexOf('\\') + 1); // MSIE fix.
				return toReturn;
			}
		}
		return null;
	}
}
