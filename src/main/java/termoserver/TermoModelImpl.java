package termoserver;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.DBMaker.Maker;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

public class TermoModelImpl implements TermoModel, InitializingBean {

	@Autowired
	TermoConfiguration configuration;

	private Set<TermoModelListener> listeners = new HashSet<>();
	private List<String> sensorNames = new ArrayList<>();
	private Map<String, File> graphMap = new HashMap<>();
	private Map<String, Long> latestUpdates = new HashMap<>();

	private DB db;

	private String activeSensor;

	@Override
	public void registerListener(TermoModelListener listener) {
		listeners.add(listener);
	}

	@Override
	public void saveTemperature(String name, float value, long date) {
		getDB(name).put(date, value);
		db.commit();
		latestUpdates.put(name, date);
		notifyListeners(name);
	}

	private void notifyListeners(String sensorName) {
		for (TermoModelListener termoModelListener : listeners) {
			termoModelListener.update();
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		File dbFile = configuration.getDBFile();
		Maker maker = DBMaker.fileDB(dbFile).compressionEnable()
				.fileMmapEnable().closeOnJvmShutdown();
		db = maker.make();
	}

	@Override
	public TemperatureUpdate getLatestUpdate(String sensorName) {
		Map<Long, Float> map = getDB(sensorName);
		Long key = latestUpdates.get(sensorName);
		if (map.size() == 0) {
			return null;
		}

		if (key == null) {
			return new TemperatureUpdate(0l, 0f);
		}
		Float value = map.get(key);

		return new TemperatureUpdate(key, value);
	}

	private Map<Long, Float> getDB(String name) {
		return db.hashMap(name);
	}

	@Override
	public File getGraphFile(String sensorName) {
		return graphMap.get(sensorName);
	}

	@Override
	public void setActiveSensor(String activeSensor) {
		this.activeSensor = activeSensor;
	}

	@Override
	public String getActiveSensor() {
		return activeSensor;
	}

	@Override
	public Map<Long, Float> getData(String sensorName) {
		return getDB(sensorName);
	}

	@Override
	public void updateGraphImage(String sensorName, File file) {
		this.graphMap.put(sensorName, file);
	}

	@Override
	public List<String> getSensorNames() {
		return Collections.unmodifiableList(sensorNames);
	}

	@Override
	public void setSensors(List<String> sensorNames) {
		this.sensorNames.clear();
		this.sensorNames.addAll(sensorNames);
	}

}
