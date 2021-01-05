package app.ride;

import app.utils.DistanceMetric;

public class Ride{
	
	double sourceLatitude, sourceLongitude, destinationLatitude, destinationLongitude;
	
	/*Status values:
	 * 0 = initiated
	 * 1 = ordered
	 * 2 = accepted
	 * 3 = started
	 * 4 = finished
	 */
	int status;
	double fare;
	
	public Ride(double sourceLatitude, double sourceLongitude, double destinationLatitude, double destinationLongitude)throws Exception {
		this.sourceLatitude = sourceLatitude;
		this.sourceLongitude = sourceLongitude;
		this.destinationLatitude = destinationLatitude;
		this.destinationLongitude = destinationLongitude;
		status = 0;
		updateFare();
	}
	public double getSourceLatitude() {
		return sourceLatitude;
	}
	public double getSourceLongitude() {
		return sourceLongitude;
	}
	public double getDestinationLatitude() {
		return destinationLatitude;
	}
	public double getDestinationLongitude() {
		return destinationLongitude;
	}

	public int getStatus() {
		return status;
	}

	public double getFare() {
		return fare;
	}

	public void setDestination(double latitude, double longitude){
		this.destinationLatitude = latitude;
		this.destinationLongitude = longitude;
		updateFare();
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	private void updateFare() {
		double km = DistanceMetric.getDistance(sourceLatitude, sourceLongitude, destinationLatitude, destinationLongitude);
		km *= 5;
		km += 20;
		km += (5/100)*km;
		this.fare = km;
	}
	 
}