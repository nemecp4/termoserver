package sensorServer;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;

import sensorServer.configuration.SensorConfiguration;
import sensorServer.configuration.SensorUpdateInterval;

@Configuration
public class SensorServiceTestContext {
	@Bean
	public TimeService timeService() {
		return () -> 1l;
	}

	@Bean
	String sensorName() {
		return "e2a";
	}

	@Bean
	public Collection<SensorConfiguration> sensorConfiguration(String sensorName) {
		return Collections.singleton(SensorConfiguration.builder().name(sensorName).sensorFile("")
				.sensorUpdateInterval(new SensorUpdateInterval(TimeUnit.HOURS, 1l)).build());
	}
}
