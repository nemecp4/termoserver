package termoserver;

import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.util.Rotation;

public class FreeChartExample extends JFrame {

	private static final long serialVersionUID = 1L;

	public FreeChartExample(String applicationTitle, String chartTitle) {
		super(applicationTitle);
		// This will create the dataset
		PieDataset dataset = createDataset();
		// based on the dataset we create the chart
		JFreeChart chart = createChart(dataset, chartTitle);
		// we put the chart into a panel
		ChartPanel chartPanel = new ChartPanel(chart);
		// default size
		chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
		// add it to our application
		setContentPane(chartPanel);

	}

	/**
	 * Creates a sample dataset
	 */

	private PieDataset createDataset() {
		DefaultPieDataset result = new DefaultPieDataset();
		result.setValue("Linux", 29);
		result.setValue("Mac", 20);
		result.setValue("Windows", 51);
		return result;

	}

	/**
	 * Creates a chart
	 */

	private JFreeChart createChart(PieDataset dataset, String title) {

		JFreeChart chart = ChartFactory.createPieChart3D(title, // chart title
				dataset, // data
				true, // include legend
				true, false);

		PiePlot3D plot = (PiePlot3D) chart.getPlot();
		plot.setStartAngle(290);
		plot.setDirection(Rotation.CLOCKWISE);
		plot.setForegroundAlpha(0.5f);
		return chart;

	}

	public static void main(String[] args) {

		FreeChartExample demo = new FreeChartExample("Comparison",
				"Which operating system are you using?");
		demo.pack();
		demo.setVisible(true);
		
		 /* Define some XY Data series for the chart */
        XYSeries team1_xy_data = new XYSeries("Team 1");
        team1_xy_data.add(1990, 45);
        team1_xy_data.add(1991, 16);
        team1_xy_data.add(1992, 80);
        team1_xy_data.add(1993, 1);
        team1_xy_data.add(1994, 6);
        
        XYSeries team2_xy_data = new XYSeries("Team 2");
        team2_xy_data.add(1990, 2);
        team2_xy_data.add(1991, 10);
        team2_xy_data.add(1992, 60);
        team2_xy_data.add(1993, 60);
        team2_xy_data.add(1994, 18);
        
        XYSeries team3_xy_data = new XYSeries("Team 3");
        team3_xy_data.add(1990, 15);
        team3_xy_data.add(1991, 5);
        team3_xy_data.add(1992, 14);
        team3_xy_data.add(1993, 18);
        team3_xy_data.add(1994, 25);
        
        /* Add all XYSeries to XYSeriesCollection */
        //XYSeriesCollection implements XYDataset
        XYSeriesCollection my_data_series= new XYSeriesCollection();
        // add series using addSeries method
        my_data_series.addSeries(team1_xy_data);
        my_data_series.addSeries(team2_xy_data);
        my_data_series.addSeries(team3_xy_data);
        
        
        //Use createXYLineChart to create the chart
        JFreeChart XYLineChart=ChartFactory.createXYLineChart("Team - Number of Wins","Year","Win Count",my_data_series,PlotOrientation.VERTICAL,true,true,false);
  
        /* Step -3 : Write line chart to a file */               
         int width=640; /* Width of the image */
         int height=480; /* Height of the image */                
         File XYlineChart=new File("xy_line_Chart_example.png");              
         try {
			ChartUtilities.saveChartAsPNG(XYlineChart,XYLineChart,width,height);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

	}
}
