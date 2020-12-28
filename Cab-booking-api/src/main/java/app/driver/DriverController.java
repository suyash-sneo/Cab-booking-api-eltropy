package app.driver;


import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import static app.utils.RequestUtils.*;
import io.jsonwebtoken.*;
import org.mindrot.jbcrypt.*;

import app.ride.RideData;
import spark.*;

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
	
	public static String ensureUserIsLoggedIn(Request request) {
		String auth_token = request.headers("x-auth-token");
		try {
			Jws<Claims> claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary("MySecret")).parseClaimsJws(auth_token);
			String username = (String)claims.getBody().get("username");
			Driver driver = DriverData.getDriver(username);
			if(driver!=null)
				return username;
			return "";
		}
		catch(Exception e) {
			System.out.println(e.toString());
			return "";
		}
	}
	
	public static Route handleLoginPost = (Request request, Response response) -> {
		if(!authenticate(getQueryUsername(request), getQueryPassword(request))) {
			response.status(401);
			response.body("Authentication failed");
			return response.body();
		}
		
		Driver driver = DriverData.getDriver(getQueryUsername(request));
		
		//The JWT signature algorithm we will be using to sign the token
	    SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
	    
	    //We will sign our JWT with our ApiKey secret
	    byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary("MySecret");
	    Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
	    
	    JwtBuilder builder = Jwts.builder().claim("username", driver.getUsername()).signWith(signatureAlgorithm, signingKey);
	    
	    response.status(200);
	    response.body(builder.compact());
	    return response.body();
	};
	
	public static Route fetchNearestRide = (Request request, Response response) -> {
		String username = ensureUserIsLoggedIn(request);
		if(!username.isEmpty()) {
			
			double latitude = Double.parseDouble(request.queryParams("lat"));
			double longitude = Double.parseDouble(request.queryParams( "long"));
			
			// Fetch the ride
			String nearestRide = RideData.getNearestRide(latitude, longitude);
			response.status(200);
			response.body(nearestRide);
			return response.body();
			
		}
		response.status(403);
		response.body("Login!!!");
		return response.body();
	};
	
	public static Route acceptRide = (Request request, Response response) -> {
		String username = ensureUserIsLoggedIn(request);
		if(!username.isEmpty()) {
			
			String id = request.queryParams("id");
			
			// Accept the ride
			DriverData.setRide(username, id);

			response.status(200);
			response.body("Ride accepted!!!");
			return response.body();
		}
		response.status(403);
		response.body("Login!!!");
		return response.body();
	};
}

