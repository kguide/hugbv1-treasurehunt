package hi.android.treasure.data;

public class GpsTool {
	
	
	/**
	 * calculates distance between 2 gps points
	 * 
	 * @param la1 : latitude point 1
	 * @param lo1 : longitude point 1
	 * @param la2 : latitude point 2	 
	 * @param lo2 : longitude point 2
	 * @return distance between those (la1,lo1) and (la2,lo2)
	 */
	public static double getDistance(double la1, double lo1, double la2, double lo2) {
		  double latAbsolute = Math.sin(deg2rad(la1)) * Math.sin(deg2rad(la2));
		  double theta = Math.cos(deg2rad(lo1 - lo2));
		  double distance = Math.acos(latAbsolute +  Math.cos(deg2rad(la1)) * Math.cos(deg2rad(la2)) * theta);
		  distance = rad2deg(distance);
		  
		  double nauticalToMeters = 1853.15962;  //convert nautical mile to meters;
		  distance = distance * 60 * nauticalToMeters;  //degrees * min * nautical
		  return distance;
		}

	
	/**
	 * Converts radians to degrees
	 * 
	 * @param radians
	 * @return degrees for radians
	 */
	public static double rad2deg(double rad) {
		  return (rad * 180.0 / Math.PI);
		}
	/**
	 * Converts degrees to radians
	 * 
	 * @param degrees
	 * @return  radians for degrees
	 */
	public static double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}




}
