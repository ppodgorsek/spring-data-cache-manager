package org.ppodgorsek.cache.manager.cassandra.model;

import java.io.Serializable;

import org.ppodgorsek.cache.manager.core.model.CachedValue;
import org.springframework.data.cassandra.mapping.Table;

/**
 * @author Paul Podgorsek
 */
@Table("default")
public abstract class CassandraCachedValue<VALUE extends Serializable> implements CachedValue<VALUE> {

	private long creationDate;

	private String id;

	private VALUE value;

	@Override
	public long getCreationDate() {
		return creationDate;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public VALUE getValue() {
		return value;
	}

	@Override
	public void setCreationDate(long creationDate) {
		this.creationDate = creationDate;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public void setValue(VALUE value) {
		this.value = value;
	}

}
