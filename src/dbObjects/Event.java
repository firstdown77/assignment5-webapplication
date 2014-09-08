package dbObjects;

public class Event {
	private Long _id;
	private String username;
	private String date;
	private String evacuationMeans;
	private User[] registered;
	private int capacity;
	private Double latitude;
	private Double longitude;
	
	public Event()
	{
		
	}
	
	public Long get_id() {
		return _id;
	}
	public void set_id(Long _id) {
		this._id = _id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getEvacuationMeans() {
		return evacuationMeans;
	}
	public void setEvacuationMeans(String evacuationMeans) {
		this.evacuationMeans = evacuationMeans;
	}
	public User[] getRegistered() {
		return registered;
	}
	public void setRegistered(User[] registered) {
		this.registered = registered;
	}
	public int getCapacity() {
		return capacity;
	}
	public void setCapacity(int capacity) {
		this.capacity = capacity;
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
}
