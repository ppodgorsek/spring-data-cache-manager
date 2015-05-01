package org.ppodgorsek.cache.manager.core.model;

import java.io.Serializable;

/**
 * This interface represents a cache entry stored by Spring Data.
 *
 * @author Paul Podgorsek
 */
public interface CacheEntry extends Serializable {

	long getCalls();

	long getCreationDate();

	/**
	 * FIXME: the key could be a String or a simple type (int, long, float, double, Integer,Long, Float, Double)
	 */
	String getKey();

	Serializable getValue();

	void setCalls(long calls);

	void setCreationDate(long creationDate);

	void setKey(String key);

	void setValue(Serializable value);

	String getRegion();

	void setRegion(String region);

}
