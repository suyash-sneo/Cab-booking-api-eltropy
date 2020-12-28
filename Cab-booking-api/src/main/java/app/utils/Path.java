package app.utils;

public class Path{
	
	public static class Web{
		
		public static final String USER_LOGIN = "/user/login/";
		public static final String USER_RIDE = "/user/ride/";
		public static final String DRIVER_LOGIN = "/driver/login/";
		public static final String DRIVER_RIDE = "/driver/ride/";
		
		public static String getUserLogin() {
			return USER_LOGIN;
		}
		public static String getUserRide() {
			return USER_RIDE;
		}
		public static String getDriverLogin() {
			return DRIVER_LOGIN;
		}
		public static String getDriverRide() {
			return DRIVER_RIDE;
		}
	}
	
	public static class Template{
		
		public final static String USER_LOGIN = "/velocity/login/userLogin.vm";
		
	}
}