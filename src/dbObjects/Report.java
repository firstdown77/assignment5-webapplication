package dbObjects;

import java.io.InputStream;

public class Report {
	private Long _id;
	private String username;
	private String address;
	private Double latitude;
	private Double longitude;
	private Double radius;
	private String title;
	private String textcontent;
	private InputStream content;
	private String filename;
	
	public Report(Long _id, String username, String address, Double latitude, Double longitude,
			String title, Double radius, String textcontent, InputStream content, String filename) {
		super();
		this._id = _id;
		this.username = username;
		this.address = address;
		this.latitude = latitude;
		this.longitude = longitude;
		this.title = title;
		this.radius = radius;
		this.textcontent = textcontent;
		this.content = content;
		this.filename = filename;
	}
	
	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
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

	public Double getRadius() {
		return radius;
	}

	public void setRadius(Double radius) {
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTextcontent() {
		return textcontent;
	}

	public void setTextcontent(String textcontent) {
		this.textcontent = textcontent;
	}
}
