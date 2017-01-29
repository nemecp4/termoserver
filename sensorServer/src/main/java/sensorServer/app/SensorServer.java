package sensorServer.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties
@SpringBootApplication(scanBasePackages="sensorServer")
public class SensorServer {
	public static void main(String[] args) {
		 SpringApplication.run(SensorServer.class, args);
	}
}
