package sensorServer;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.mapdb.DB;
import org.mapdb.HTreeMap;
import org.mapdb.Serializer;
import org.springframework.stereotype.Component;

@Component
public class MapDBStorageFactory implements StorageFactory {

	private DB db;

	public MapDBStorageFactory(DB db) {
		this.db = db;
	}

	@Override
	public Map<Long,Float> getStorage(String name) {
		HTreeMap<Long, Float> map = db.hashMap(name,  Serializer.LONG, Serializer.FLOAT).createOrOpen();
		return new MapWrapper(map);
	}

}

class MapWrapper implements  Map<Long,Float>{

	private final HTreeMap<Long, Float> map;

	public MapWrapper(HTreeMap<Long, Float> map) {
		this.map = map;
	}

	@Override
	public int size() {
		return map.size();
	}

	@Override
	public boolean isEmpty() {
		return map.isEmpty();
	}

	@Override
	public boolean containsKey(Object key) {
		return map.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return map.containsValue(value);
	}

	@Override
	public Float get(Object key) {
		return map.get(key);
	}

	@Override
	public Float put(Long key, Float value) {
		return map.put(key,value);
	}

	@Override
	public Float remove(Object key) {
		return map.remove(key);
	}

	@Override
	public void putAll(Map<? extends Long, ? extends Float> m) {
		map.putAll(m);
		
	}

	@Override
	public void clear() {
		map.clear();
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<Long> keySet() {
		return map.keySet();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<Float> values() {
		return map.values();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<java.util.Map.Entry<Long, Float>> entrySet() {
		return map.entrySet();
	}
	
}
