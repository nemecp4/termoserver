package sensorServer;

import java.util.Map;

public interface StorageFactory {
	Map<Long, Float> getStorage(String name);
}
