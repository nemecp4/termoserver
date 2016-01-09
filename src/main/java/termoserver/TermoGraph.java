package termoserver;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Map;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.SeriesException;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.springframework.beans.factory.annotation.Autowired;

public class TermoGraph {
	private SimpleDateFormat formater = new SimpleDateFormat("HH:mm:ss");

	private int graphSize = 10;
	@Autowired
	private TermoModel model;

	public File generateGraph(String name) {
		Map<Long, Float> data = model.getData(name);
		int counter = 0;

		TimeSeries dataset = new TimeSeries("Temperature for " + name);
		for (Long key : data.keySet()) {
			Float value = data.get(key);
			Second s = new Second(new Date(key));
			try {
				dataset.add(s, new Double(value));
			} catch (SeriesException se) {
				// probably already added
			}
		}
		TimeSeriesCollection seriesCollection = new TimeSeriesCollection();
		seriesCollection.addSeries(dataset);

		JFreeChart chart = ChartFactory.createTimeSeriesChart(
				String.format("Teplota - %s", name), "Minutes",
				"Temperature [C]", seriesCollection, false, false, false);

		/* Step -3 : Write line chart to a file */
		int width = 640; /* Width of the image */
		int height = 480; /* Height of the image */
		File file = new File(name + ".png");
		try {
			ChartUtilities.saveChartAsPNG(file, chart, width, height);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return file;
	}
}
