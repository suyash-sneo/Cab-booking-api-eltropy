package app.utils;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import org.json.JSONObject;

public class DistanceMetric{
	
	private static String host = "http://router.project-osrm.org/table/v1/driving/";
	private static String coordinates;
	private static String params = "sources=0&destinations=1&annotations=distance";
	
	public static double getDistance(double sourceLatitude, double sourceLongitude, double destinationLatitude, double destinationLongitude) {
		
		coordinates = sourceLatitude+","+sourceLongitude+";"+destinationLatitude+","+destinationLongitude;
		try {
			HttpResponse<JsonNode> response = Unirest.get(host+coordinates+"?"+params).asJson();
			JSONObject obj = new JSONObject(response.getBody().toString());
			double distanceMeters = obj.getJSONArray("distances").getJSONArray(0).getDouble(0);
			return distanceMeters/1000;
		}
		catch(Exception e) {
			System.out.println("Failed to fetch or parse response from router.project-osrm.org");
			return -1;
		}
	}
	
//	public static void main(String args[]) {
//		double distance = getDistance(13.388860,52.517037,13.397634,52.529407);
//		System.out.println(distance);
//	}
}