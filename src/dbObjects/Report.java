package dbObjects;

import java.io.InputStream;

public class Report {
	private Long _id;
	private String username;
	private double latitude;
	private double longitude;
	private double radius;
	private String title;
	private InputStream content;
	private String filename;
	
	public Report(Long _id, String user, double latitude, double longitude,
			String title, double radius, InputStream content, String filename) {
		super();
		this._id = _id;
		this.username = user;
		this.latitude = latitude;
		this.longitude = longitude;
		this.title = title;
		this.radius = radius;
		this.content = content;
		this.filename = filename;
	}
	
	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	/**
	 * @return the _id
	 */
	public Long get_id() {
		return _id;
	}
	/**
	 * @param _id the _id to set
	 */
	public void set_id(Long _id) {
		this._id = _id;
	}
	/**
	 * @return the user
	 */
	public String getUser() {
		return username;
	}
	/**
	 * @param user the user to set
	 */
	public void setUser(String user) {
		this.username = user;
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

	public InputStream getContent() {
		return content;
	}

	public void setContent(InputStream content) {
		this.content = content;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}
}
