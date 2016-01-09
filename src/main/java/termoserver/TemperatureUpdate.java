package termoserver;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TemperatureUpdate {
	private final Long time;
	private final Float temperature;
}
