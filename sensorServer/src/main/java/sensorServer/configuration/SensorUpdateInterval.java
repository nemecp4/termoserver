package sensorServer.configuration;

import java.util.concurrent.TimeUnit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SensorUpdateInterval {
	public static SensorUpdateInterval DEFAULT_UPDATE_INTERVAL = new SensorUpdateInterval(TimeUnit.SECONDS, 30l);

	private TimeUnit timeUnit;
	private long value;

	public long toMillis() {
		return timeUnit.toMillis(value);
	}
}
