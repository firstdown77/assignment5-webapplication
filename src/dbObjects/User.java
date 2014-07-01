package dbObjects;

public class User {
	private String user;
	private String password;
	private String firstName;
	private String lastName;
	
	/**
	 * @param user
	 * @param password
	 * @param firstName
	 * @param lastName
	 */
	public User(String user, String password, String firstName, String lastName) {
		this.user = user;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
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
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
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
}
