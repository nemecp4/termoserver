package termoserver;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfiguration {

	@Bean
	public TemperatureProvider temperatureProvider() {
		return new FakeTemperatureProvider();
	}

	@Bean
	TermoConfiguration termoConfiguration() {
		return new TermoConfiguration();
	}

	@Bean
	TermoController termoController() {
		return new TermoControllerImpl();
	}

	@Bean
	TermoModel termoModel() {
		return new TermoModelImpl();
	}

	@Bean
	TermoGraph termoGraph() {
		return new TermoGraph();
	}
}
