package termoserver;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class FakeTemperatureProvider implements TemperatureProvider, Runnable {

	private List<TemperatureChangeListener> list = new ArrayList<>();

	public FakeTemperatureProvider(){
		Thread t = new Thread(this);
		t.setDaemon(true);
		t.start();
	}
	
	@Override
	public void registerListener(TemperatureChangeListener listener) {
		this.list.add(listener);

	}

	@Override
	public void run() {
		while(true){
			double t = ThreadLocalRandom.current().nextDouble(0d, 90d);
			for (TemperatureChangeListener l : list) {
				l.update(t, System.currentTimeMillis());
			}
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

}
