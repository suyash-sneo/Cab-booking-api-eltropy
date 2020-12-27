package app;

import app.driver.Driver;
import com.google.gson.Gson;
import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class MongodbTest {
  public static void main(String args[]) {
	/*
    MongoClient mongoClient = new MongoClient(); // Connect with default settings i.e. localhost:27017
    MongoDatabase db = mongoClient.getDatabase("test"); // Get database "test". Creates one if it doesn't exist
    Driver driver = new Driver("Devesh", "10", "10Dev", new Coordinates(10.0, 22.5));
    // Deserialize object to json string
    Gson gson = new Gson();
    String json = gson.toJson(driver);
    // Parse to bson document and insert
    Document doc = Document.parse(json);
    db.getCollection("NameColl").insertOne(doc);
    mongoClient.close();
    /*
    // Retrieve to ensure object was inserted
    FindIterable<Document> iterable = db.getCollection("NameColl").find();
    iterable.forEach(new Block<Document>() {
      @Override
      public void apply(final Document document) {
        System.out.println(document); // See below to convert document back to Employee
      }
    });
    */

  }
}
