package app.user;

import app.utils.*;
import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class UserData{
	
	public static void createUser(User user) {
		MongoDatabase db = ConnectDB.getDB();
		Gson gson = new Gson();
		String json = gson.toJson(user);
		Document doc = Document.parse(json);
		db.getCollection("Users").insertOne(doc);
	}
	
	public static User getUser(String username) {
		MongoDatabase db = ConnectDB.getDB();
		BasicDBObject query = new BasicDBObject();
		query.put("username", username);
		FindIterable<Document> userobj = db.getCollection("Users").find(query);
		Document userdoc = userobj.first();
		if(userdoc!=null) {
			userdoc.remove("_id");
			String json = userdoc.toJson();
			Gson gson = new Gson();
			return gson.fromJson(json, User.class);
		}
		return null;
	}
	
	public static void setRide(String username, String rideId) {
		MongoDatabase db = ConnectDB.getDB();
		BasicDBObject query = new BasicDBObject();
		query.put("username", username);
		FindIterable<Document> userobj = db.getCollection("Users").find(query);
		Document userdoc = userobj.first();
		userdoc.put("currentRideId", rideId);
		userdoc = db.getCollection("Users").findOneAndReplace(query, userdoc);
	}
	
	public static void finishRide(String username) {
		MongoDatabase db = ConnectDB.getDB();
		BasicDBObject query = new BasicDBObject();
		query.put("username", username);
		FindIterable<Document> userobj = db.getCollection("Users").find(query);
		Document userdoc = userobj.first();
		userdoc.put("currentRideId", "");
		userdoc = db.getCollection("Users").findOneAndReplace(query, userdoc);
	}
}