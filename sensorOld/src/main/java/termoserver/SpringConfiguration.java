package termoserver;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling
@Configuration
public class SpringConfiguration {

	/**
	 * we do not need to define particular beans here as they are scanned by
	 * spring and created by @Component or such<br>
	 * We need to have this configuration to set Scheduling on.
	 */
}
