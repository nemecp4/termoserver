package termoserver;

public interface TemperatureProvider {
	public void registerListener(TemperatureChangeListener listener);
}
