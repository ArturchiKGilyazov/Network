package ru.nsu.ccfit.lukyanova.udpserver;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class programsDataSingleton {
	private static programsDataSingleton instance = null;

	private Map<String, Long> storage = Collections.synchronizedMap(new HashMap<>());

	public static synchronized programsDataSingleton getInstance() {
		if (null == instance) {
			instance = new programsDataSingleton();
		}
		return instance;
	}
	
	public void cleanUp() {
		var currentTime = System.currentTimeMillis();
		storage.entrySet().removeIf(item -> (currentTime - item.getValue()> 10000));
	}
	
	public void addProgram(String id) {
		storage.put(id, System.currentTimeMillis());
	}
	
	public Set<String> getData() {
		return Set.copyOf(storage.keySet());
	}
}
