package termoserver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TermoControllerImpl implements TermoController, InitializingBean {
	@Autowired
	private TermoConfiguration config;
	@Autowired
	private TermoModel model;

	@Autowired
	private TermoGraph graphController;

	@Override
	public void afterPropertiesSet() throws Exception {
		if (config.getSensorNames().size() > 0) {
			String name = config.getSensorNames().get(0);
			log.info("setting active sensor: {}", name);
			model.setActiveGraph(GraphTiming.MINUT_5);
			model.setSensors(config.getSensorNames());
		}

	}

	public static Float parseTemperature(String line) {

		String[] parts = line.split(" ");
		for (String string : parts) {
			if (string.startsWith("t=")) {
				String value = string.substring(2);
				if (value != null && value.length() > 0) {
					return new Float(Float.parseFloat(value) / 1000);
				}
			}
		}
		return null;
	}

	@Override
	public void update() {
		List<String> names = config.getSensorNames();
		log.info("going to update {}", names);
		for (String name : names) {
			File file = config.getSensorFile(name);
			if (!file.exists()) {
				log.warn("Unable to read temperature from {}, file not exist",
						file);
				continue;
			}
			try (BufferedReader bw = new BufferedReader(new FileReader(file))) {
				String line;
				while ((line = bw.readLine()) != null) {
					if (line.contains("t=")) {
						Float t = parseTemperature(line);
						if (t != null) {
							model.saveTemperature(name, t,
									System.currentTimeMillis());
						}
					}
				}

			} catch (IOException e) {
				log.warn("unable to read sensor {}, exeption: ", file,
						e.toString());

			}
		}
	}

	@Override
	public void setActiveGraph(GraphTiming timing) {
		log.info("setting active graph to {}", timing);
		model.setActiveGraph(timing);
	}

	@Override
	public void updateGraph(GraphTiming timing) {

		File file = graphController.generateGraph(timing);
		log.debug(
				"generating graph for all sensors{} with timing {} stored in file {}",
				model.getSensorNames(), timing, file);
		model.updateGraphImage(timing, file);

	}
}
