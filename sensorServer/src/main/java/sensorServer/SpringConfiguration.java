package sensorServer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.mapdb.DB;
import org.mapdb.DBException;
import org.mapdb.DBMaker;
import org.mapdb.DBMaker.Maker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import com.google.common.collect.ImmutableList;

import lombok.extern.slf4j.Slf4j;
import sensorServer.configuration.SensorConfiguration;
import sensorServer.configuration.SensorUpdateInterval;

@Slf4j
@EnableScheduling
@Configuration
public class SpringConfiguration {

	private static final SensorUpdateInterval SensorUpdateInterval = null;

	@Bean
	public DB db(@Value("${mapdb.dbfile}") String dbFile) {
		log.info("opening MapDB at {}", dbFile);
		try {
			Maker maker = DBMaker.fileDB(new File(dbFile)).fileMmapEnable().closeOnJvmShutdown();
			return maker.make();
		} catch (DBException.WrongFormat e) {
			// old db file
			log.error("unable to re-open mapdb file {}, probably old format, remove it and start again", dbFile);
			throw e;
		}
	}

	@Bean
	public List<SensorConfiguration> sensorConfiguration(@Value("${sensor.propertyFile}") String propertyFile)
			throws IOException {
		log.info("Creating Sensor Configuration from: {}", propertyFile);
		if (!Paths.get(propertyFile).toFile().exists()) {
			log.warn("provided configuration file do not exist, generating blank one");
			generateSampleConfiguration(propertyFile);
			log.warn("Please edit file {} ", propertyFile);
			throw new RuntimeException("Please edit file " + propertyFile);
		}

		InputStream resource = Utils.tryOpenResource(propertyFile);
		if (resource == null) {
			log.error("Unable to access file: " + propertyFile);
			throw new RuntimeException(" file not found " + propertyFile);
		}
		Yaml yaml = new Yaml(new Constructor(List.class));
		@SuppressWarnings("unchecked")
		List<SensorConfiguration> sensors = (List<SensorConfiguration>) yaml.load(resource);
		return sensors;
	}

	@Bean
	public StorageFactory storageFactory(DB db) {
		return new MapDBStorageFactory(db);
	}

	@Bean
	public TimeService timeService() {
		return () -> System.currentTimeMillis();
	}

	@Bean
	public TaskScheduler taskScheduler() {
		return new ThreadPoolTaskScheduler();// default is 1
	}

	private void generateSampleConfiguration(String propertyFile) throws IOException {
		ImmutableList<SensorConfiguration> sampleSensors = ImmutableList.of(
				SensorConfiguration.builder()
						.name("NameOfFirstSensor")
						.sensorFile("/some/path/to/sensor1")
						.sensorUpdateInterval(sensorServer.configuration.SensorUpdateInterval.DEFAULT_UPDATE_INTERVAL)
						.build(),
				SensorConfiguration.builder()
						.name("NameOfSecondSensor")
						.sensorFile("/some/path/to/sensor2")
						.sensorUpdateInterval(new SensorUpdateInterval(TimeUnit.HOURS, 3l))
						.build());
		Yaml yaml = new Yaml(new Constructor(List.class));
		yaml.dump(sampleSensors, new FileWriter(propertyFile));
	}
}
