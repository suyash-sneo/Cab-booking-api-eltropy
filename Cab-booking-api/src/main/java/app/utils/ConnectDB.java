package app.utils;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

public class ConnectDB{
	static MongoClient mongoClient;
	static MongoDatabase db = null;
	public static MongoDatabase getDB() {
		if(db!=null) {
			return db;
		}
		mongoClient = new MongoClient();
		db = mongoClient.getDatabase("cab-booking-api");
		return db;
	}
	public static void close() {
		mongoClient.close();
	}
}