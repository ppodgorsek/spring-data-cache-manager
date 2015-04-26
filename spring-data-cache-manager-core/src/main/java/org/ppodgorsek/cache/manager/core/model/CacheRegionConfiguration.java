package org.ppodgorsek.cache.manager.core.model;

/**
 * @author Paul Podgorsek
 */
public class CacheRegionConfiguration {

	private String name;

	private long timeToLive;

	private EvictionStrategyType evictionStrategyType;

	private long maxEntriesInCache;

	private String type;

	/**
	 * Default constructor.
	 */
	public CacheRegionConfiguration() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {

		if (name == null) {
			name = null;
		}
		else {
			this.name = name.toLowerCase();
		}
	}

	public long getTimeToLive() {
		return timeToLive;
	}

	public void setTimeToLive(long timeToLive) {
		this.timeToLive = timeToLive;
	}

	public EvictionStrategyType getEvictionStrategyType() {
		return evictionStrategyType;
	}

	public void setEvictionStrategyType(EvictionStrategyType evictionStrategyType) {
		this.evictionStrategyType = evictionStrategyType;
	}

	public long getMaxEntriesInCache() {
		return maxEntriesInCache;
	}

	public void setMaxEntriesInCache(long maxEntriesInCache) {
		this.maxEntriesInCache = maxEntriesInCache;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {

		if (type == null) {
			this.type = null;
		}
		else {
			this.type = type.toLowerCase();
		}
	}

}
