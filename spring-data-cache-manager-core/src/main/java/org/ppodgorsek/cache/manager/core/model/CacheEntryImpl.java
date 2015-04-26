package org.ppodgorsek.cache.manager.core.model;

import java.io.Serializable;

import org.springframework.data.annotation.Persistent;

/**
 * @author Paul Podgorsek
 */
@Persistent
public class CacheEntryImpl implements CacheEntry {

	private long creationDate;

	private String key;

	private Serializable value;

	private String region;

	private long calls;

	@Override
	public String getRegion() {
		return region;
	}

	@Override
	public void setRegion(String region) {
		this.region = region;
	}

	@Override
	public long getCreationDate() {
		return creationDate;
	}

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public Serializable getValue() {
		return value;
	}

	@Override
	public void setCreationDate(long creationDate) {
		this.creationDate = creationDate;
	}

	@Override
	public void setKey(String key) {
		this.key = key;
	}

	@Override
	public void setValue(Serializable value) {
		this.value = value;
	}

	@Override
	public long getCalls() {
		return calls;
	}

	@Override
	public void setCalls(long calls) {
		this.calls = calls;
	}

}
