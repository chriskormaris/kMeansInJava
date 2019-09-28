package plot;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Utilities {
	
	// find the points that have max latitude
	public static List<LatLong> findTopLeftAndTopRight(List<LatLong> points) {
 
		LatLong topLeft;
		LatLong topRight;
		LatLong maxLatPoint = null;
    	LatLong secondMaxLatPoint = null;
        double maxLat = -90;
        double secondMaxLat = -90;
 
        // set points based on max latitudes
        for (LatLong point: points) {
            if (point.getLatitude() >= maxLat) {
                maxLat = point.getLatitude();
                maxLatPoint = point;
            }
        }
        for (LatLong point: points) {
            if (point.getLatitude() >= secondMaxLat && maxLatPoint.getLongitude() != point.getLongitude()) {
                secondMaxLat = point.getLatitude();
                secondMaxLatPoint = point;
            }
        }

        // change point based on min longitude
        if (maxLatPoint.getLongitude() <= secondMaxLatPoint.getLongitude()) {
        	topLeft = maxLatPoint;
        	topRight = secondMaxLatPoint;
        } else {
        	topLeft = secondMaxLatPoint;
        	topRight = maxLatPoint;
        }
        
        List<LatLong> topLeftAndTopRight = new ArrayList<LatLong>();
        topLeftAndTopRight.addAll(Arrays.asList(topLeft, topRight));
        // System.out.println("topLeft: " + bottomLeft);
        // System.out.println("topRight: " + bottomRight);

        return topLeftAndTopRight;
    }
    
    // find the points that have min latitude
	public static List<LatLong> findBottomLeftAndBottomRight(List<LatLong> points) {
 
		LatLong bottomLeft;
		LatLong bottomRight;
    	LatLong minLatPoint = null;
    	LatLong secondMinLatPoint = null;
        double minLat = 90;
        double secondMinLat = 90;
 
        // set point based on min latitude
        for (LatLong point: points) {
            if (point.getLatitude() <= minLat) {
                minLat = point.getLatitude();
                minLatPoint = point;
            }
        }
        for (LatLong point: points) {
            if (point.getLatitude() <= secondMinLat && minLatPoint.getLongitude() != point.getLongitude()) {
                secondMinLat = point.getLatitude();
                secondMinLatPoint = point;
            }
        }

        // change point based on min longitude
        if (minLatPoint.getLongitude() <= secondMinLatPoint.getLongitude()) {
        	bottomLeft = minLatPoint;
        	bottomRight = secondMinLatPoint;
        } else {
        	bottomLeft = secondMinLatPoint;
        	bottomRight = minLatPoint;
        }
        
        List<LatLong> bottomLeftBottomRight = new ArrayList<LatLong>();
        bottomLeftBottomRight.addAll(Arrays.asList(bottomLeft, bottomRight));
        // System.out.println("topLeft: " + bottomLeft);
        // System.out.println("topRight: " + bottomRight);

        return bottomLeftBottomRight;
    }
	
	public static double euclideanDistance(double[] a, double[] b) 
			throws ArrayIndexOutOfBoundsException {
		if (a.length != b.length) 
			throw new ArrayIndexOutOfBoundsException();
		double sum = 0;
		for (int i=0; i<a.length; i++) {
			sum += Math.pow(a[i] - b[i], 2);
		}
		return Math.sqrt(sum);
	}
	
	public static double euclideanNorm(double[] x) {
		double sum = 0;
		for (int i=0; i<x.length; i++) {
			sum += Math.pow(x[i], 2);
		}
		return Math.sqrt(sum);
	}
	
}
