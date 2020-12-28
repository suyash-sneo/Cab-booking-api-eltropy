package app.user;


import app.utils.*;
import app.ride.*;
import com.google.gson.Gson;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import static app.utils.RequestUtils.*;
import io.jsonwebtoken.*;
import org.mindrot.jbcrypt.*;
import spark.*;


public class UserController{
	
	public static boolean authenticate(String username, String password) {
		if(username.isEmpty() || password.isEmpty())
			return false;
		User user = UserData.getUser(username);
		if(user==null)
			return false;
		String hashedPassword = BCrypt.hashpw(password, user.getSalt());
		return hashedPassword.equals(user.getHashedPassword());
	}
	
	public static String ensureUserIsLoggedIn(Request request) {
		String auth_token = request.headers("x-auth-token");
		try {
			Jws<Claims> claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary("MySecret")).parseClaimsJws(auth_token);
			String username = (String)claims.getBody().get("username");
			User user = UserData.getUser(username);
			if(user!=null)
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
		
		User user = UserData.getUser(getQueryUsername(request));
		
		//The JWT signature algorithm we will be using to sign the token
	    SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
	    
	    //We will sign our JWT with our ApiKey secret
	    byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary("MySecret");
	    Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
		
	    
	    JwtBuilder builder = Jwts.builder().claim("username", user.getUsername()).signWith(signatureAlgorithm, signingKey);
	    
	    response.status(200);
	    response.body(builder.compact());
	    return response.body();
	};
	
	public static Route fetchRideFare = (Request request, Response response) -> {
		String username = ensureUserIsLoggedIn(request);
		if(!username.isEmpty()) {
			
			// Create a new ride with status 0
			Ride ride = new Ride(Double.parseDouble(request.queryParams("sLat")), 
					Double.parseDouble(request.queryParams("sLong")), 
					Double.parseDouble(request.queryParams("dLat")), 
					Double.parseDouble(request.queryParams("dLong")));
			
			String id = RideData.createRideAndGetId(ride);
			double fare = ride.getFare();
			
			response.status(200);
			response.body(new Gson().toJson(new RideWithFare(id, fare)));
			return response.body();
		}
		response.status(403);
		response.body("Login!!!");
		return response.body();
	};
	
	public static Route bookRide = (Request request, Response response) -> {
		String username = ensureUserIsLoggedIn(request);
		if(!username.isEmpty()) {
			
			String id = request.queryParams("id");
			// Change Ride's status to booked
			RideData.updateStatus(id, 1);
			// Add ride to customer's entry
			UserData.setRide(username, id);
			response.status(200);
			response.body("Ride booked successfully!!!");
			return response.body();
		}
		response.status(403);
		response.body("Login!!!");
		return response.body();
	};
	
	public static Route updateRideDestination = (Request request, Response response) -> {
		String username = ensureUserIsLoggedIn(request);
		if(!username.isEmpty()) {
			
			String id = request.queryParams("id");
			
			// Update destination and get the updated Fare
			double updatedFare = RideData.updateDestination(id, Double.parseDouble(request.queryParams("dLat")), Double.parseDouble(request.queryParams("dLong")));
			
			response.status(200);
			response.body("Updated Fare: "+updatedFare);
			return response.body();
		}
		response.status(403);
		response.body("Login!!!");
		return response.body();
	};
	
	public static Route finishRide = (Request request, Response response) -> {
		String username = ensureUserIsLoggedIn(request);
		if(!username.isEmpty()) {
			
			String id = request.queryParams("id");
			
			// Delete the ride from Ride's database
			RideData.finishRide(id);
			
			//Delete the ride from user
			UserData.finishRide(username);
			
			response.status(200);
			response.body("Ride Finished!");
			return response.body();
		}
		response.status(403);
		response.body("Login!!!");
		return response.body();
	};
}