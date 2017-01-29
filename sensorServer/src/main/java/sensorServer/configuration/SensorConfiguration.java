package sensorServer.configuration;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class SensorConfiguration {
	
	@NotNull
	private String name;
	@NotNull
	private String sensorFile;
	@NotNull
	private SensorUpdateInterval sensorUpdateInterval;

}
