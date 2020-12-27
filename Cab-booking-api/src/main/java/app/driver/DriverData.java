package app.driver;

import app.utils.*;
import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class DriverData{
	
	public static void createDriver(Driver driver) {
		MongoDatabase db = ConnectDB.getDB();
		Gson gson = new Gson();
		String json = gson.toJson(driver);
		Document doc = Document.parse(json);
		db.getCollection("Drivers").insertOne(doc);
	}
	
	public static Driver getDriver(String username) {
		MongoDatabase db = ConnectDB.getDB();
		BasicDBObject query = new BasicDBObject();
		query.put("username", username);
		FindIterable<Document> driverobj = db.getCollection("Drivers").find(query);
		Document driverdoc = driverobj.first();
		if(driverdoc!=null) {
			driverdoc.remove("_id");
			String json = driverdoc.toJson();
			Gson gson = new Gson();
			return gson.fromJson(json, Driver.class);
		}
		return null;
	}
	
	public static void updateLocation(String username, double latitude, double longitude) {
		MongoDatabase db = ConnectDB.getDB();
		BasicDBObject query = new BasicDBObject();
		query.put("username", username);
		FindIterable<Document> driverobj = db.getCollection("Drivers").find(query);
		Document driverdoc = driverobj.first();
		driverdoc.put("latitude", latitude);
		driverdoc.put("longitude", longitude);
		driverdoc = db.getCollection("Drivers").findOneAndReplace(query, driverdoc);
	}
	
	public static void setRide(String username, String rideId) {
		MongoDatabase db = ConnectDB.getDB();
		BasicDBObject query = new BasicDBObject();
		query.put("username", username);
		FindIterable<Document> driverobj = db.getCollection("Drivers").find(query);
		Document driverdoc = driverobj.first();
		driverdoc.put("currentRideId", rideId);
		driverdoc = db.getCollection("Drivers").findOneAndReplace(query, driverdoc);
	}
	
	public static void finishRide(String username) {
		MongoDatabase db = ConnectDB.getDB();
		BasicDBObject query = new BasicDBObject();
		query.put("username", username);
		FindIterable<Document> driverobj = db.getCollection("Drivers").find(query);
		Document driverdoc = driverobj.first();
		driverdoc.put("currentRideId", "");
		driverdoc = db.getCollection("Drivers").findOneAndReplace(query, driverdoc);
	}
}