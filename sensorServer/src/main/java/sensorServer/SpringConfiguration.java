package sensorServer;

import java.io.File;
import java.io.InputStream;
import java.util.List;

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

import lombok.extern.slf4j.Slf4j;
import sensorServer.configuration.SensorConfiguration;

@Slf4j
@EnableScheduling
@Configuration
public class SpringConfiguration {

	@Bean
	public DB db(@Value("${mapdb.dbfile}") String dbFile) {
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
	public List<SensorConfiguration> sensorConfiguration(@Value("${sensor.propertyFile}") String propertyFile) {
		log.info("Creating Sensor Configuration from: {}", propertyFile);
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
}
