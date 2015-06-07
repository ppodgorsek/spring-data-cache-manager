package org.ppodgorsek.cache.manager.core.model;

/**
 * @author Paul Podgorsek
 */
public class CacheRegionConfiguration {

	private String name;

	private String type;

	private long timeToLive;

	private EvictionStrategy evictionStrategy;

	private int maxEntriesInCache;

	private boolean eternal;

	/**
	 * Default constructor.
	 */
	public CacheRegionConfiguration() {
		super();
	}

	@Override
	public String toString() {

		final StringBuilder builder = new StringBuilder(getClass().getSimpleName());
		builder.append("[name=").append(name);
		builder.append(",type=").append(type);
		builder.append(",timeToLive=").append(timeToLive);
		builder.append(",evictionStrategy=").append(evictionStrategy);
		builder.append(",maxEntriesInCache=").append(maxEntriesInCache);
		builder.append(",eternal=").append(eternal);
		builder.append("]");

		return builder.toString();
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

	public String getType() {
		return type;
	}

	public void setType(final String type) {

		if (type == null) {
			this.type = null;
		}
		else {
			this.type = type.toLowerCase();
		}
	}

	public long getTimeToLive() {
		return timeToLive;
	}

	public void setTimeToLive(final long timeToLive) {
		this.timeToLive = timeToLive;
	}

	public EvictionStrategy getEvictionStrategy() {
		return evictionStrategy;
	}

	public void setEvictionStrategy(final EvictionStrategy evictionStrategy) {
		this.evictionStrategy = evictionStrategy;
	}

	public int getMaxEntriesInCache() {
		return maxEntriesInCache;
	}

	public void setMaxEntriesInCache(final int maxEntries) {
		maxEntriesInCache = maxEntries;
	}

	public boolean isEternal() {
		return eternal;
	}

	public void setEternal(final boolean eternal) {
		this.eternal = eternal;
	}

}
