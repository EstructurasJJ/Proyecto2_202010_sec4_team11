package model.data_structures;

import java.util.Iterator;

public interface ITablaHash <K extends Comparable<K> ,V extends Comparable <V>>
{
	void putInSet(K key, V value);
	
	Iterator<V> getSet(K key);
	
	Iterator<V> deleteSet(K key);
	
	Iterator<K> keys();
}
