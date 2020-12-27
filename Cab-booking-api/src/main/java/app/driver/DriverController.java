package app.driver;

import org.mindrot.jbcrypt.*;

public class DriverController{
	
	public static boolean authenticate(String username, String password) {
		if(username.isEmpty() || password.isEmpty())
			return false;
		Driver driver = DriverData.getDriver(username);
		if(driver==null)
			return false;
		String hashedPassword = BCrypt.hashpw(password, driver.getSalt());
		return hashedPassword.equals(driver.getHashedPassword());
	}
}