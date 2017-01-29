package sensorServer;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import junit.framework.TestCase;
import sensorServer.configuration.SensorConfiguration;
import sensorServer.configuration.SensorUpdateInterval;

public class SensorConfigurationTest extends TestCase {
	String resourceName = "sensors-example.yml";

	public void testUnmarshalingOfConfiguration() throws IOException {
		InputStream resource = this.getClass().getClassLoader().getResource(resourceName).openStream();
		assertNotNull("find test resource on classpath", resource);

		Yaml yaml = new Yaml(new Constructor(List.class));

		@SuppressWarnings("unchecked")
		List<SensorConfiguration> sensors = (List<SensorConfiguration>) yaml.load(resource);
		assertNotNull(sensors);
		assertEquals("two sensors were loaded", 2, sensors.size());
		sensors.contains(new SensorConfiguration("Nam1", "tmp/term1", new SensorUpdateInterval(TimeUnit.SECONDS, 30)));
	}

//	@Test
//	public void testExportTest() throws JsonProcessingException {
//		List<SensorConfiguration> set = new ArrayList<SensorConfiguration>();
//		set.add(new SensorConfiguration("Nam1", "tmp/term1", new SensorUpdateInterval(TimeUnit.SECONDS, 30)));
//		set.add(new SensorConfiguration("Nam2", "tmp/term2", new SensorUpdateInterval(TimeUnit.HOURS, 30)));
//		Yaml yaml = new Yaml();
//		String dump = yaml.dump(set);
//		System.out.println(dump);
//
//		final ObjectMapper mapper = new ObjectMapper(); // jackson's
//														// objectmapper
//		dump = mapper.writeValueAsString(set);
//		System.out.print(" jackson: " + dump);
//		assertEquals("dump: ", dump);
//	}
}
