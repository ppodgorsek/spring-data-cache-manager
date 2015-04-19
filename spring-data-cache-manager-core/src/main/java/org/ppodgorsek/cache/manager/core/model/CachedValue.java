package org.ppodgorsek.cache.manager.core.model;

import java.io.Serializable;

/**
 * This interface represents a cached value stored by Spring Data.
 *
 * @author Paul Podgorsek
 */
public interface CachedValue<VALUE extends Serializable> extends Serializable {

	long getCalls();

	long getCreationDate();

	/**
	 * FIXME: the ID could be a String or a simple type (int, long, float, double, Integer,Long, Float, Double)
	 */
	String getId();

	VALUE getValue();

	void setCalls(long calls);

	void setCreationDate(long creationDate);

	void setId(String id);

	void setValue(VALUE value);

}
