package app.utils;

public class RideWithFare{
	
	public String id;
	public double fare;
	
	public RideWithFare(String id, double fare) {
		this.id = id;
		this.fare = fare;
	}
	
	public String getId() {
		return this.id;
	}
	public double getFare() {
		return this.fare;
	}
}