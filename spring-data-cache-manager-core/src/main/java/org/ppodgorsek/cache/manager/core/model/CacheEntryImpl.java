package org.ppodgorsek.cache.manager.core.model;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Persistent;

/**
 * @author Paul Podgorsek
 */
@Persistent
public class CacheEntryImpl implements CacheEntry {

	private static final long serialVersionUID = 2227302096962496577L;

	@Id
	private String key;

	@Persistent
	private Serializable value;

	@Persistent
	private String region;

	@LastModifiedDate
	private long creationDate;

	@Persistent
	private long calls;

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public void setKey(final String key) {
		this.key = key;
	}

	@Override
	public Serializable getValue() {
		return value;
	}

	@Override
	public void setValue(final Serializable value) {
		this.value = value;
	}

	@Override
	public String getRegion() {
		return region;
	}

	@Override
	public void setRegion(final String region) {
		this.region = region;
	}

	@Override
	public long getCreationDate() {
		return creationDate;
	}

	@Override
	public void setCreationDate(final long creationDate) {
		this.creationDate = creationDate;
	}

	@Override
	public long getCalls() {
		return calls;
	}

	@Override
	public void setCalls(final long calls) {
		this.calls = calls;
	}

}
