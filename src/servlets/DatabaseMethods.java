package servlets;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.sql.Types;
import java.util.HashSet;
import java.util.LinkedList;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import dbObjects.Report;
import dbObjects.User;

public class DatabaseMethods {

	private Connection con;
	private String url = "jdbc:mysql://localhost:3306/";
	private String dbName = "disasterevacuationdb";
	private String driver = "com.mysql.jdbc.Driver";
	private String userName = "root";
	private String password = "";
	private boolean isOpen = false;

	public void open()
	{
		if (isOpen) return;

		try {
			Class.forName(driver).newInstance();
			con = DriverManager.getConnection(url+dbName,userName,password);
			isOpen = true; 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void close()
	{
		isOpen = false;
		try {
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


	//	Long _id;
	//	String username;
	//	String address;
	//	double latitude;
	//	double longitude;
	//	double radius;
	//	String title;
	//	String textcontent;
	//	InputStream content;
	//  String filename;
	public Long insertReport(Report r) {
		open();
		String SQL_QUERY = "INSERT INTO reports (username, address, latitude,"
				+ " longitude, radius, title, textcontent, content, filename) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement pst;
		r.setUser(StringEscapeUtils.escapeHtml4(r.getUser()));
		r.setAddress(StringEscapeUtils.escapeHtml4(r.getAddress()));
		r.setTitle(StringEscapeUtils.escapeHtml4(r.getTitle()));
		r.setTextcontent(StringEscapeUtils.escapeHtml4(r.getTextcontent()));
		r.setFilename(StringEscapeUtils.escapeHtml4(r.getFilename()));
		r.setUser(StringEscapeUtils.escapeJava(r.getUser()));
		r.setAddress(StringEscapeUtils.escapeJava(r.getAddress()));
		r.setTitle(StringEscapeUtils.escapeJava(r.getTitle()));
		r.setTextcontent(StringEscapeUtils.escapeJava(r.getTextcontent()));
		r.setFilename(StringEscapeUtils.escapeJava(r.getFilename()));
		try {
			pst = con.prepareStatement(SQL_QUERY, Statement.RETURN_GENERATED_KEYS);
			pst.setString(1, r.getUser());
			pst.setString(2, r.getAddress());
			if (r.getLatitude() == null) {
				pst.setNull(3, Types.DOUBLE);
			}
			else {
				pst.setDouble(3, r.getLatitude());
			}
			if (r.getLongitude() == null) {
				pst.setNull(4, Types.DOUBLE);
			}
			else {
				pst.setDouble(4, r.getLongitude());
			}
			if (r.getRadius() == null) {
				pst.setNull(5, Types.DOUBLE);
			}
			else {
				pst.setDouble(5, r.getRadius());
			}
			pst.setString(6, r.getTitle());
			pst.setString(7, r.getTextcontent());
			pst.setBlob(8, r.getContent());
			if (r.getFilename().equals("")) {
				pst.setString(9, null);
			}
			else {
				pst.setString(9, r.getFilename());
			}
			pst.executeUpdate();
			ResultSet keys = pst.getGeneratedKeys();
			if (keys.next()) {
				long toReturn = keys.getLong(1);
				close();
				return toReturn;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		close();
		return null;
	}

	public Report getRecentReport() {
		open();
		String SQL_QUERY= "SELECT * from reports ORDER BY report_id DESC LIMIT 1";
		Statement stmt;
		Report r = null;
		try {
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(SQL_QUERY);
			if(rs.next()) {
				r = new Report(Long.valueOf(Long.valueOf(rs.getInt("report_id"))),
						rs.getString("username"), rs.getString("address"), (Double)rs.getObject("latitude"),
						(Double)rs.getObject("longitude"), rs.getString("title"),
						(Double)rs.getObject("radius"), rs.getString("textcontent"),
						rs.getBlob("content").getBinaryStream(),
						rs.getString("filename"));
				r.setUser(StringEscapeUtils.unescapeJava(r.getUser()));
				r.setAddress(StringEscapeUtils.unescapeJava(r.getAddress()));
				r.setTitle(StringEscapeUtils.unescapeJava(r.getTitle()));
				r.setTextcontent(StringEscapeUtils.unescapeJava(r.getTextcontent()));
				r.setFilename(StringEscapeUtils.unescapeJava(r.getFilename()));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} 
		close();
		return r;
	}

	public HashSet<Report> getReportsByUser(String username) {
		open();
		String SQL_QUERY= "SELECT * from reports WHERE username='" + username + "'";
		Statement stmt;
		Report r = null;
		HashSet<Report> hsr = new HashSet<Report>();
		try {
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(SQL_QUERY);
			while(rs.next()) {
				r = new Report(Long.valueOf(Long.valueOf(rs.getInt("report_id"))),
						rs.getString("username"), rs.getString("address"), (Double)rs.getObject("latitude"),
						(Double)rs.getObject("longitude"), rs.getString("title"),
						(Double)rs.getObject("radius"), rs.getString("textcontent"),
						rs.getBlob("content").getBinaryStream(),
						rs.getString("filename"));
				r.setUser(StringEscapeUtils.unescapeJava(r.getUser()));
				r.setAddress(StringEscapeUtils.unescapeJava(r.getAddress()));
				r.setTitle(StringEscapeUtils.unescapeJava(r.getTitle()));
				r.setTextcontent(StringEscapeUtils.unescapeJava(r.getTextcontent()));
				r.setFilename(StringEscapeUtils.unescapeJava(r.getFilename()));
				hsr.add(r);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		close();
		return hsr;
	}

	public HashSet<Report> getAllReports() {
		open();
		String SQL_QUERY= "SELECT * from reports";
		Statement stmt;
		Report r = null;
		HashSet<Report> hsr = new HashSet<Report>();
		try {
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(SQL_QUERY);
			while(rs.next()) {
				r = new Report(Long.valueOf(Long.valueOf(rs.getInt("report_id"))),
						rs.getString("username"), rs.getString("address"), (Double)rs.getObject("latitude"),
						(Double)rs.getObject("longitude"), rs.getString("title"),
						(Double)rs.getObject("radius"), rs.getString("textcontent"),
						rs.getBlob("content").getBinaryStream(),
						rs.getString("filename"));
				r.setUser(StringEscapeUtils.unescapeJava(r.getUser()));
				r.setAddress(StringEscapeUtils.unescapeJava(r.getAddress()));
				r.setTitle(StringEscapeUtils.unescapeJava(r.getTitle()));
				r.setTextcontent(StringEscapeUtils.unescapeJava(r.getTextcontent()));
				r.setFilename(StringEscapeUtils.unescapeJava(r.getFilename()));
				hsr.add(r);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		close();
		return hsr;
	}

	public Report getReportById(Long id) {
		int reportID = id.intValue();
		open();
		String SQL_QUERY= "SELECT * from reports WHERE report_id='" + reportID + "'";
		Statement stmt;
		Report r = null;
		try {
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(SQL_QUERY);
			if(rs.next()) {
				r = new Report(Long.valueOf(Long.valueOf(rs.getInt("report_id"))),
						rs.getString("username"), rs.getString("address"), (Double)rs.getObject("latitude"),
						(Double)rs.getObject("longitude"), rs.getString("title"),
						(Double)rs.getObject("radius"), rs.getString("textcontent"),
						rs.getBlob("content").getBinaryStream(),
						rs.getString("filename"));
				r.setUser(StringEscapeUtils.unescapeJava(r.getUser()));
				r.setAddress(StringEscapeUtils.unescapeJava(r.getAddress()));
				r.setTitle(StringEscapeUtils.unescapeJava(r.getTitle()));
				r.setTextcontent(StringEscapeUtils.unescapeJava(r.getTextcontent()));
				r.setFilename(StringEscapeUtils.unescapeJava(r.getFilename()));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		close();
		return r;
	}

	public HashSet<Report> searchReports(String pattern) {
		open();
		pattern = StringEscapeUtils.escapeJava(pattern);
		String SQL_QUERY= "SELECT * FROM reports WHERE title LIKE '%" + pattern
				+ "%' OR username LIKE '%" + pattern + "%' OR filename LIKE '%"
				+ pattern + "%' OR address LIKE '%" + pattern + "%' OR"
						+ " textcontent LIKE '%" + pattern + "%'";
		Statement stmt;
		Report r = null;
		HashSet<Report> rst = new HashSet<Report>();
		try {
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(SQL_QUERY);
			while(rs.next()) {
				r = new Report(Long.valueOf(Long.valueOf(rs.getInt("report_id"))),
						rs.getString("username"), rs.getString("address"), (Double)rs.getObject("latitude"),
						(Double)rs.getObject("longitude"), rs.getString("title"),
						(Double)rs.getObject("radius"), rs.getString("textcontent"),
						rs.getBlob("content").getBinaryStream(),
						rs.getString("filename"));
				r.setUser(StringEscapeUtils.unescapeJava(r.getUser()));
				r.setAddress(StringEscapeUtils.unescapeJava(r.getAddress()));
				r.setTitle(StringEscapeUtils.unescapeJava(r.getTitle()));
				r.setTextcontent(StringEscapeUtils.unescapeJava(r.getTextcontent()));
				r.setFilename(StringEscapeUtils.unescapeJava(r.getFilename()));
				rst.add(r);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		close();
		return rst;
	}
	
	public HashSet<Report> advancedSearchReports(String pattern, String[] categories) {
		open();
		pattern = StringEscapeUtils.escapeJava(pattern);
		String whereClause = "";
		for (int i = 0; i < categories.length; i++) {
			whereClause += categories[i] + " LIKE '%" + pattern + "%'";
			if (i != categories.length - 1) {
				whereClause += " OR ";
			}
		}	
		String SQL_QUERY= "SELECT * FROM reports WHERE " + whereClause;
		Statement stmt;
		Report r = null;
		HashSet<Report> rst = new HashSet<Report>();
		try {
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(SQL_QUERY);
			while(rs.next()) {
				r = new Report(Long.valueOf(Long.valueOf(rs.getInt("report_id"))),
						rs.getString("username"), rs.getString("address"), (Double)rs.getObject("latitude"),
						(Double)rs.getObject("longitude"), rs.getString("title"),
						(Double)rs.getObject("radius"), rs.getString("textcontent"),
						rs.getBlob("content").getBinaryStream(),
						rs.getString("filename"));
				r.setUser(StringEscapeUtils.unescapeJava(r.getUser()));
				r.setAddress(StringEscapeUtils.unescapeJava(r.getAddress()));
				r.setTitle(StringEscapeUtils.unescapeJava(r.getTitle()));
				r.setTextcontent(StringEscapeUtils.unescapeJava(r.getTextcontent()));
				r.setFilename(StringEscapeUtils.unescapeJava(r.getFilename()));
				rst.add(r);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		close();
		return rst;
	}
	
	//	Long _id;
	//	String username;
	//	String address;
	//	double latitude;
	//	double longitude;
	//	double radius;
	//	String title;
	//	String textcontent;
	//	InputStream content;
	//  String filename;
	public boolean updateReport(Report r) {
		open();
		String SQL_QUERY= "UPDATE reports SET username=?, address=?, latitude=?,"
				+ " longitude=?, radius=?, title=?, textcontent=? "
				+ (r.getContent() instanceof ByteArrayInputStream ? "" : ", filename=?, content=? ")+
				"WHERE report_id=?";
		PreparedStatement pst;
		try {
			pst = con.prepareStatement(SQL_QUERY);
			pst.setString(1, r.getUser());
			pst.setString(2, r.getAddress());
			if (r.getLatitude() == null) {
				pst.setNull(3, Types.DOUBLE);
			}
			else {
				pst.setDouble(3, r.getLatitude());
			}
			if (r.getLongitude() == null) {
				pst.setNull(4, Types.DOUBLE);
			}
			else {
				pst.setDouble(4, r.getLongitude());
			}
			if (r.getRadius() == null) {
				pst.setNull(5, Types.DOUBLE);
			}
			else {
				pst.setDouble(5, r.getRadius());
			}
			pst.setString(6, r.getTitle());
			pst.setString(7, r.getTextcontent());
			if (r.getContent() instanceof ByteArrayInputStream) {
				pst.setLong(8, r.get_id());
			}
			else if (r.getContent() instanceof FileInputStream){
				pst.setString(8, r.getFilename());
				pst.setBlob(9, r.getContent());
				pst.setLong(10, r.get_id());
			}
			int result = pst.executeUpdate();
			close();
			return (result > 0);
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		close();
		return false;
	}
	
	public boolean deleteReport(Long report_id) {
		open();
		String SQL_QUERY= "DELETE FROM reports WHERE report_id="+report_id;
		Statement stmt;
		try {
			stmt = con.createStatement();
			int result = stmt.executeUpdate(SQL_QUERY);
			close();
			return (result > 0);

		} catch (SQLException e) {
			e.printStackTrace();
		} 
		close();
		return false;
	}

	public boolean createUserWrapper(User u) {
		u.setFirstName(StringEscapeUtils.escapeHtml4(u.getFirstName()));
		u.setLastName(StringEscapeUtils.escapeHtml4(u.getLastName()));
		u.setUsername(StringEscapeUtils.escapeHtml4(u.getUsername()));
		u.setFirstName(StringEscapeUtils.escapeJava(u.getFirstName()));
		u.setLastName(StringEscapeUtils.escapeJava(u.getLastName()));
		u.setUsername(StringEscapeUtils.escapeJava(u.getUsername()));
		boolean result1 = insertUser(u);
		boolean result2 = insertUserRole(u);
		if (result1 == true && result2 == true) {
			return true;
		}
		return false;
	}
	
	private boolean insertUserRole(User u) {
		open();
		String SQL_QUERY = "INSERT INTO user_roles (username, rolename) VALUES (?, ?)";
		PreparedStatement pst;
		try {
			pst = con.prepareStatement(SQL_QUERY);
			pst.setString(1, u.getUsername());
			pst.setString(2, "normal");
			boolean result = (pst.executeUpdate()>0);
			close();
			return result;
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		close();
		return false;
	}

	//	Long user_id;
	//	String username;
	//	String password_hash;
	//	String firstName;
	//	String lastName;
	//	Date joinDate;
	public boolean insertUser(User u) {
		open();
		String SQL_QUERY = "INSERT INTO users (username, password_hash, firstname,"
				+ " lastname, joindate) VALUES (?, ?, ?, ?, ?)";
		PreparedStatement pst;
		try {
			pst = con.prepareStatement(SQL_QUERY);
			pst.setString(1, u.getUsername());
			pst.setString(2, u.getPassword());
//			pst.setBytes(3, u.getSecretKey());
			pst.setString(3, u.getFirstName());
			pst.setString(4, u.getLastName());
			pst.setDate(5, u.getJoinDate());
			boolean result = (pst.executeUpdate()>0);
			close();
			return result;
		} catch (SQLIntegrityConstraintViolationException e) {
			e.printStackTrace();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		close();
		return false;
	}

	public User getUserByUsername(String username) {
		open();
		String SQL_QUERY= "SELECT * from users WHERE username='" + username + "'";
		Statement stmt;
		User u = null;
		try {
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(SQL_QUERY);
			if(rs.next()) {
				u = new User(Long.valueOf(Long.valueOf(rs.getInt("user_id"))),
						rs.getString("username"), rs.getString("password_hash"),
						rs.getString("firstname"),
						rs.getString("lastname"), rs.getDate("joindate"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		close();
		u.setFirstName(StringEscapeUtils.unescapeJava(u.getFirstName()));
		u.setLastName(StringEscapeUtils.unescapeJava(u.getLastName()));
		u.setUsername(StringEscapeUtils.unescapeJava(u.getUsername()));
		return u;
	}

	public LinkedList<User> getAllUsers() {
		open();
		String SQL_QUERY= "SELECT * from users";
		Statement stmt;
		User u = null;
		LinkedList<User> llu = new LinkedList<User>();
		try {
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(SQL_QUERY);
			while(rs.next()) {
				u = new User(rs.getLong("user_id"), rs.getString("username"),
						rs.getString("password_hash"), rs.getString("firstname"),
						rs.getString("lastname"), rs.getDate("joindate"));
				u.setFirstName(StringEscapeUtils.unescapeJava(u.getFirstName()));
				u.setLastName(StringEscapeUtils.unescapeJava(u.getLastName()));
				u.setUsername(StringEscapeUtils.unescapeJava(u.getUsername()));
				llu.add(u);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		close();
		return llu;
	}
	
	public boolean deleteUser(String username) {
		open();
		String SQL_QUERY= "DELETE FROM users WHERE username='"+username+"'";
		Statement stmt;
		try {
			stmt = con.createStatement();
			int result = stmt.executeUpdate(SQL_QUERY);
			close();
			return (result > 0);

		} catch (SQLException e) {
			e.printStackTrace();
		} 
		close();
		return false;
	}
	
	public boolean deleteUserRole(String username) {
		open();
		String SQL_QUERY= "DELETE FROM user_roles WHERE username='"+username+"'";
		Statement stmt;
		try {
			stmt = con.createStatement();
			int result = stmt.executeUpdate(SQL_QUERY);
			close();
			return (result > 0);

		} catch (SQLException e) {
			e.printStackTrace();
		} 
		close();
		return false;
	}
	
	public boolean deleteUserReports(String username) {
		open();
		String SQL_QUERY= "DELETE FROM reports WHERE username='"+username+"'";
		Statement stmt;
		try {
			stmt = con.createStatement();
			int result = stmt.executeUpdate(SQL_QUERY);
			close();
			return (result > 0);

		} catch (SQLException e) {
			e.printStackTrace();
		} 
		close();
		return false;
	}
	
	public boolean deleteUserWrapper(String username) {
		boolean result1 = deleteUser(username);
		boolean result2 = deleteUserRole(username);
		boolean result3 = deleteUserReports(username);
		if (result1 == true && result2 == true && result3 == true) {
			return true;
		}
		return false;
	}

	public int uploadInitialData(InputStream dataStream) {
		int result = 0;
		try {
			JSONTokener tokener = new JSONTokener(new InputStreamReader(dataStream));
			JSONObject root = new JSONObject(tokener);
			result += uploadInitialReports(root);
			result += uploadInitialUsers(root);
		} catch (JSONException e) {
			e.printStackTrace();
			return 0;
		}
		return result;
	}
	
	public int uploadInitialUsers(JSONObject root) {
		open();
		int count = 0;
		String SQL_QUERY = "INSERT INTO users (username, password_hash, firstname,"
				+ " lastname, joindate) VALUES (?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE "
				+ "username=VALUES(username), password_hash=VALUES(password_hash), "
				+ "firstname=VALUES(firstname), lastname=VALUES(lastname), joindate="
				+ "VALUES(joindate)";
		PreparedStatement pst;
		JSONArray usersRoot = root.getJSONArray("users");
		for (int i = 0; i < usersRoot.length(); i++) {
			JSONObject userObject = usersRoot.getJSONObject(i);
			try {
				String username = userObject.getString("username");
				String password_hash = userObject.getString("password");
				String[] nameSplit = userObject.getString("name").split(" ");
				String firstname = nameSplit[0];
				String lastname = null;
				if (nameSplit.length > 1) {
					lastname = nameSplit[1];
				}
				User u = new User(null, username, password_hash, firstname, lastname, null);
				u.setFirstName(StringEscapeUtils.escapeJava(u.getFirstName()));
				u.setLastName(StringEscapeUtils.escapeJava(u.getLastName()));
				u.setUsername(StringEscapeUtils.escapeJava(u.getUsername()));
				pst = con.prepareStatement(SQL_QUERY);
				pst.setString(1, u.getUsername());
				pst.setString(2, u.getPassword());
				pst.setString(3, u.getFirstName());
				pst.setString(4, u.getLastName());
				pst.setString(5, null);
				count += pst.executeUpdate();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		close();
		return count;
	}

	public int uploadInitialReports(JSONObject root) {
		open();
		int count = 0;
		String SQL_QUERY = "INSERT INTO reports (report_id, username, address, latitude,"
				+ " longitude, radius, title, textcontent, content, filename) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE "
				+ "username=VALUES(username), address=VALUES(address), latitude="
				+ "VALUES(latitude), longitude=VALUES(longitude), radius="
				+ "VALUES(radius), title=VALUES(title), textcontent=VALUES(textcontent),"
				+ " content=VALUES(content), filename=VALUES(filename)";
		PreparedStatement pst;
		JSONArray reportsRoot = root.getJSONArray("reports");
		for (int i = 0; i < reportsRoot.length(); i++) {
			JSONObject reportObject = reportsRoot.getJSONObject(i);
			try {
				// Note: I take a shortcut here so that instead of having to re-write a lot of code
				// to allow for String ids, I calculate the given id's hashcode of type long
				// and use that as the id for reports - because the id type has previously been a long.
				Long report_id = Long.valueOf(reportObject.getString("id").hashCode());
				String username = reportObject.getString("user");
				JSONObject geometryObject = reportObject.getJSONObject("geometry");
				String type = geometryObject.getString("type");
				JSONArray coordinatesArray = geometryObject.getJSONArray("coordinates");
				Double latitude = coordinatesArray.getDouble(0);
				Double longitude = coordinatesArray.getDouble(1);
				Double radius;
				if (type.equals("Circle")) {
					radius = geometryObject.getDouble("Circle");
				} 
				else {
					radius = Double.valueOf(0);
				}
				String title = reportObject.getString("title");
				String filename = reportObject.getString("picture");
				URL url = new URL(filename);
				URLConnection urlCon = url.openConnection();
				InputStream content = urlCon.getInputStream();
				String textcontent = reportObject.getString("content");
				username = StringEscapeUtils.escapeHtml4(username);
				//					Note: No address is given.
				title = StringEscapeUtils.escapeHtml4(title);
				textcontent = StringEscapeUtils.escapeHtml4(textcontent);
				filename = StringEscapeUtils.escapeHtml4(filename);
				Report r = new Report(report_id, username, null, latitude, longitude, title, radius, textcontent, content, filename);
				r.setUser(StringEscapeUtils.escapeJava(r.getUser()));
				r.setAddress(StringEscapeUtils.escapeJava(r.getAddress()));
				r.setTitle(StringEscapeUtils.escapeJava(r.getTitle()));
				r.setTextcontent(StringEscapeUtils.escapeJava(r.getTextcontent()));
				r.setFilename(StringEscapeUtils.escapeJava(r.getFilename()));
				pst = con.prepareStatement(SQL_QUERY);
				pst.setLong(1, r.get_id());
				pst.setString(2, r.getUser());
				pst.setString(3, r.getAddress());
				if (r.getLatitude() == null) {
					pst.setNull(4, Types.DOUBLE);
				}
				else {
					pst.setDouble(4, r.getLatitude());
				}
				if (r.getLongitude() == null) {
					pst.setNull(5, Types.DOUBLE);
				}
				else {
					pst.setDouble(5, r.getLongitude());
				}
				if (r.getRadius() == null) {
					pst.setNull(6, Types.DOUBLE);
				}
				else {
					pst.setDouble(6, r.getRadius());
				}
				pst.setString(7, r.getTitle());
				pst.setString(8, r.getTextcontent());
				pst.setBlob(9, r.getContent());
				pst.setString(10, r.getFilename());
				count += pst.executeUpdate();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		close();
		return count;
	}
}
