package testPackage;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/view_file")
public class ViewFileServlet extends HttpServlet {
	private static final long serialVersionUID = -2432280626493956191L;

	private String getMimeType(String fileName) {
		String toReturn = fileName.substring(fileName.lastIndexOf(".") + 1);
		return toReturn;
	}

	@Override
	public void doGet(HttpServletRequest request,
			HttpServletResponse response)
					throws ServletException, IOException {
		String fileName = request.getParameter("filename");
		String mimeType = getMimeType(fileName);
		response.setContentType(mimeType);
		OutputStream out = response.getOutputStream();
		DatabaseMethods db = new DatabaseMethods();
		InputStream fileContent = db.getRecentReport().getContent();
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
