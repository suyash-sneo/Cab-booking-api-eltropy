package app.ride;

import app.utils.*;
import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class RideData {

	public static void createRide(Ride ride) {
		MongoDatabase db = ConnectDB.getDB();
		Gson gson = new Gson();
		String json = gson.toJson(ride);
		Document doc = Document.parse(json);
		db.getCollection("Rides").insertOne(doc);
	}

	public static Ride getRide(String id) {
		MongoDatabase db = ConnectDB.getDB();
		BasicDBObject query = new BasicDBObject();
		query.put("_id", id);
		FindIterable<Document> rideobj = db.getCollection("Rides").find(query);
		Document ridedoc = rideobj.first();
		if (ridedoc != null) {
			ridedoc.remove("_id");
			String json = ridedoc.toJson();
			Gson gson = new Gson();
			return gson.fromJson(json, Ride.class);
		}
		return null;
	}

	public static void updateDestination(String id, double latitude, double longitude) {
		// Connect to db
		MongoDatabase db = ConnectDB.getDB();
		// Create a search query
		BasicDBObject query = new BasicDBObject();
		query.put("_id", id);
		// Extract ride based on query
		FindIterable<Document> rideobj = db.getCollection("Rides").find(query);
		Document ridedoc = rideobj.first();
		ridedoc.remove("_id");
		String json = ridedoc.toJson();
		Gson gson = new Gson();
		Ride ride = gson.fromJson(json, Ride.class);

		// Update the destination
		ride.setDestination(latitude, longitude);
		json = gson.toJson(ride);
		ridedoc = Document.parse(json);

		// Replace the ride in database
		ridedoc = db.getCollection("Rides").findOneAndReplace(query, ridedoc);
	}

	public static void updateStatus(String id, int status) {
		// Connect to db
		MongoDatabase db = ConnectDB.getDB();
		// Create a search query
		BasicDBObject query = new BasicDBObject();
		query.put("_id", id);
		// Extract ride based on query
		FindIterable<Document> rideobj = db.getCollection("Rides").find(query);
		Document ridedoc = rideobj.first();
		ridedoc.put("status", status);
		// Replace the ride in database
		ridedoc = db.getCollection("Rides").findOneAndReplace(query, ridedoc);
	}
	
	public static void finishRide(String id) {
		MongoDatabase db = ConnectDB.getDB();
		BasicDBObject query = new BasicDBObject();
		query.put("_id", id);
		
		@SuppressWarnings("unused")
		Document ridedoc = db.getCollection("Rides").findOneAndDelete(query);
	}
}