package sensorServer;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class StoreValue {

	private Long currentTime;
	private Float temperature;
}
