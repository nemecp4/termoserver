package sensorServer;

import static org.mockito.Mockito.*;

import java.util.HashMap;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import sensorServer.configuration.SensorConfiguration;

public class SensorUpdateTaskTest {
	private SensorConfiguration sensor;
	private TimeService timeService;
	private HashMap<Long, Float> storage;
	private SensorUpdateTask sensorUpdate;

	@Before
	public void setup() {
		timeService = mock(TimeService.class);
		sensor = SensorConfiguration.builder().name("name").build();
		storage = new HashMap<Long, Float>();
		sensorUpdate = new SensorUpdateTask(sensor, timeService, storage);
	}

	@Test
	public void parseTemperatureTest() {
		Float v1 = SensorUpdateTask.parseTemperature("98 01 55 00 7f ff 0c 10 24 : crc=24 YES");
		assertNull("no temperature found", v1);

		Float v2 = SensorUpdateTask.parseTemperature("98 01 55 00 7f ff 0c 10 24 t=25500");
		assertEquals("temp is 25.5", 25.5d, v2.floatValue(), 0.01d);
	}

}
