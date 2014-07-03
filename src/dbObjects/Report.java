package dbObjects;

public class Report {
	private Long _id;
	private String user;
	private String geometry;
	private String title;

	
	/**
	 * @param _id
	 * @param user
	 * @param geometry
	 * @param title
	 */
	public Report(Long _id, String user, String geometry, String title) {
		this._id = _id;
		this.user = user;
		this.geometry = geometry;
		this.title = title;
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
		return user;
	}
	/**
	 * @param user the user to set
	 */
	public void setUser(String user) {
		this.user = user;
	}
	/**
	 * @return the geometry
	 */
	public String getGeometry() {
		return geometry;
	}
	/**
	 * @param geometry the geometry to set
	 */
	public void setGeometry(String geometry) {
		this.geometry = geometry;
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
}
