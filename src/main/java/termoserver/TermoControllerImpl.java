package termoserver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class TermoControllerImpl implements TermoController, InitializingBean,
		Runnable {
	@Autowired
	private TermoConfiguration config;
	@Autowired
	private TermoModel model;

	@Autowired
	private TermoGraph graphController;

	private Thread termoThread;

	@Override
	public void afterPropertiesSet() throws Exception {
		this.termoThread = new Thread(this);
		termoThread.setName("temperature_reader");
		termoThread.setDaemon(true);
		termoThread.start();
		if (config.getSensorNames().size() > 0) {
			String name = config.getSensorNames().get(0);
			log.info("setting active sensor: {}", name);
			model.setActiveSensor(name);
			model.setSensors(config.getSensorNames());
		}

	}

	private void updateGraph(String name) {
		File file = graphController.generateGraph(name);
		log.debug("generating graph for {}", name);
		model.updateGraphImage(name, file);
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
	public void run() {
		while (true) {
			try {
				Thread.sleep(config.getTemperatueReadIntervall());
			} catch (InterruptedException e) {
			}
			update();
		}
	}

	@Override
	public void update() {
		List<String> names = config.getSensorNames();
		log.debug("going to update {}", names);
		int counter = 0;
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
			updateGraph(name);
		}

	}

	@Override
	public void setActiveSensor(String name) {
		model.setActiveSensor(name);

	}
}
