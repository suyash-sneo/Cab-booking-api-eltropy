package app;

import app.utils.*;
import app.user.*;
import app.driver.*;
import static spark.Spark.*;
import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

public class Application{
	
	public static void main(String args[]) {
		
		
		//Configure the server port
		port(3000);
		staticFiles.location("/public");
		

        // Set up before-filters
        before("*", Filters.addTrailingSlashes);
        
        //Set up routes
        // get(Path.Web.USER_LOGIN, UserController.serveLoginPage);
        post(Path.Web.USER_LOGIN, UserController.handleLoginPost);
        get(Path.Web.USER_RIDE, UserController.fetchRideFare);
        /*
        post(Path.Web.USER_RIDE, UserController.bookRide);
        put(Path.Web.USER_RIDE, UserController.updateRideDestination);
        delete(Path.Web.USER_RIDE, UserController.finishRide);
        
        // get(Path.Web.DRIVER_LOGIN, DriverController.serveLoginPage);
        post(Path.Web.DRIVER_LOGIN, DriverController.handleLoginPost);
        get(Path.Web.DRIVER_RIDE, DriverController.fetchAllRidesSorted);
        post(Path.Web.DRIVER_RIDE, DriverController.acceptRide);
        */
        //Close MongoDB connection
        //ConnectDB.close();
	}
	
}