package testPackage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
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
	//	double latitude;
	//	double longitude;
	//	double radius;
	//	String title;
	//	InputStream content;
	public boolean insertReport(Report r) {
		open();
		String SQL_QUERY = "INSERT INTO reports (username, latitude, longitude,"
				+ " radius, title, content, filename) VALUES (?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement pst;
		r.setUser(StringEscapeUtils.escapeJava(r.getUser()));
		r.setTitle(StringEscapeUtils.escapeJava(r.getTitle()));
		r.setFilename(StringEscapeUtils.escapeJava(r.getFilename()));
		try {
			pst = con.prepareStatement(SQL_QUERY);
			pst.setString(1, r.getUser());
			pst.setDouble(2, r.getLatitude());
			pst.setDouble(3, r.getLongitude());
			pst.setDouble(4, r.getRadius());
			pst.setString(5, r.getTitle());
			pst.setBlob(6, r.getContent());
			pst.setString(7, r.getFilename());
			return (pst.executeUpdate()>0);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		close();
		return false;
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
						rs.getString("username"), rs.getDouble("latitude"),
						rs.getDouble("longitude"), rs.getString("title"),
						rs.getDouble("radius"),
						rs.getBlob("content").getBinaryStream(),
						rs.getString("filename"));
				r.setUser(StringEscapeUtils.unescapeJava(r.getUser()));
				r.setTitle(StringEscapeUtils.unescapeJava(r.getTitle()));
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
						rs.getString("username"), rs.getDouble("latitude"),
						rs.getDouble("longitude"), rs.getString("title"),
						rs.getDouble("radius"),
						rs.getBlob("content").getBinaryStream(),
						rs.getString("filename"));
				r.setUser(StringEscapeUtils.unescapeJava(r.getUser()));
				r.setTitle(StringEscapeUtils.unescapeJava(r.getTitle()));
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
						rs.getString("username"), rs.getDouble("latitude"),
						rs.getDouble("longitude"), rs.getString("title"),
						rs.getDouble("radius"),
						rs.getBlob("content").getBinaryStream(),
						rs.getString("filename"));
				r.setUser(StringEscapeUtils.unescapeJava(r.getUser()));
				r.setTitle(StringEscapeUtils.unescapeJava(r.getTitle()));
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
						rs.getString("username"), rs.getDouble("latitude"),
						rs.getDouble("longitude"), rs.getString("title"),
						rs.getDouble("radius"),
						rs.getBlob("content").getBinaryStream(),
						rs.getString("filename"));
				r.setUser(StringEscapeUtils.unescapeJava(r.getUser()));
				r.setTitle(StringEscapeUtils.unescapeJava(r.getTitle()));
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
		String SQL_QUERY= "SELECT * FROM reports WHERE title LIKE '%"
				+ pattern + "%' OR username LIKE '%" + pattern + "%' OR filename LIKE"
				+ " '%" + pattern + "%'";
		System.out.println(SQL_QUERY);
		Statement stmt;
		Report r = null;
		HashSet<Report> rst = new HashSet<Report>();
		try {
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(SQL_QUERY);
			if(rs.next()) {
				r = new Report(Long.valueOf(Long.valueOf(rs.getInt("report_id"))),
						rs.getString("username"), rs.getDouble("latitude"),
						rs.getDouble("longitude"), rs.getString("title"),
						rs.getDouble("radius"),
						rs.getBlob("content").getBinaryStream(),
						rs.getString("filename"));
				r.setUser(StringEscapeUtils.unescapeJava(r.getUser()));
				r.setTitle(StringEscapeUtils.unescapeJava(r.getTitle()));
				r.setFilename(StringEscapeUtils.unescapeJava(r.getFilename()));
				rst.add(r);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rst;
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
			return (pst.executeUpdate()>0);
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
