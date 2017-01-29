package sensorServer.configuration;

import java.util.concurrent.TimeUnit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SensorUpdateInterval {
	private TimeUnit timeUnit;
	private long value;

	public long toMillis() {
		return timeUnit.toMillis(value);
	}
}
