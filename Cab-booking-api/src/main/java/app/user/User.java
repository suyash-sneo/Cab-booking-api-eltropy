package app.user;

import org.mindrot.jbcrypt.*;

public class User{
	String username;
	String salt;
	String hashedPassword;
	String currentRideId;
	
	public User(String username, String password) {
		this.username = username;
		this.salt = BCrypt.gensalt();
		this.hashedPassword = BCrypt.hashpw(password, this.salt);
		this.currentRideId = "";
	}
	
	public String getSalt() {
		return this.salt;
	}
	
	public String getUsername() {
		return this.username;
	}
	
	public String getHashedPassword() {
		return this.hashedPassword;
	}
	
	public String getCurrentRideId() {
		return this.currentRideId;
	}
	public void setCurrentRideId(String currentRideId) {
		this.currentRideId = currentRideId;
	}
}