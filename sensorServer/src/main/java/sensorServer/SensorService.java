package sensorServer;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import sensorServer.configuration.SensorConfiguration;

@Service
@Slf4j
public class SensorService {

	private final LinkedHashMap<String, SensorConfiguration> sensorMap = new LinkedHashMap<>();
	private final TaskScheduler taskScheduler;
	private final StorageFactory storageFactory;
	private final TimeService timeService;

	@Autowired
	public SensorService(Collection<SensorConfiguration> sensorConfiguration, TaskScheduler taskScheduler,
			StorageFactory storageFactory, TimeService timeService) {
		sensorConfiguration.stream().forEach(s -> sensorMap.put(s.getName(), s));
		this.taskScheduler = taskScheduler;
		this.storageFactory = storageFactory;
		this.timeService = timeService;

	}

	@PostConstruct
	public void schedule() {
		log.info("Scheduling update of sensors ({})", sensorMap.size());
		Date now = new Date();
		sensorMap.values().forEach(sensor -> {
			log.info("Scheduling sensor: {}", sensor);
			taskScheduler.scheduleAtFixedRate(getTask(sensor.getName()), now,
					sensor.getSensorUpdateInterval().toMillis());
		});
	}

	public List<String> getSensors() {
		return sensorMap.keySet().stream().collect(Collectors.toList());
	}

	public TermoValue getLatestValueFor(String sensorName) {
		Map<Long, Float> storage = getStorage(sensorName);
		Optional<Long> o = storage.keySet().stream().sorted().max((a, b) -> a.compareTo(b));
		return o.isPresent() ? new TermoValue(new Timestamp(o.get()), storage.get(o.get())) : null;
	}

	public TermoValue updateAndGetValeu(String sensorName) {
		StoreValue value = getTask(sensorName).update();
		return new TermoValue(new Timestamp(value.getCurrentTime()), value.getTemperature());
	}

	private SensorUpdateTask getTask(String sensorName) {
		return new SensorUpdateTask(sensorMap.get(sensorName), timeService, storageFactory.getStorage(sensorName));
	}

	public List<TermoValue> getData(String sensorName, Long from, Long to) {
		Map<Long, Float> storage = getStorage(sensorName);
		return storage.keySet().stream()
				.filter(p -> biggerThan(p, from))
				.filter(p -> lessThan(p, to))
				.map(key -> new TermoValue(new Timestamp(key), storage.get(key)))
				.collect(Collectors.toList());
	}

	public List<TermoValue> getData(String sensorName, long from) {
		Map<Long, Float> storage = getStorage(sensorName);
		return storage.keySet()
				.stream()
					.filter(p -> biggerThan(p, from))
				.map(key -> new TermoValue(new Timestamp(key), storage.get(key)))
				.collect(Collectors.toList());
	}

	private Map<Long, Float> getStorage(String sensorName) {
		if (!sensorMap.keySet().contains(sensorName)) {
			throw new NoSuchSensorAvailableException("no sensor named: " + sensorName);
		}
		return storageFactory.getStorage(sensorName);
	}

	private boolean lessThan(Long l1, Long l2) {
		return l1 < l2;
	}

	private boolean biggerThan(Long l1, Long l2) {
		return l1 > l2;
	}

}
