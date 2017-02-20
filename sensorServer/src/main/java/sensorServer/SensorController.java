package sensorServer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(path = "/sensors")
@RestController
public class SensorController {

	@Autowired
	private SensorService sensorService;

	@RequestMapping(method = RequestMethod.GET)
	public List<String> getSensors() {
		return sensorService.getSensors();
	}
	
	@RequestMapping(path = "/{sensorName}/latest", method = RequestMethod.GET)
	public TermoValue getLatestValue(@PathVariable String sensorName){
		return sensorService.getLatestValueFor(sensorName);
	}
	
	@RequestMapping(path = "/{sensorName}/update", method = RequestMethod.GET)
	public TermoValue forceUpdateValue(@PathVariable String sensorName){
		return sensorService.updateAndGetValeu(sensorName);
	}
	
	@RequestMapping(path = "/{sensorName}/data", method = RequestMethod.GET)
	public List<TermoValue> getData(@PathVariable String sensorName, @RequestParam long from, @RequestParam long to){
		return sensorService.getData(sensorName,from,to);
	}
}
