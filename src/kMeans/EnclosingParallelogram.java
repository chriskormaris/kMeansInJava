package kMeans;
 
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import plot.LatLong;
import plot.PlotPointsAndPolygon;


public class EnclosingParallelogram {
   
    static LatLong topLeft;
    static LatLong topRight;
    static LatLong bottomLeft;
    static LatLong bottomRight;
   
    public static void main(String[] args) {
        List<LatLong> latLongs = new ArrayList<LatLong>();
 
        /*** FIXED POINT COORDINATES ***/
        /*
        LatLong latLong1 = new LatLong(23, 37, 1);
        LatLong latLong2 = new LatLong(24, 37, 2);  // false positive
        LatLong latLong3 = new LatLong(23, 38, 3);
        LatLong latLong4 = new LatLong(24, 38, 4);
        LatLong latLong5 = new LatLong(23.5, 38, 5);
        LatLong latLong6 = new LatLong(23.5, 37, 6);  // false positive
        LatLong latLong7 = new LatLong(25, 37.5, 7);
 
//      latLongs.addAll(Arrays.asList(latLong1, latLong2, latLong3, latLong4));
        latLongs.addAll(Arrays.asList(latLong1, latLong2, latLong3, latLong4, latLong5, latLong6, latLong7));
        */
       
        /*** RANDOM COORDINATES ***/
        int num_lat_longs = 100;
        for (int i=0; i<num_lat_longs; i++) {
            LatLong latLong = new LatLong().getRandomLatLong(25, 22, 39, 36);
            // System.out.println(latLong);
            latLongs.add(latLong);
        }
       
        /*** READ FROM FILE COORDINATES ***/
        /*
		FileManager fm = new FileManager();
		fm.parseFile("coordinates_file/points.txt");
		latLongs = fm.getLatLongs();
		*/
               
        EnclosingParallelogram es = new EnclosingParallelogram();
        List<LatLong> quadrangle = es.getEnclosingParallelogram(latLongs);
       
        PlotPointsAndPolygon myplot = new PlotPointsAndPolygon(latLongs, quadrangle);
        myplot.showInFrame();
       
    }
 
    private List<LatLong> getEnclosingParallelogram(List<LatLong> latLongs) {
       
        double maxLongitude = -90;
        double minLongitude = 90;
        double maxLatitude = -90;
        double minLatitude = 90;
       
        for (LatLong latLong: latLongs) {
            if (maxLongitude < latLong.getLongitude())
                maxLongitude = latLong.getLongitude();
            if (minLongitude > latLong.getLongitude())
                minLongitude = latLong.getLongitude();
            if (maxLatitude < latLong.getLatitude())
                maxLatitude = latLong.getLatitude();
            if (minLatitude > latLong.getLatitude())
                minLatitude = latLong.getLatitude();
        }
 
        topLeft = new LatLong(minLongitude, maxLatitude, 1);
        topRight = new LatLong(maxLongitude, maxLatitude, 2);
        bottomLeft = new LatLong(minLongitude, minLatitude, 3);
        bottomRight = new LatLong(maxLongitude, minLatitude, 4);
       
        List<LatLong> parallelogram = Arrays.asList(topLeft, topRight, bottomRight,
                    bottomLeft, topLeft);
       
        return parallelogram;
    }
   
}
