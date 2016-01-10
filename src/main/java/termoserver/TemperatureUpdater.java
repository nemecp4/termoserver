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

	@Scheduled(fixedDelay = 10000)
	public void readTemperature() {
		log.info("scheduled update of temperature");
		controller.update();
	}
}
