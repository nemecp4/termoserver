package termoserver;

public interface TemperatureChangeListener {
	public void update(double temperature, long time);
}
