package sensorServer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import sensorServer.configuration.SensorConfiguration;

@Slf4j
public class SensorUpdateTask implements Runnable {

	private SensorConfiguration sensor;
	private Map<Long, Float> storage;
	private TimeService timeService;

	public SensorUpdateTask(SensorConfiguration sensor, TimeService timeService, Map<Long, Float> storage) {
		this.sensor = sensor;
		this.timeService = timeService;
		this.storage = storage;
	}

	@Override
	public void run() {
		update();
	}

	public StoreValue update() {
		StoreValue value = read();
		if (value != null) {
			log.info("storing {}", value);
			storage.put(value.getCurrentTime(), value.getTemperature());
		}
		return value;
	}

	private StoreValue read() {
		log.info("Updating value from sensor: {}", sensor.getName());
		File file = new File(sensor.getSensorFile());
		if (!file.exists()) {
			log.warn("Unable to read temperature from {}, file not exist", file);
		}
		try (BufferedReader bw = new BufferedReader(new FileReader(file))) {
			String line;
			while ((line = bw.readLine()) != null) {
				if (line.contains("t=")) {
					Float t = parseTemperature(line);
					if (t != null) {
						log.debug("value read={}", t);
						return new StoreValue(timeService.currentTime(), t);
					}
				}
			}
		} catch (IOException e) {
			log.warn("unable to read sensor {}, exeption: {}", file, e.toString());
		}
		return null;
	}

	public static Float parseTemperature(String line) {
		String[] parts = line.split(" ");
		for (String string : parts) {
			if (string.startsWith("t=")) {
				String value = string.substring(2);
				if (value != null && value.length() > 0) {
					return new Float(Float.parseFloat(value) / 1000);
				}
			}
		}
		return null;
	}

}