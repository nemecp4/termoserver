package termoserver;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class TemperatureTime {

	TemperatureTime() {
		Instant iDate = Instant.now();
		Instant iDate2 = Instant.ofEpochMilli(System.currentTimeMillis());

		Instant iDate3 = iDate2.minus(5, ChronoUnit.DAYS);

		System.out.println("now1: " + iDate + "\nnow2: " + iDate2 + "\nnow3 "
				+ iDate3.toEpochMilli());
	}

	public static void main(String[] argc) {
		new TemperatureTime();
	}
}
