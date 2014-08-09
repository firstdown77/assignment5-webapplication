package servlets;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.sql.Types;
import java.util.HashSet;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang3.StringEscapeUtils;

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
			pst.setString(9, r.getFilename());
			pst.executeUpdate();
			ResultSet keys = pst.getGeneratedKeys();
			if (keys.next()) {
				return keys.getLong(1);
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

	//	Long user_id;
	//	String username;
	//	String password_hash;
	//	byte[] secretKey;
	//	String firstName;
	//	String lastName;
	//	Date joinDate;
	public boolean insertUser(User u) {
		open();
		String SQL_QUERY = "INSERT INTO users (username, password_hash, secretkey, firstname,"
				+ " lastname, joindate) VALUES (?, ?, ?, ?, ?, ?)";
		PreparedStatement pst;
		try {
			pst = con.prepareStatement(SQL_QUERY);
			pst.setString(1, u.getUsername());
			pst.setBytes(2, u.getPassword());
			pst.setBytes(3, u.getSecretKey());
			pst.setString(4, u.getFirstName());
			pst.setString(5, u.getLastName());
			pst.setDate(6, u.getJoinDate());
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
						rs.getString("username"), rs.getBytes("password_hash"),
						rs.getBytes("secretkey"), rs.getString("firstname"),
						rs.getString("lastname"), rs.getDate("joindate"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		close();
		return u;
	}

	public boolean verifyPassword(String username, String password) {
		User u = getUserByUsername(username);
		try {
			// create a cipher based upon Blowfish
			Cipher cipher = Cipher.getInstance("Blowfish");
			// get the secret key previously used
			SecretKey secretkey = new SecretKeySpec(u.getSecretKey(), 0,
					u.getSecretKey().length, "Blowfish");
			// re-initialise the cipher to be in decrypt mode
			cipher.init(Cipher.DECRYPT_MODE, secretkey);
			// get encrypted message
			byte[] encrypted = u.getPassword();
			// decrypt message
			byte[] decrypted = cipher.doFinal(encrypted);
			if (password.equals(new String(decrypted))) {
				return true;
			}
			else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
