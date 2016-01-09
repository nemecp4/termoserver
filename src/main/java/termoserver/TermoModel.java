package termoserver;

import java.io.File;
import java.util.List;
import java.util.Map;

public interface TermoModel {

	public void registerListener(TermoModelListener listener);

	public void saveTemperature(String sensorName, float value, long date);

	public TemperatureUpdate getLatestUpdate(String sensorName);

	public File getGraphFile(String sensorName);

	public void setActiveSensor(String sensorName);

	public String getActiveSensor();

	public Map<Long, Float> getData(String sensorName);

	public void updateGraphImage(String sensorName, File file);

	public List<String> getSensorNames();

	public void setSensors(List<String> sensorNames);

}
