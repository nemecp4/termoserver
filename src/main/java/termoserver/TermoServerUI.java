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

		// File graphFile = model.getGraphFile(model.getActiveSensor());
		File graphFile = model.getGraph();
		if (graphFile == null) {
			log.warn("no image for {} in model", model.getActiveGraphTiming());
			return new Label("no graph for " + model.getActiveGraphTiming());
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
		allSensorsHL.setMargin(true);
		allSensorsHL.setSpacing(true);

		List<String> sensors = model.getSensorNames();

		if (sensors.size() == 0) {
			allSensorsHL.addComponent(new Label("no sensors configured"));
			log.warn("no sensor to display");
			return;
		}

		// if (activeSensor != null) {
		// TemperatureUpdate latestUpdate = model
		// .getLatestUpdate(activeSensor);
		// Panel panel = new Panel(String.format("<b>%s</b>", activeSensor));
		//
		// VerticalLayout activeVL = new VerticalLayout();
		// activeVL.setMargin(true);
		// String time = (latestUpdate == null ? "?" : longToTime(latestUpdate
		// .getTime()));
		// float temp = (latestUpdate == null ? 0f : latestUpdate
		// .getTemperature());
		// Button activeButton = new Button(String.format("%.2f[C] - %s",
		// temp, time));
		// activeButton.addClickListener((event) -> controller.update());
		//
		// activeVL.addComponent(activeButton);
		//
		// activeVL.addComponent(createGraph());
		//
		// activeVL.setComponentAlignment(activeButton,
		// Alignment.MIDDLE_CENTER);
		// panel.setContent(activeVL);
		// mainVL.addComponent(panel);
		//
		// }

		for (String name : sensors) {
			TemperatureUpdate latestUpdate = model.getLatestUpdate(name);
			String time = (latestUpdate == null ? "?" : longToTime(latestUpdate
					.getTime()));
			float temp = (latestUpdate == null ? 0f : latestUpdate
					.getTemperature());

			Label sensorLabel = new Label(String.format("%s (%.2f[C]  %s)",
					name, temp, time));
			allSensorsHL.addComponent(sensorLabel);
		}
		mainVL.addComponent(allSensorsHL);
		mainVL.addComponent(createGraph());

		HorizontalLayout graphControlHL = new HorizontalLayout();
		Button hoursB = new Button("5 hodin");
		hoursB.addClickListener((event) -> updateGraph(GraphTiming.HOUR5));
		Button minuteB = new Button("30 minut");
		minuteB.addClickListener((event) -> updateGraph(GraphTiming.MINUT_30));
		Button daysB = new Button("5 days");
		daysB.addClickListener((event) -> updateGraph(GraphTiming.DAY_5));
		graphControlHL.addComponent(minuteB);
		graphControlHL.addComponent(hoursB);
		graphControlHL.addComponent(daysB);
		mainVL.addComponent(graphControlHL);

		// mainVL.addComponent(new Label("hello world"));
		setContent(mainVL); // Attach to the UI
	}

	private void updateGraph(GraphTiming timing) {
		controller.setActiveGraph(timing);
		reset();
	}

	private String longToTime(long time) {
		return format.format(new Date(time));
	}

	@Override
	public void update() {
		// check that vadding thread is active thread should be done here
		// reset();

	}

}
