package app.ride;

import app.utils.*;
import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;

public class RideData {

	public static String createRideAndGetId(Ride ride) {
		MongoDatabase db = ConnectDB.getDB();
		Gson gson = new Gson();
		String json = gson.toJson(ride);
		Document doc = Document.parse(json);
		db.getCollection("Rides").insertOne(doc);
		return doc.get("_id").toString();
	}

	public static Ride getRide(String id) {
		MongoDatabase db = ConnectDB.getDB();
		BasicDBObject query = new BasicDBObject();
		query.put("_id", new ObjectId(id));
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
	
	public static String getNearestRide(double latitude, double longitude) {
		MongoDatabase db = ConnectDB.getDB();
		FindIterable<Document> rideobj = db.getCollection("Rides").find();
		String minId = "";
		double minDistance = Double.MAX_VALUE;
		for(Document document: rideobj) {
			System.out.println(document.get("_id").toString());
			double distance = Math.abs(latitude - document.getDouble("sourceLatitude")) + Math.abs(longitude - document.getDouble("sourceLongitude"));
			System.out.println(distance);
			if(distance<minDistance) {
				minDistance = distance;
				minId = document.get("_id").toString();
			}
		}
		return minId;
	}

	public static double updateDestination(String id, double latitude, double longitude) {
		// Connect to db
		MongoDatabase db = ConnectDB.getDB();
		// Create a search query
		BasicDBObject query = new BasicDBObject();
		query.put("_id", new ObjectId(id));
		// Extract ride based on query
		FindIterable<Document> rideobj = db.getCollection("Rides").find(query);
		Document ridedoc = rideobj.first();
		ridedoc.remove("_id");
		String json = ridedoc.toJson();
		Gson gson = new Gson();
		Ride ride = gson.fromJson(json, Ride.class);

		// Update the destination
		ride.setDestination(latitude, longitude);
		double fare = ride.getFare();
		json = gson.toJson(ride);
		ridedoc = Document.parse(json);

		// Replace the ride in database
		ridedoc = db.getCollection("Rides").findOneAndReplace(query, ridedoc);
		
		// Return the updated fare
		return fare;
	}

	public static void updateStatus(String id, int status) {
		// Connect to db
		MongoDatabase db = ConnectDB.getDB();
		// Create a search query
		BasicDBObject query = new BasicDBObject();
		query.put("_id", new ObjectId(id));
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
		query.put("_id", new ObjectId(id));
		
		@SuppressWarnings("unused")
		Document ridedoc = db.getCollection("Rides").findOneAndDelete(query);
	}
}