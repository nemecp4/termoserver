package termoserver;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.InitializingBean;

@Slf4j
public class TermoConfiguration implements InitializingBean {

	private static final String home = System.getProperty("user.home");
	private static final String location = home + "/.termo.properties";
	private static final String TERMO_SENSORS = "TERMO_SENSOR_FILES";
	private static final String READ_INTERVALL = "READ_INTERVALL";
	private static final String TEMPERATURE_DB_FILE = "TEMPERATURE_DB_FILE";
	private Properties prop;
	private Map<String, File> sensors = new TreeMap<>();

	public TermoConfiguration() {
		prop = new Properties();
		File file = new File(location);
		if (!file.exists()) {
			generateDefaultProperties();
		}
		;
		try {
			prop.load(new FileReader(file));
		} catch (IOException e) {
			throw new RuntimeException(
					"can't read file from home directory, bad disk or what?", e);
		}
		log.info("proeprties read {}", prop);
	}

	private static void generateDefaultProperties() {
		Properties p = new Properties();
		p.setProperty(TERMO_SENSORS, "");
		p.setProperty(READ_INTERVALL, "5000");
		p.setProperty(TERMO_SENSORS, "name1:file_path1,name2:file_path2");
		p.setProperty(TEMPERATURE_DB_FILE, home + "/.temperature.db");
		try {
			p.store(new FileWriter(new File(location)), "default values");
		} catch (IOException e) {
			throw new RuntimeException(
					"cannot write to home directory, full disk?", e);
		}
	}

	public Long getTemperatueReadIntervall() {
		return Long.valueOf(prop.getProperty(READ_INTERVALL));
	}

	public List<File> getSensorFiles() {

		return new ArrayList<File>(sensors.values());
	}

	private void parseSensors() {

		String termoSensors = prop.getProperty(TERMO_SENSORS);

		List<File> files = new ArrayList<>();

		if (termoSensors == null || termoSensors.length() <= 0) {
			log.warn("sensors ({}) not defined in property file({})",
					TERMO_SENSORS, location);
		} else {

			for (String s : termoSensors.split(",")) {
				String[] sensor = s.split(":");
				if (sensor.length != 2) {
					log.warn(
							"Unable to parse sensor: {}, expecting name:value",
							s);
				} else {
					sensors.put(sensor[0], new File(sensor[1]));
				}
			}
		}
	}

	public File getDBFile() {
		String f = prop.getProperty(TEMPERATURE_DB_FILE);
		return new File(f);
	}

	public File getSensorFile(String name) {
		return sensors.get(name);
	}

	public List<String> getSensorNames() {
		return new ArrayList<String>(sensors.keySet());
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		parseSensors();

	}

}
