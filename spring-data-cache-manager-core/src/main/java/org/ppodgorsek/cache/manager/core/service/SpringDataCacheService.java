package org.ppodgorsek.cache.manager.core.service;

import org.springframework.cache.Cache;

/**
 * @author Paul Podgorsek
 */
public interface SpringDataCacheService extends Cache {

	/**
	 * Returns the number of cache hits that occurred while statistics were enabled.
	 *
	 * @return The number of hits.
	 */
	long getHits();

	/**
	 * Returns the number of cache misses that occurred while statistics were enabled.
	 *
	 * @return The number of misses.
	 */
	long getMisses();

	/**
	 * Indicates whether statistics have been enabled or not.
	 *
	 * @return {@code true} if the stats are enabled, {@code false} otherwise.
	 */
	boolean areStatisticsEnabled();

	void setStatisticsEnabled(boolean enabled);

}
