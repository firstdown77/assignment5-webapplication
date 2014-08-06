package testPackage;

import java.io.ByteArrayOutputStream;
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

import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;

import dbObjects.Report;

@WebServlet("/view_report")
@MultipartConfig
public class ViewReportServlet extends HttpServlet {

	private static final long serialVersionUID = 8522932174797318262L;

	//This method is used upon existing report selection.
	@Override
	public void doGet(HttpServletRequest request,
			HttpServletResponse response)
					throws ServletException, IOException {
		
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		long reportID = Long.parseLong(request.getParameter("report_id"));
		DatabaseMethods db = new DatabaseMethods();
		Report r = db.getReportById(reportID);
		
	    out.println
	      ("<!DOCTYPE html>\n" +
	       "<html>\n" +
	       "<head><title>View Report</title></head>\n" +
	       "<link rel='stylesheet' href='css/bootstrap.min.css' type='text/css'/>" +
	       "<link rel='stylesheet' href='css/header.css' type='text/css'/>" + 
	       "<script src='js/jquery-1.11.1.min.js'></script>" +
	       "<script src='js/load_header.js'></script>" +
	       "<meta http-equiv='Content-Type' content='text/html; charset=US-ASCII' />" +
	       "<title>View Report</title>" + 
	       "<body bgcolor=\"#fdf5e6\">\n" +
	       "<div id='header'></div>" +
	       "<h1 class='text-center'>View Report</h1>\n" +
	       "<p class='text-center'>" + r.getTitle() + "</p>\n\n" +
	       "<p class='text-center'>" + r.getLatitude() + "</p>\n" +
	       "<p class='text-center'>" + r.getLongitude() + "</p>\n" +
	       "<p class='text-center'>" + r.getRadius() + "</p>\n" +
	       "<p class='text-center'>" + r.getUser() + "</p>\n" +
	       "<p class='text-center'>" + "<a href='view_file?filename=" + r.getFilename() + "' target='_blank'>" + r.getFilename() + "</a>" + "</p>\n" +
	       "</body></html>");
	}
	
	//This method is used upon report creation.
	@Override
	public void doPost(HttpServletRequest request,
			HttpServletResponse response)
					throws ServletException, IOException {
		
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		String title = request.getParameter("title");
		double latitude;
		double longitude;
		double radius;
		try {
			latitude = Double.parseDouble(request.getParameter("latitude"));
		}
		catch (NumberFormatException e) {
			latitude = 0;
		}
		try {
			longitude = Double.parseDouble(request.getParameter("longitude"));
		}
		catch (NumberFormatException e) {
			longitude = 0;
		}
		try {
			radius = Double.parseDouble(request.getParameter("radius"));
		}
		catch (NumberFormatException e) {
			radius = 0;
		}
		String username = UserVariables.username;
		Part filePart = request.getPart("content");
		InputStream fileContent = filePart.getInputStream();
		String fileName = getFilename(filePart);
		Report r = new Report(null, username, latitude, longitude,
				title, radius, fileContent, fileName);
		DatabaseMethods db = new DatabaseMethods();
		db.insertReport(r);
		
	    out.println
	      ("<!DOCTYPE html>\n" +
	       "<html>\n" +
	       "<head><title>View Report</title></head>\n" +
	       "<link rel='stylesheet' href='css/bootstrap.min.css' type='text/css'/>" +
	       "<script src='js/jquery-1.11.1.min.js'></script>" +
	       "<script src='js/load_header.js'></script>" +
	       "<meta http-equiv='Content-Type' content='text/html; charset=US-ASCII' />" +
	       "<title>View Report</title>" + 
	       "<body bgcolor=\"#fdf5e6\">\n" +
	       "<div id='header'></div>" +
	       "<h1 class='text-center'>View Report</h1>\n" +
	       "<p class='text-center'>" + title + "</p>\n" +
	       "<p class='text-center'>" + latitude + "</p>\n" +
	       "<p class='text-center'>" + longitude + "</p>\n" +
	       "<p class='text-center'>" + radius + "</p>\n" +
	       "<p class='text-center'>" + username + "</p>\n" +
	       "<p class='text-center'>" + "<a href='view_file?filename=" + fileName + "' target='_blank'>" + fileName + "</a>" + "</p>\n" +
	       "</body></html>");
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
