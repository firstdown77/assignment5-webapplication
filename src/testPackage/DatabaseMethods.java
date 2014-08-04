package testPackage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;

import dbObjects.Report;

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
		//Query to determine if course already exists in DB.
		open();
		String SQL_QUERY = "INSERT INTO reports (username, latitude, longitude,"
				+ " radius, title, content, filename) VALUES (?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement pst;
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
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		close();
		return r;
	}
	
	public HashSet<Report> searchReports(String pattern) {
		open();
		String SQL_QUERY= "SELECT * FROM reports WHERE title LIKE '%"
		+ pattern + "%' OR username LIKE '%" + pattern + "&' OR filename LIKE"
				+ " '&" + pattern + "&'";
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
				rst.add(r);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rst;
	}
	
	
}
