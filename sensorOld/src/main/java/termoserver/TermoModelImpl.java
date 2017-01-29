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
import org.mapdb.HTreeMap;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TermoModelImpl implements TermoModel, InitializingBean {

	@Autowired
	TermoConfiguration configuration;

	private Set<TermoModelListener> listeners = new HashSet<>();
	private List<String> sensorNames = new ArrayList<>();
	private Map<GraphTiming, File> graphMap = new HashMap<>();
	private Map<String, Long> latestUpdates = new HashMap<>();

	private DB db;

	private File allSensorGraph = null;

	private GraphTiming activeGraphTiming = GraphTiming.MINUT_30;

	@Override
	public void registerListener(TermoModelListener listener) {
		listeners.add(listener);
	}

	@SuppressWarnings("unchecked")
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
		Maker maker = DBMaker.fileDB(dbFile).fileMmapEnable().closeOnJvmShutdown();
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

	private Map getDB(String name) {
		return db.hashMap(name).createOrOpen();
	}

	@Override
	public Map<Long, Float> getData(String sensorName) {
		return getDB(sensorName);
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

	@Override
	public void updateGraphImage(File file) {
		this.allSensorGraph = file;

	}

	@Override
	public File getAllSensorGraphFile() {
		return allSensorGraph;
	}

	@Override
	public void setActiveGraph(GraphTiming timing) {
		this.activeGraphTiming = timing;
	}

	@Override
	public void updateGraphImage(GraphTiming timing, File file) {
		this.graphMap.put(timing, file);

	}

	@Override
	public File getGraph() {
		return graphMap.get(activeGraphTiming);
	}

	@Override
	public GraphTiming getActiveGraphTiming() {
		return activeGraphTiming;
	}

}
