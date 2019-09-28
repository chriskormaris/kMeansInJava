package plot;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.RadialGradientPaint;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.erichseifert.gral.data.DataSeries;
import de.erichseifert.gral.data.DataTable;
import de.erichseifert.gral.graphics.Insets2D;
import de.erichseifert.gral.graphics.Label;
import de.erichseifert.gral.plots.XYPlot;
import de.erichseifert.gral.plots.axes.AxisRenderer;
import de.erichseifert.gral.plots.lines.DefaultLineRenderer2D;
import de.erichseifert.gral.plots.lines.LineRenderer;
import de.erichseifert.gral.plots.points.DefaultPointRenderer2D;
import de.erichseifert.gral.plots.points.PointRenderer;
import de.erichseifert.gral.ui.InteractivePanel;


public class PlotClusters extends Panel {
	
	/** Version id for serialization. **/
	private static final long serialVersionUID = -5263057758564264676L;

	/** Instance to generate random data values. **/
	private static final Random random = new Random();
	
	private static int K;
	
	@SuppressWarnings("unchecked")
	public PlotClusters(List<LatLong> points, List<LatLong> means, int[] dataClusters) {
		int N = points.size();
		K = means.size() - 1;
		
	    // Create data table
	    DataTable pointData = new DataTable(Double.class, Double.class);
	    pointData.setName("Points");
	    
	    DataTable meanData = new DataTable(Double.class, Double.class);
	    meanData.setName("Means");

	    // Create new XY Plots
	    XYPlot xyPlot = new XYPlot(pointData, meanData);
	    
	    for (LatLong point: points) {
	    	pointData.add(point.getLongitude(), point.getLatitude());
	    }
	    
	    for (LatLong mean: means) {
	    	meanData.add(mean.getLongitude(), mean.getLatitude());
	    }

	    xyPlot.getLegend().clear();
	    
	    // Create different data series for each point.
	    // This is useful for the legend and for assigning different colors.
	    List<DataSeries> ds_points = new ArrayList<DataSeries>();
	    int iterations = 0;
	    for (LatLong point: points) {
	    	DataTable tempData = new DataTable(Double.class, Double.class);
	    	tempData.add(point.getLongitude(), point.getLatitude());
	     	if (iterations < points.size()-1) {
	 		    DataSeries ds = new DataSeries("Point " + point.getId(), tempData);
//	 		    plot.add(ds);
	 		    ds_points.add(ds);
	     	}
		    iterations++;
	    }
	    
	    List<DataSeries> ds_means = new ArrayList<DataSeries>();
	    iterations = 0;
	    for (int k=0; k<K; k++) {
	    	DataTable tempData = new DataTable(Double.class, Double.class);
	    	tempData.add(means.get(k).getLongitude(), means.get(k).getLatitude());
	     	if (iterations < points.size()-1) {
	 		    DataSeries ds = new DataSeries("Mean " + (k+1), tempData);
//	 		    plot.add(ds);
	 		    ds_means.add(ds);
	     	}
		    iterations++;
	    }
 		
	    // Sort point data series by name.
	    ds_points.sort(new DataSeriesComparator());
 		for (DataSeries ds: ds_points) {
 			xyPlot.add(ds);
 		}
	    
	    ds_means.sort(new DataSeriesComparator());
 		for (DataSeries ds: ds_means) {
 			xyPlot.add(ds);
 		}
	    
	    xyPlot.setLegendVisible(false);
	    
	    // Format plots
 		xyPlot.setInsets(new Insets2D.Double(20.0, 40.0, 40.0, 40.0));
 		xyPlot.setBackground(Color.WHITE);
 		xyPlot.getTitle().setText(getDescription());
 		
 		// Format plot area
 		xyPlot.getPlotArea().setBackground(new RadialGradientPaint(
 			new Point2D.Double(0.5, 0.5),
 			0.75f,
 			new float[] { 0.6f, 0.8f, 1.0f },
 			new Color[] { new Color(0, 0, 0, 0), new Color(0, 0, 0, 32), new Color(0, 0, 0, 128) }
 		));
 		xyPlot.getPlotArea().setBorderStroke(null);

 		// Format axes
 		AxisRenderer axisRendererX = xyPlot.getAxisRenderer(XYPlot.AXIS_X);
 		AxisRenderer axisRendererY = xyPlot.getAxisRenderer(XYPlot.AXIS_Y);
 		axisRendererX.setLabel(new Label("Longitude"));
 		xyPlot.setAxisRenderer(XYPlot.AXIS_X, axisRendererX);
 		
 		// Custom tick labels
 		/*
 		Map<Double, String> labels = new HashMap<Double, String>();
 		labels.put(2.0, "Two");
 		labels.put(1.5, "OnePointFive");
 		axisRendererX.setCustomTicks(labels);
 		*/
 		
 		// Custom stroke for the Y axis
 		BasicStroke stroke = new BasicStroke(2f);
 		axisRendererX.setShapeStroke(stroke);
 		Label linearAxisLabel = new Label("Latitude");
 		linearAxisLabel.setRotation(90);
 		axisRendererY.setLabel(linearAxisLabel);
 		
 		// Change intersection point of Y axis
 		// axisRendererY.setIntersection(1.0);
 		
 		// Change tick spacing
 		// axisRendererX.setTickSpacing(0.1);
 		// axisRendererY.setTickSpacing(0.1);
 		
 		// Format data lines.
 		// This batch of code connects lines together.
 		LineRenderer lineRenderer = new DefaultLineRenderer2D();
 		lineRenderer.setColor(COLOR1);
 		lineRenderer.setStroke(new BasicStroke(
 				3.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
 				10.0f, new float[] {3f, 6f}, 0.0f));
		xyPlot.setLineRenderers(meanData, lineRenderer);

 		// Custom gaps for points
 		lineRenderer.setGap(2.0);
 		lineRenderer.setGapRounded(true); 	
 		
 		// Format rendering of data points.
 		// This batch of code changes the color of the points randomly.
 		/*
 		for (DataSeries ds: ds_means) {
 	 		PointRenderer defaultPointRenderer = new DefaultPointRenderer2D();
 	 		
 	 		// Java 'Color' class takes 3 floats, from 0 to 1.
 	 		float r = random.nextFloat();
 	 		float g = random.nextFloat();
 	 		float b = random.nextFloat();
			Color randomColor = new Color(r, g, b);
 	 		
 	 		defaultPointRenderer.setColor(randomColor);
 			xyPlot.setPointRenderers(ds, defaultPointRenderer);
	    }
	    */
 		
 		Color[] clusterColors = new Color[K];
	    for (int k=0; k<K; k++) {
	 		// Java 'Color' class takes 3 floats, from 0 to 1.
	 		float r = random.nextFloat();
	 		float g = random.nextFloat();
	 		float b = random.nextFloat();
	 		clusterColors[k] = new Color(r, g, b);
	    }
 		
 		for (int n=0; n<N-1; n++) {
 			DataSeries ds = ds_points.get(n);
 			int cluster = dataClusters[n];
 			Color clusterColor = clusterColors[cluster];
 			// System.out.println(clusterColor);
 	 		PointRenderer defaultPointRenderer = new DefaultPointRenderer2D();
 	 		
 	 		defaultPointRenderer.setColor(clusterColor);
 			xyPlot.setPointRenderers(ds, defaultPointRenderer);
	    }

 		// Add plot to Swing component
 		add(new InteractivePanel(xyPlot), BorderLayout.CENTER);
 		
 		// Change zoom level
 		xyPlot.getNavigator().setZoom(0.6f);
 		
 	}
	
 	@Override
 	public String getTitle() {
 		return "K-Means Clustering Plot for K=" + K;
 	}

 	@Override
 	public String getDescription() {
 		return "K-Means Clustering Plot for K=" + K;
 	}

}
