package sensorServer;
import static org.mockito.Mockito.*;

import java.sql.Timestamp;
import java.util.HashMap;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes={SensorServiceTestContext.class, SensorService.class})
@RunWith(SpringRunner.class)
public class SensorServiceTest {

	@MockBean
	private TaskScheduler taskScheduler;
	@MockBean
	private StorageFactory storageFactory;
	
	@Autowired
	private SensorService sensorService;
	
	@Autowired 
	private String sensorName;
	
	@Test
	public void test() {
		HashMap<Long, Float> map = new HashMap<>();
		when(storageFactory.getStorage(sensorName)).thenReturn(map);
		
		map.put(3l, 1.5f);
		map.put(1l, 0.5f);
		map.put(20l, 10f);
		
		TermoValue tv = sensorService.getLatestValueFor(sensorName);
		assertEquals(new TermoValue(new Timestamp(20l), 10f),tv);
	}

}
