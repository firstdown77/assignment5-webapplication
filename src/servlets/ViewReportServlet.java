package servlets;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import dbObjects.Report;

@WebServlet("/view_report")
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
	      ("<!DOCTYPE html>\n" +
	       "<html>\n" +
	       "<head>\n" +
	       "<link rel='stylesheet' href='css/bootstrap.min.css' type='text/css'/>\n" +
	       "<link rel='stylesheet' href='css/header.css' type='text/css'/>\n" + 
	       "<script src='js/jquery-1.11.1.min.js'></script>\n" +
	       "<script src='js/load_header.js'></script>\n" +
	       "<meta http-equiv='Content-Type' content='text/html; charset=US-ASCII' />\n" +
	       "<title>View Report</title></head>\n" +
	       "<body bgcolor=\"#fdf5e6\">\n" +
	       "<div id='header'></div>" +
	       "<h1 class='text-center'>View Report</h1>\n" +
	       "<p class='text-center' id='status_message' style='display:none'>" + updateMessage + "</p>\n" + 
	       "<script>$('#status_message').fadeIn(300).delay(1500).fadeTo(300, 0);</script>\n" +
	       "<p class='text-center'>" + (r.getTitle() == null ? "No Title Provided" : r.getTitle()) + "</p>\n\n" +
	       "<p class='text-center'>" + (r.getTextcontent() == null ? "No Text Content Provided" : r.getTextcontent()) + "</p>\n\n" +
	       "<p class='text-center'>" + (r.getAddress() == null ? "No Address Provided" : r.getAddress()) + "</p>\n" +
	       "<p class='text-center'>" + (r.getLatitude() == null ? "No Latitude Provided" : r.getLatitude()) + "</p>\n" +
	       "<p class='text-center'>" + (r.getLongitude() == null ? "No Longitude Provided" : r.getLongitude()) + "</p>\n" +
	       "<p class='text-center'>" + (r.getRadius() == null ? "No Radius Provided" : r.getRadius()) + "</p>\n" +
	       "<p class='text-center'>" + (r.getUser() == null ? "Username Unavailable" : r.getUser()) + "</p>\n" +
	       "<p class='text-center'>" + (r.getFilename() == null ? "No File Content Provided" : "<a href='view_file?filename=" + 
	       r.getFilename() + "' target='_blank'>" + r.getFilename() + "</a>") +
	       "</p>\n<p class='text-center'><a href='update_report.jsp?update_report_id=" + r.get_id() + "'>Update Report</a> | <a href='my_account.jsp?username=&delete_report_id=" + r.get_id() + "'>Delete Report</a>\n"
	       		+ "</p>\n</body></html>");
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
		String username = UserVariables.username;
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
