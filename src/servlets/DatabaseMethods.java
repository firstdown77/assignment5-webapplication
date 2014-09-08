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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import dbObjects.Event;
import dbObjects.Report;
import dbObjects.User;

public class DatabaseMethods {

	private Connection con;
	private String url = "jdbc:mysql://localhost:3306/";
	private String dbName = "disasterevacuationdb";
	private String driver = "com.mysql.jdbc.Driver";
	private String userName = "root";
	private String password = "1234";
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
				keys.close();
				return toReturn;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			close();
		}
		return null;
	}

	public List<Report> getReportsByUser(String username) {
		open();
		String SQL_QUERY= "SELECT * from reports WHERE username='" + username + "'";
		Statement stmt;
		Report r = null;
		LinkedList<Report> hsr = new LinkedList<Report>();
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
				if (r.getTitle() == null) r.setTitle("Untitled");
				hsr.add(r);
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		close();
		return hsr;
	}

	public ArrayList<Report> getAllReports() {
		open();
		String SQL_QUERY= "SELECT * from reports";
		Statement stmt;
		Report r = null;
		ArrayList<Report> hsr = new ArrayList<Report>();
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
			rs.close();
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
			rs.close();
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
			rs.close();
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
			rs.close();
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
			rs.close();
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
			rs.close();
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
			result += uploadInitialUserRoles(root);
			result += uploadInitialEvents(root);
		} catch (JSONException e) {
			e.printStackTrace();
			return 0;
		}
		return result;
	}
	
	public int uploadInitialEvents(JSONObject root) throws JSONException {
		open();
		int count = 0;
		String SQL_QUERY = "INSERT INTO events (date, username, longitude,"
				+ " latitude, capacity, evacuation_means) VALUES (?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE "
				+ "date=VALUES(date), username=VALUES(username), "
				+ "longitude=VALUES(longitude), latitude=VALUES(latitude), capacity="
				+ "VALUES(capacity), evacuation_means=VALUES(evacuation_means)";
		PreparedStatement pst;
		JSONArray eventsRoot = root.getJSONArray("evacuationEvents");
		for (int i = 0; i < eventsRoot.length(); i++) {
			JSONObject eventObject = eventsRoot.getJSONObject(i);
			try {
				String meanOfEvacuation = eventObject.getString("meanOfEvacuation");
				String estimatedTime = eventObject.getString("estimatedTime");
				
				Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss X", Locale.ENGLISH).parse(estimatedTime.replace('T', ' '));
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
				String reportDate = df.format(date);
				int capacity = eventObject.getInt("capacity");
				JSONObject geometryObject = eventObject.getJSONObject("geometry");
				String type = geometryObject.getString("type");
				JSONArray coordinatesArray = geometryObject.getJSONArray("coordinates");
				Double latitude = coordinatesArray.getDouble(0);
				Double longitude = coordinatesArray.getDouble(1);
				pst = con.prepareStatement(SQL_QUERY);
				pst.setString(1, reportDate);
				pst.setString(2, UserVariables.adminUsername);
				pst.setDouble(3, longitude);
				pst.setDouble(4, latitude);
				pst.setInt(5, capacity);
				pst.setString(6, meanOfEvacuation);
				count += pst.executeUpdate();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		close();
		return count;
	}
	
	public int uploadInitialUsers(JSONObject root) throws JSONException {
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
	
	public int uploadInitialUserRoles(JSONObject root) throws JSONException {
		open();
		int count = 0;
		String SQL_QUERY = "INSERT INTO user_roles (username, rolename) VALUES (?, ?) ON DUPLICATE KEY UPDATE "
				+ "username=VALUES(username), rolename=VALUES(rolename)";
		PreparedStatement pst;
		JSONArray usersRoot = root.getJSONArray("users");
		for (int i = 0; i < usersRoot.length(); i++) {
			JSONObject userObject = usersRoot.getJSONObject(i);
			try {
				String username = userObject.getString("username");
				String rolename = "normal";
				pst = con.prepareStatement(SQL_QUERY);
				pst.setString(1, username);
				pst.setString(2, rolename);
				count += pst.executeUpdate();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		close();
		return count;
	}

	public int uploadInitialReports(JSONObject root) throws JSONException {
		open();
		int count = 0;
		String SQL_QUERY = "INSERT INTO reports (username, address, latitude,"
				+ " longitude, radius, title, textcontent, content, filename) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE "
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
				urlCon.connect();
				InputStream content = urlCon.getInputStream();				
				String textcontent = reportObject.getString("content");
				username = StringEscapeUtils.escapeHtml4(username);
				//					Note: No address is given.
				title = StringEscapeUtils.escapeHtml4(title);
				textcontent = StringEscapeUtils.escapeHtml4(textcontent);
				filename = StringEscapeUtils.escapeHtml4(filename);
				Report r = new Report(null, username, null, latitude, longitude, title, radius, textcontent, content, filename);
				r.setUser(StringEscapeUtils.escapeJava(r.getUser()));
				r.setAddress(StringEscapeUtils.escapeJava(r.getAddress()));
				r.setTitle(StringEscapeUtils.escapeJava(r.getTitle()));
				r.setTextcontent(StringEscapeUtils.escapeJava(r.getTextcontent()));
				r.setFilename(StringEscapeUtils.escapeJava(r.getFilename()));
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
				pst.setBlob(8, r.getContent());
				pst.setString(9, r.getFilename());
				count += pst.executeUpdate();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		close();
		return count;
	}
	
	private ArrayList<Event> getEvents(boolean all) {
		open();
		String SQL_QUERY;
		if (!all)
			SQL_QUERY= "SELECT * from events WHERE date >= now()";
		else
			SQL_QUERY= "SELECT * from events";
		
		Statement stmt;
		Event e = null;
		ArrayList<Event> hsr = new ArrayList<Event>();
		try {
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(SQL_QUERY);
			while(rs.next()) {
				e = new Event();
				e.set_id(Long.valueOf(Long.valueOf(rs.getInt("event_id"))));
				e.setUsername(rs.getString("username")); 
				e.setDate(rs.getString("date"));
				e.setLatitude((Double)rs.getObject("latitude"));
				e.setLongitude((Double)rs.getObject("longitude"));
				e.setEvacuationMeans(rs.getString("evacuation_means"));
				e.setCapacity(rs.getInt("capacity"));
						
				e.setUsername(StringEscapeUtils.unescapeJava(e.getUsername()));
				e.setEvacuationMeans(StringEscapeUtils.unescapeJava(e.getEvacuationMeans()));
				e.setDate(StringEscapeUtils.unescapeJava(e.getDate()));
				hsr.add(e);
			}
			rs.close();
		} catch (SQLException exc) {
			exc.printStackTrace();
		} 
		finally{
			close();
		}
		return hsr;
	}
	
	public ArrayList<Event> getAllEvents() {
		return getEvents(true);
	}
	
	public ArrayList<Event> getUpcomingEvents() {
		return getEvents(false);
	}
	
	public Event getEventById(Long id)
	{
		return getEventById(id, true);
	}
	
	public Event getEventById(Long id, boolean open) {
		int eventID = id.intValue();
		if (open)
			open();
		String SQL_QUERY= "SELECT * from events WHERE event_id='" + eventID + "'";
		Statement stmt;
		Event e = null;
		try {
			//Get event
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(SQL_QUERY);
			if(rs.next()) {
				e = new Event();
				e.set_id(Long.valueOf(Long.valueOf(rs.getInt("event_id"))));
				e.setUsername(rs.getString("username")); 
				e.setDate(rs.getString("date"));
				e.setLatitude((Double)rs.getObject("latitude"));
				e.setLongitude((Double)rs.getObject("longitude"));
				e.setEvacuationMeans(rs.getString("evacuation_means"));
				e.setCapacity(rs.getInt("capacity"));
						
				e.setUsername(StringEscapeUtils.unescapeJava(e.getUsername()));
				e.setEvacuationMeans(StringEscapeUtils.unescapeJava(e.getEvacuationMeans()));
				e.setDate(StringEscapeUtils.unescapeJava(e.getDate()));
			}
			rs.close();
			
			if (e != null)
			{
				//Get users registered to the event
				SQL_QUERY= "SELECT * from users, events_users WHERE users.user_id = events_users.user_id and events_users.event_id="+ eventID;
				User u = null;
	
				stmt = con.createStatement();
				rs = stmt.executeQuery(SQL_QUERY);
				LinkedList<User> llu = new LinkedList<User>();
				while(rs.next()) {
					u = new User(Long.valueOf(Long.valueOf(rs.getInt("user_id"))),
							rs.getString("username"), rs.getString("password_hash"),
							rs.getString("firstname"),
							rs.getString("lastname"), rs.getDate("joindate"));
					u.setFirstName(StringEscapeUtils.unescapeJava(u.getFirstName()));
					u.setLastName(StringEscapeUtils.unescapeJava(u.getLastName()));
					u.setUsername(StringEscapeUtils.unescapeJava(u.getUsername()));
					llu.add(u);
				}
				rs.close();	
				e.setRegistered(llu.toArray(new User[llu.size()]));
			}
		} catch (SQLException exc) {
			exc.printStackTrace();
		}finally
		{
			if (open)
				close();
		}
		return e;
	}
	
	public Event getRegisteredEvent(String username) {
		User u = getUserByUsername(username);
		int userID = u.getUserID().intValue();
		return getRegisteredEvent(userID, true);
	}
	
	public Event getRegisteredEvent(int userID, boolean open) {
		Event e = null;
		try {
			if (open)
				open();
			String SQL_QUERY= "SELECT * from events, events_users  WHERE events.event_id = events_users.event_id and date >= now() and user_id=" + userID;
			Statement stmt;
			
			//Get event
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(SQL_QUERY);
			if(rs.next()) {
				e = new Event();
				e.set_id(Long.valueOf(Long.valueOf(rs.getInt("event_id"))));
				e.setUsername(rs.getString("username")); 
				e.setDate(rs.getString("date"));
				e.setLatitude((Double)rs.getObject("latitude"));
				e.setLongitude((Double)rs.getObject("longitude"));
				e.setEvacuationMeans(rs.getString("evacuation_means"));
				e.setCapacity(rs.getInt("capacity"));
						
				e.setUsername(StringEscapeUtils.unescapeJava(e.getUsername()));
				e.setEvacuationMeans(StringEscapeUtils.unescapeJava(e.getEvacuationMeans()));
				e.setDate(StringEscapeUtils.unescapeJava(e.getDate()));
				e.setRegistered(new User[0]);
			}
			rs.close();
			
			if (e != null)
			{
				//Get users registered to the event
				SQL_QUERY= "SELECT * from users, events_users WHERE users.user_id = events_users.user_id and events_users.event_id="+ e.get_id().intValue();
				User u = null;
	
				stmt = con.createStatement();
				rs = stmt.executeQuery(SQL_QUERY);
				LinkedList<User> llu = new LinkedList<User>();
				while(rs.next()) {
					u = new User(Long.valueOf(Long.valueOf(rs.getInt("user_id"))),
							rs.getString("username"), rs.getString("password_hash"),
							rs.getString("firstname"),
							rs.getString("lastname"), rs.getDate("joindate"));
					u.setFirstName(StringEscapeUtils.unescapeJava(u.getFirstName()));
					u.setLastName(StringEscapeUtils.unescapeJava(u.getLastName()));
					u.setUsername(StringEscapeUtils.unescapeJava(u.getUsername()));
					llu.add(u);
				}
				rs.close();	
				e.setRegistered(llu.toArray(new User[llu.size()]));
			}
		} catch (SQLException exc) {
			exc.printStackTrace();
		}finally
		{
			if (open)
				close();
		}
		return e;
	}
	
	public Long insertEvent(Event e) {
		open();
		String SQL_QUERY = "INSERT INTO events (date, username, longitude, latitude,"
				+ " evacuation_means, capacity) "
				+ "VALUES (?, ?, ?, ?, ?, ?)";
		PreparedStatement pst;
		e.setUsername(StringEscapeUtils.escapeHtml4(e.getUsername()));
		e.setDate(StringEscapeUtils.escapeHtml4(e.getDate()));
		e.setEvacuationMeans(StringEscapeUtils.escapeHtml4(e.getEvacuationMeans()));
		try {
			pst = con.prepareStatement(SQL_QUERY, Statement.RETURN_GENERATED_KEYS);
			pst.setString(1, e.getDate());
			pst.setString(2, e.getUsername());
			pst.setDouble(3, e.getLongitude());
			pst.setDouble(4, e.getLatitude());
			pst.setString(5, e.getEvacuationMeans());
			pst.setInt(6, e.getCapacity());
			pst.executeUpdate();
			ResultSet keys = pst.getGeneratedKeys();
			if (keys.next()) {
				long toReturn = keys.getLong(1);
				keys.close();
				return toReturn;
			}
			else
			{
				keys.close();
				return new Long(-1);
			}
		} catch (SQLException exc) {
			exc.printStackTrace();
			return new Long(-1);
		}
		finally{
			close();
		}
	}
	
	public boolean deleteEvent(Long event_id) {
		open();
		String SQL_QUERY= "DELETE FROM events WHERE event_id="+event_id;
		Statement stmt;
		try {
			stmt = con.createStatement();
			int result = stmt.executeUpdate(SQL_QUERY);
			return (result > 0);

		} catch (SQLException e) {
			e.printStackTrace();
		} 
		finally{
			close();
		}
		return false;
	}
	
	public void unregisterFromEvent(int event_id, int user_id){
		open();
		
		String SQL_QUERY = "DELETE FROM events_users WHERE user_id=? and event_id=? ";
		PreparedStatement pst;
		try {
			pst = con.prepareStatement(SQL_QUERY, Statement.RETURN_GENERATED_KEYS);
			pst.setInt(1, user_id);
			pst.setInt(2, event_id);
			pst.executeUpdate();
		}catch (Exception exc)
		{
			exc.printStackTrace();
		}
		finally
		{
			close();
		}
	}
	
	public Long addUserToEvent(int event_id, int user_id) {
		open();
		
		String SQL_QUERY = "INSERT INTO events_users (user_id, event_id) "
				+ "VALUES (?, ?)";
		PreparedStatement pst;
		try {
			con.setAutoCommit(false);
			con.createStatement().execute("LOCK TABLES events READ, users READ, events_users WRITE");
			Event e = getEventById(new Long(event_id), false);
			Event er = getRegisteredEvent(user_id, false);
			Long toReturn;
			if (er == null)
			{
				if (e.getRegistered().length < e.getCapacity())
				{
					pst = con.prepareStatement(SQL_QUERY, Statement.RETURN_GENERATED_KEYS);
					pst.setInt(1, user_id);
					pst.setInt(2, event_id);
					pst.executeUpdate();
					con.commit();
					toReturn = new Long(1);
				}
				else
				{
					con.rollback();
					toReturn = new Long(-1);
				}
			}else{
				toReturn = new Long(-1);	
			}
			con.createStatement().execute("UNLOCK TABLES");
			return toReturn;
		} catch (SQLException exc) {
			exc.printStackTrace();
			try{con.rollback();con.createStatement().execute("UNLOCK TABLES");}catch(Exception excc){}
			return new Long(-1);
		}
		finally{
			close();
		}
	}
	
	public Event searchNearestEvent(double longitude, double latitude)
	{	
		Event e = null;
		try {
			open();
			String SQL_QUERY= "SELECT event_id, username, date, evacuation_means, capacity, "+
			"latitude, longitude, SQRT(POW(69.1 * (latitude - "+latitude+"), 2) + " +
			   "POW(69.1 * ("+longitude+" - longitude) * COS(latitude / 57.3), 2)) AS distance "+
			    "from events WHERE date >= now() ORDER BY distance";
			Statement stmt;
			
			//Get event
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(SQL_QUERY);
			if(rs.next()) {
				e = new Event();
				e.set_id(Long.valueOf(Long.valueOf(rs.getInt("event_id"))));
				e.setUsername(rs.getString("username")); 
				e.setDate(rs.getString("date"));
				e.setLatitude((Double)rs.getObject("latitude"));
				e.setLongitude((Double)rs.getObject("longitude"));
				e.setEvacuationMeans(rs.getString("evacuation_means"));
				e.setCapacity(rs.getInt("capacity"));
						
				e.setUsername(StringEscapeUtils.unescapeJava(e.getUsername()));
				e.setEvacuationMeans(StringEscapeUtils.unescapeJava(e.getEvacuationMeans()));
				e.setDate(StringEscapeUtils.unescapeJava(e.getDate()));
			}
			rs.close();
		} catch (SQLException exc) {
			exc.printStackTrace();
		}finally
		{
			close();
		}
		return e;
		
	}

}
