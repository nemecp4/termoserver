package termoserver;

import java.time.temporal.ChronoUnit;

public enum GraphTiming {
	HOUR5(5, ChronoUnit.HOURS), DAY_5(5, ChronoUnit.DAYS), MINUT_30(30,
			ChronoUnit.MINUTES);

	private long duration;
	private ChronoUnit unit;

	private GraphTiming(long duration, ChronoUnit unit) {
		this.duration = duration;
		this.unit = unit;
	}

	public long getDuration() {
		return duration;
	}

	public ChronoUnit getUnit() {
		return unit;
	}
}
