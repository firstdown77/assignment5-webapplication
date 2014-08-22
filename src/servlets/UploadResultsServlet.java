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

@WebServlet("/members/upload_results")
@MultipartConfig
public class UploadResultsServlet extends HttpServlet {

	private static final long serialVersionUID = -3850098455535039605L;
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
//		PrintWriter out = resp.getWriter();
		Part filePart = req.getPart("content");
		InputStream fileContent = filePart.getInputStream();
		DatabaseMethods db = new DatabaseMethods();
		int recordsImpacted = db.uploadInitialData(fileContent);
		String updateMessage;
		if (recordsImpacted > 0) {
			updateMessage = "Success, " + recordsImpacted + " database entries were impacted.";
		}
		else {
			//Note: this is not good error handling.
			updateMessage = "Whoops, your data file was not uploaded successfully.";
		}
		resp.sendRedirect("/assignment5-webapplication/?upload_file=" + updateMessage);
//	    out.println
//	      ("<!DOCTYPE html>\n" +
//	       "<html>\n" +
//	       "<head>\n" +
//	       "<link rel='stylesheet' href='/assignment5-webapplication/css/bootstrap.min.css' type='text/css'/>\n" +
//	       "<link rel='stylesheet' href='/assignment5-webapplication/css/header.css' type='text/css'/>\n" + 
//	       "<script src='/assignment5-webapplication/js/jquery-1.11.1.min.js'></script>\n" +
//	       "<script src='/assignment5-webapplication/js/load_header.js'></script>\n" +
//	       "<meta http-equiv='Content-Type' content='text/html; charset=US-ASCII' />\n" +
//	       "<title>File Upload Results</title></head>\n" +
//	       "<body bgcolor=\"#fdf5e6\">\n" +
//	       "<div id='header'></div>" +
//	       "<h1 class='text-center'>File Upload Results</h1>\n" +
//	       "<p class='text-center' id='status_message' style='display:none'>" + updateMessage + "</p>\n" + 
//	       "<script>$('#status_message').fadeIn(300).delay(1500).fadeTo(300, 0);</script>\n"
//	       + "<ul><li><a href='/assignment5-webapplication'>Return to Home</a></li>\n"
//	       + "<li><a href='view_all_users.jsp'>View All Users</a></li>"
//	       + "<li><a href='view_all_reports.jsp'>View All Reports</a></li>\n"
//	       + "</body></html>");
	}
}
