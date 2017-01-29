package termoserver;

import java.io.File;
import java.util.List;
import java.util.Map;

public interface TermoModel {

	public void registerListener(TermoModelListener listener);

	public void saveTemperature(String sensorName, float value, long date);

	public TemperatureUpdate getLatestUpdate(String sensorName);

	public Map<Long, Float> getData(String sensorName);

	public void updateGraphImage(GraphTiming timing, File file);

	public List<String> getSensorNames();

	public void setSensors(List<String> sensorNames);

	/**
	 * Update graph with all sensor in one file
	 * 
	 * @param file
	 */
	public void updateGraphImage(File file);

	public File getAllSensorGraphFile();

	public void setActiveGraph(GraphTiming timing);

	public File getGraph();

	public GraphTiming getActiveGraphTiming();

}
