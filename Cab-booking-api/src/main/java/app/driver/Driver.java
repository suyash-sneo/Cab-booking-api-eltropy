package app.driver;

public class Driver{
	String username;
	String salt;
	String hashedPassword;
	double latitude, longitude;
	String currentRideId;
	
	public Driver(String username, String salt, String hashedPassword) {
		this.username = username;
		this.salt = salt;
		this.hashedPassword = hashedPassword;
		this.latitude = 0.0;
		this.longitude = 0.0;
		this.currentRideId = "";
	}
	
	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}
	
	public void setLocation(double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public String getCurrentRideId() {
		return currentRideId;
	}
	public void setCurrentRideId(String currentRideId) {
		this.currentRideId = currentRideId;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getHashedPassword() {
		return hashedPassword;
	}
	public String getSalt() {
		return salt;
	}
}