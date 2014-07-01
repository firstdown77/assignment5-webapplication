package dbObjects;

import java.sql.Time;

public class EvacuationEvent {
	private Long _id;
	private String geometry;
	private Time estimatedTime;
	private String meansOfEvacuation;
	private Integer capacity;
	private Integer registrationCount;
	
	/**
	 * @param _id
	 * @param geometry
	 * @param estimatedTime
	 * @param meansOfEvacuation
	 * @param capacity
	 * @param registrationCount
	 */
	public EvacuationEvent(Long _id, String geometry, Time estimatedTime,
			String meansOfEvacuation, Integer capacity,
			Integer registrationCount) {
		this._id = _id;
		this.geometry = geometry;
		this.estimatedTime = estimatedTime;
		this.meansOfEvacuation = meansOfEvacuation;
		this.capacity = capacity;
		this.registrationCount = registrationCount;
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
	 * @return the estimatedTime
	 */
	public Time getEstimatedTime() {
		return estimatedTime;
	}
	/**
	 * @param estimatedTime the estimatedTime to set
	 */
	public void setEstimatedTime(Time estimatedTime) {
		this.estimatedTime = estimatedTime;
	}
	/**
	 * @return the meansOfEvacuation
	 */
	public String getMeansOfEvacuation() {
		return meansOfEvacuation;
	}
	/**
	 * @param meansOfEvacuation the meansOfEvacuation to set
	 */
	public void setMeansOfEvacuation(String meansOfEvacuation) {
		this.meansOfEvacuation = meansOfEvacuation;
	}
	/**
	 * @return the capacity
	 */
	public Integer getCapacity() {
		return capacity;
	}
	/**
	 * @param capacity the capacity to set
	 */
	public void setCapacity(Integer capacity) {
		this.capacity = capacity;
	}
	/**
	 * @return the registrationCount
	 */
	public Integer getRegistrationCount() {
		return registrationCount;
	}
	/**
	 * @param registrationCount the registrationCount to set
	 */
	public void setRegistrationCount(Integer registrationCount) {
		this.registrationCount = registrationCount;
	}
}
