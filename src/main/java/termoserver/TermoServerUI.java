package termoserver;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.annotations.Theme;
import com.vaadin.server.FileResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SpringUI
@Theme("valo")
@Slf4j
public class TermoServerUI extends UI implements TermoModelListener {

	/**
	 * this is not thread save but we are not going to be updated from multiple
	 * threads
	 **/
	private SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");

	@Autowired
	TermoController controller;

	@Autowired
	TermoModel model;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void init(VaadinRequest vaadinRequest) {
		log.info("init called with " + vaadinRequest);
		model.registerListener(this);

		setPollInterval(5000);
		reset();

	}

	private Component createGraph() {

		File graphFile = model.getGraphFile(model.getActiveSensor());
		if (graphFile == null) {
			log.warn("no image for {} in model", model.getActiveSensor());
			return new Label("no graph for " + model.getActiveSensor());
		}

		Image graphImage = new Image("Termo graph ",
				new FileResource(graphFile));
		Panel graphPanel = new Panel();
		graphImage.addClickListener((event) -> controller.update());
		graphPanel.setContent(graphImage);
		return graphPanel;
	}

	private void reset() {

		VerticalLayout mainVL = new VerticalLayout();
		HorizontalLayout allSensorsHL = new HorizontalLayout();

		List<String> sensors = model.getSensorNames();

		if (sensors.size() == 0) {
			log.warn("no sensor to display");
			return;
		}
		String activeSensor = model.getActiveSensor();
		TemperatureUpdate latestUpdate = model.getLatestUpdate(activeSensor);
		if (activeSensor != null && latestUpdate != null) {
			Panel panel = new Panel(String.format("<b>%s</b>", activeSensor));

			VerticalLayout activeVL = new VerticalLayout();
			activeVL.setMargin(true);

			Button activeButton = new Button(String.format("%.2f[C] - %s",
					latestUpdate.getTemperature(),
					longToTime(latestUpdate.getTime())));
			activeButton.addClickListener((event) -> controller.update());

			activeVL.addComponent(activeButton);

			activeVL.addComponent(createGraph());

			activeVL.setComponentAlignment(activeButton,
					Alignment.MIDDLE_CENTER);
			panel.setContent(activeVL);
			mainVL.addComponent(panel);
		}

		for (String name : sensors) {
			TemperatureUpdate update = model.getLatestUpdate(name);
			if (update == null) {
				continue;
			}
			Button sB = new Button(String.format("%s (%.2f[C]/%s)", name,
					update.getTemperature(), longToTime(update.getTime())));
			sB.addClickListener((event) -> sensorSelected(name));
			allSensorsHL.addComponent(sB);
		}
		mainVL.addComponent(allSensorsHL);
		setContent(mainVL); // Attach to the UI
	}

	private String longToTime(long time) {
		return format.format(new Date(time));
	}

	private void sensorSelected(String name) {
		controller.setActiveSensor(name);
		reset();
	}

	@Override
	public void update() {
		// check that vadding thread is active thread should be done here
		// reset();

	}

}
