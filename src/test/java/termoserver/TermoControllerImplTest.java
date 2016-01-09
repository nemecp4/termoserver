package termoserver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TermoControllerImplTest {

	@Test
	public void parseTemperatureTest() {
		Float v1 = TermoControllerImpl
				.parseTemperature("98 01 55 00 7f ff 0c 10 24 : crc=24 YES");
		assertNull("no temperature found", v1);

		Float v2 = TermoControllerImpl
				.parseTemperature("98 01 55 00 7f ff 0c 10 24 t=25500");
		assertEquals("temp is 25.5", 25.5d, v2.floatValue(), 0.01d);
	}

}
