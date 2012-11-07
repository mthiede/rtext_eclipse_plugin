package org.rtext.lang.util;

import java.util.HashMap;
import java.util.Map;

public class Cache<K, V> {
	
	private final Function<K, V> initializer;
	private final Map<K, V> contents = new HashMap<K, V>();

	public Cache(Function<K, V> initializer) {
		this.initializer = initializer;
	}

	public static <K, V> Cache<K, V> create(Function<K, V> initializer){
		return new Cache<K, V>(initializer);
	}
	
	
	public V get(K key){
		V value = contents.get(key);
		if(value == null){
			value = initializer.apply(key);
			contents.put(key, value);
		}
		return value;
	}

}
