package dbObjects;

import java.sql.Date;

public class User {
	private Long user_id;
	private String username;
	private byte[] password_hash;
	private byte[] secretKey;
	private String firstName;
	private String lastName;
	private Date joinDate;

	/**
	 * 
	 * @param user_id
	 * @param username
	 * @param password_hash
	 * @param firstName
	 * @param lastName
	 * @param joinDate
	 */
	public User(Long user_id, String username, byte[] password_hash, byte[] secretKey,
			String firstName, String lastName, Date joinDate) {
		this.user_id = user_id;
		this.username = username;
		this.password_hash = password_hash;
		this.firstName = firstName;
		this.lastName = lastName;
		this.joinDate = joinDate;
		this.secretKey = secretKey;
	}

	public Long getUser_id() {
		return user_id;
	}

	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Date getJoinDate() {
		return joinDate;
	}

	public void setJoinDate(Date joinDate) {
		this.joinDate = joinDate;
	}
	/**
	 * @return the user
	 */
	public Long getUserID() {
		return user_id;
	}
	/**
	 * @param user the user to set
	 */
	public void setUserID(Long user_id) {
		this.user_id = user_id;
	}
	/**
	 * @return the password
	 */
	public byte[] getPassword() {
		return password_hash;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(byte[] password_hash) {
		this.password_hash = password_hash;
	}
	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}
	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}
	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public byte[] getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(byte[] secretKey) {
		this.secretKey = secretKey;
	}
}
