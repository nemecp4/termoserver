package termoserver;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TemperatureUpdater {

	@Autowired
	private TermoController controller;

	/**
	 * once 30s = 30000
	 */
	@Scheduled(fixedDelay = 30000)
	public void readTemperature() {
		log.info("scheduled update of temperature");
		controller.update();
	}

	/**
	 * once a 30 minute=1800000
	 */
	@Scheduled(fixedDelay = 1800000)
	public void update30M() {
		log.info("scheduled update of temperature");
		controller.updateGraph(GraphTiming.MINUT_30);
	}

	/**
	 * once a 5 hour=18000000
	 */
	@Scheduled(fixedDelay = 18000000)
	public void update5H() {
		log.info("scheduled update of temperature");
		controller.updateGraph(GraphTiming.HOUR5);
	}

	/**
	 * once a 6hour=21600000
	 */
	@Scheduled(fixedDelay = 21600000)
	public void update5D() {
		log.info("scheduled update of temperature");
		controller.updateGraph(GraphTiming.DAY_5);
	}
}
