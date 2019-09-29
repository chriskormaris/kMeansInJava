package kMeans;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import plot.LatLong;
import plot.PlotClusters;
import plot.Utilities;

public class KMeans {
	
	Random r;
	
	private LatLong[] means;
	private int[] dataClusters;

	public LatLong[] getMeans() {
		return means;
	}

	public void setMeans(LatLong[] means) {
		this.means = means;
	}

	public int[] getDataClusters() {
		return dataClusters;
	}

	public void setDataClusters(int[] dataClusters) {
		this.dataClusters = dataClusters;
	}

	public void kMeans(int K, List<LatLong> latLongs,
			int maxiters, double tolerance) {
		Random rand = new Random();
		int N = latLongs.size();
		int D = 2;
		
		means = new LatLong[K];
		dataClusters = new int[N];

		// First, initialize the means M, somewhat randomly.
		for (int k=0; k<K; k++) {
			for (int d=0; d<D; d++) {
				double Md = 0;
				for (int i=0; i<N; i++) {
					if (d==0)
						Md += latLongs.get(i).getLongitude();
					else if (d==1)
						Md += latLongs.get(i).getLatitude();
				}
				Md /= N;
				if (d == 0) {
					means[k] = new LatLong(k);
					means[k].setLongitude(Md + rand.nextGaussian());
				} else if (d == 1) {
					means[k].setLatitude(Md + rand.nextGaussian());
				}
				// System.out.println(M[k]);
			}
		}
		
		System.out.println("Minit: ");
		for (int k=0; k<K; k++) {
			for (int d=0; d<2; d++) {
				if (d == 0)
					System.out.print(means[k].getLongitude() + " ");
				else if (d == 1)
					System.out.print(means[k].getLatitude() + " ");
			}
			System.out.println();
		}
		System.out.println();

        double Jold = Integer.MAX_VALUE; 
		int it = 0;
		while (it < maxiters) {
			
		    // STEP 1 -- passing data to clusters 
			double[] minDistances = new double[N];
			for (int n=0; n<N; n++) {
				LatLong latLong = latLongs.get(n);
				
				double[] latLongDouble = new double[2];
				latLongDouble[0] = latLong.getLongitude();
				latLongDouble[1] = latLong.getLatitude();
				
				double[] distances = new double[K];
				for (int k=0; k<K; k++) {
					double[] Mk = new double[D];
					Mk[0] = means[k].getLongitude();
					Mk[1] = means[k].getLatitude();
					double distance = Utilities.euclideanDistance(Mk, latLongDouble);
					distances[k] = distance;
				}
				
				double minDistance = Integer.MAX_VALUE;
				for (int k=0; k<K; k++) {
					if (distances[k] <= minDistance) {
						minDistance = distances[k];
						minDistances[n] = minDistance;
						dataClusters[n] = k;
					}
				}
				
			}
				
		    // STEP 2 -- update the mean centers 
			for (int k=0; k<K; k++) {
	            double[][] sum = new double[K][D];
	            int number_of_ks = 0;
				for (int n=0; n<N; n++) {
					LatLong latLong = latLongs.get(n);

	                double[] xn = new double[2];
	                xn[0] = latLong.getLongitude();
	                xn[1] = latLong.getLatitude();
	                
	                if (dataClusters[n] == k) {
	                	sum[k][0] += xn[0];
	                	sum[k][1] += xn[1];

	                    number_of_ks = number_of_ks + 1;
					}
				}
				// be careful not to divide by zero
				if (number_of_ks != 0) {
					means[k].setLongitude(sum[k][0] / number_of_ks);
					means[k].setLatitude(sum[k][1] / number_of_ks);
				}
			}
			
			// Calculate the cost function J.
	        double sum = 0;
			for (int n=0; n<N; n++) {
				double minDistance = minDistances[n]; 
				sum += minDistance;
			}
	        double J = sum;
	        
	        // print the value of objective function (!!YOU NEED TO COMPUTE THE COST FUNCTION J!!!)
	        System.out.printf("Iteration %4d  Cost function %11.6f\n", it, J); 
	        
	        // STEP 3 - check for convergence 
	        // if ((Math.abs(J - Jold) < tolerance) || (it > maxiters)) {
	        if ((Math.abs(J - Jold) < tolerance)) {
	            break;
	        }
	        Jold = J;
	        
	        it++;
		}
		
	}

	public static void main(String[] args) {	
		/*** READ FROM FILE COORDINATES ***/
		List<LatLong> latLongs = new ArrayList<LatLong>();
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

		KMeans km = new KMeans();
		int K = 4;
		int iterations = 100;
        double tolerance = 1e-6; 
		km.kMeans(K, latLongs, iterations, tolerance);
		System.out.println();

		System.out.println("M: ");
		for (int k=0; k<K; k++) {
			for (int d=0; d<2; d++) {
				if (d == 0)
					System.out.print(km.getMeans()[k].getLongitude() + " ");
				else if (d == 1)
					System.out.print(km.getMeans()[k].getLatitude() + " ");
			}
			System.out.println();
		}
		System.out.println();
		
		List<LatLong> meansList = Arrays.asList(km.getMeans());
		
		List<LatLong> topLeftAndTopRight = Utilities.findTopLeftAndTopRight(meansList);
		LatLong topLeft = topLeftAndTopRight.get(0);
		LatLong topRight = topLeftAndTopRight.get(1);
		
		List<LatLong> bottomLeftAndBottomRight = Utilities.findBottomLeftAndBottomRight(meansList);
		LatLong bottomLeft = bottomLeftAndBottomRight.get(0);
		LatLong bottomRight = bottomLeftAndBottomRight.get(1);
		
		List<LatLong> meansSquare = Arrays.asList(topLeft, topRight, bottomRight, bottomLeft, topLeft); 

		PlotClusters myplot = new PlotClusters(latLongs, meansSquare, km.getDataClusters());
		myplot.showInFrame();
	}
	
}
