package app.user;


import app.utils.*;
import app.ride.*;
import com.google.gson.Gson;
import com.google.gson.Gson;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import static app.utils.RequestUtils.*;
import io.jsonwebtoken.*;
import org.mindrot.jbcrypt.*;
import spark.*;
import java.util.*;


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
	
	public static boolean ensureUserIsLoggedIn(Request request) {
		String auth_token = request.headers("x-auth-token");
		try {
			String json = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary("MySecret")).parsePlaintextJwt(auth_token).getBody();
			Gson gson = new Gson();
			UsernameToken tk = gson.fromJson(json, UsernameToken.class);
			String username = tk.getUsername();
			User user = UserData.getUser(username);
			if(user!=null)
				return true;
			return false;
		}
		catch(Exception e) {
			System.out.println(e.toString());
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
	    
	    JwtBuilder builder = Jwts.builder().setPayload(new Gson().toJson(new UsernameToken(user.getUsername()))).signWith(signatureAlgorithm, signingKey);
	    
	    response.status(200);
	    response.body(builder.compact());
	    return response.body();
	};
	
	public static Route fetchRideFare = (Request request, Response response) -> {
		if(ensureUserIsLoggedIn(request)) {
			
			// Create a new ride with status 0
			Ride ride = new Ride(Double.parseDouble(request.queryParams("sLat")), 
					Double.parseDouble(request.queryParams("sLong")), 
					Double.parseDouble(request.queryParams("dLat")), 
					Double.parseDouble(request.queryParams("dLong")));
		}
	};
}