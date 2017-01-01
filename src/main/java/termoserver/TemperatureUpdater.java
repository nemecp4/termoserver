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
		log.info("reading temperature");
		controller.update();
	}

	/**
	 * once a 5 minute=300 000
	 */
	@Scheduled(fixedDelay = 300000)
	public void update30M() {
		log.info("Updating 30m graph");
		controller.updateGraph(GraphTiming.MINUT_30);
	}

	/**
	 * once per 30 minute = 18000000
	 */
	@Scheduled(fixedDelay = 18000000)
	public void update5H() {
		log.info("Updating 5H temperature");
		controller.updateGraph(GraphTiming.HOUR5);
	}

	/**
	 * once a hour=3600000
	 */
	@Scheduled(fixedDelay = 3600000)
	public void update5D() {
		log.info("Updating 5D temperature");
		controller.updateGraph(GraphTiming.DAY_5);
	}
}
