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
	 * once a 1minute=60000
	 */
	@Scheduled(fixedDelay = 10000)
	public void update5M() {
		log.info("scheduled update of temperature");
		controller.updateGraph(GraphTiming.MINUT_5);
	}

	/**
	 * once a 1hour=360000
	 */
	@Scheduled(fixedDelay = 10000)
	public void update5H() {
		log.info("scheduled update of temperature");
		controller.updateGraph(GraphTiming.HOUR5);
	}

	/**
	 * once a 6hour=21600000
	 */
	@Scheduled(fixedDelay = 10000)
	public void update5D() {
		log.info("scheduled update of temperature");
		controller.updateGraph(GraphTiming.DAY_5);
	}
}
