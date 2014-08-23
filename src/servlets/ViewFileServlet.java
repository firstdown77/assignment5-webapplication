package servlets;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/members/view_file")
public class ViewFileServlet extends HttpServlet {
	private static final long serialVersionUID = -2432280626493956191L;

	@Override
	public void doGet(HttpServletRequest request,
			HttpServletResponse response)
					throws ServletException, IOException {
		Long report_id = Long.parseLong(request.getParameter("id"));
		DatabaseMethods db = new DatabaseMethods();
		InputStream fileContent = db.getReportById(report_id).getContent();
		InputStream fileContent2 = db.getReportById(report_id).getContent();
		String mimeType = URLConnection.guessContentTypeFromStream(new BufferedInputStream(fileContent2));
		response.setContentType(mimeType);
		OutputStream out = response.getOutputStream();
		// Copy the contents of the file to the output stream
		byte[] buf = new byte[1024];
		int count = 0;
		while ((count = fileContent.read(buf)) >= 0) {
			out.write(buf, 0, count);
		}
		fileContent.close();
		out.close();
	}

}
