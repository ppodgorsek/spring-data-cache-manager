package org.ppodgorsek.cache.manager.core.service.impl;

import java.io.Serializable;
import java.util.Date;

import org.ppodgorsek.cache.manager.core.dao.SpringDataCacheDao;
import org.ppodgorsek.cache.manager.core.model.CacheEntry;
import org.ppodgorsek.cache.manager.core.model.CacheEntryImpl;
import org.ppodgorsek.cache.manager.core.model.EvictionStrategy;
import org.ppodgorsek.cache.manager.core.service.SpringDataCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.util.Assert;

/**
 * @author Paul Podgorsek
 */
public class SpringDataCacheServiceImpl implements SpringDataCacheService {

	private static final Logger LOGGER = LoggerFactory.getLogger(SpringDataCacheServiceImpl.class);

	private final SpringDataCacheDao dao;

	private final String name;

	private final boolean eternal;

	private final EvictionStrategy evictionStrategy;

	private long hits;

	private final long maxEntries;

	private long misses;

	/**
	 * Indicates whether statistics (hits, misses, number of entries) should be computed or not.
	 */
	private boolean statisticsEnabled;

	private final long timeToLiveInSeconds;

	/**
	 * <p>
	 * FIXME: useful?
	 * </p>
	 * <p>
	 * All elements that are put in the cache will also be pushed to this other cache (ehcache for example).
	 * </p>
	 * <p>
	 * This would make the long maxEntriesInDistantCache obsolete
	 * </p>
	 */
	private Cache backendCache;

	/**
	 * Default constructor.
	 *
	 * @param cacheRegion
	 *            The cache region's name.
	 * @param cacheDao
	 *            Backing Spring Data cache instance.
	 */
	public SpringDataCacheServiceImpl(final String cacheRegion, final SpringDataCacheDao cacheDao, final long timeToLive,
			final EvictionStrategy evictionStrategyType, final long maxEntriesInCache, final boolean eternalCache) {

		Assert.notNull(cacheRegion, "The cache name is required");
		Assert.notNull(cacheDao, "The cache DAO is required");
		Assert.notNull(evictionStrategyType, "The eviction strategy is required");
		Assert.isTrue(timeToLive >= 0, "The TTL mustn't be negative.");
		Assert.isTrue(maxEntriesInCache > 0, "The maximum number of cache entries must be greater than 0.");

		dao = cacheDao;
		name = cacheRegion;
		timeToLiveInSeconds = timeToLive;
		evictionStrategy = evictionStrategyType;
		maxEntries = maxEntriesInCache;
		eternal = eternalCache;
	}

	/**
	 * <p>
	 * Cleans up outdated elements from the cache and frees some other elements if the region's limit has been reached.
	 * </p>
	 * <p>
	 * FIXME: calling this method every time a cache put is done might have performance impacts.
	 * </p>
	 */
	private void cleanupOldCacheValues() {

		if (!eternal) {

			dao.deleteByCreationDateLessThan(new Date().getTime() - timeToLiveInSeconds);

			final long numberOfElementsToDelete = dao.countAll() - maxEntries + 1;

			if (numberOfElementsToDelete >= 0) {

				LOGGER.debug("Cleaning up the {} cache region using the {} eviction type", name, evictionStrategy);

				switch (evictionStrategy) {
					case LFU:
						// TODO
						// dao.deleteAllOrderByCallsAscLimitTo(numberOfElementsToDelete);
						break;

					case LRU:
						// TODO
						// dao.deleteAllOrderByCreationDateAscLimitTo(numberOfElementsToDelete);
						break;
				}
			}
		}
	}

	@Override
	public void clear() {
		dao.deleteAll();
	}

	@Override
	public void evict(final Object key) {

		Assert.isInstanceOf(String.class, key, "The key should be a string");

		dao.deleteByKey((String) key);
	}

	@Override
	public ValueWrapper get(final Object key) {

		Assert.isInstanceOf(String.class, key, "The key should be a string");

		final CacheEntry cachedValue = dao.findByKey((String) key);

		if (cachedValue == null) {
			if (statisticsEnabled) {
				misses++;
			}

			return null;
		}
		else {
			if (statisticsEnabled) {
				hits++;
			}

			return new SimpleValueWrapper(cachedValue);
		}
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public SpringDataCacheDao getNativeCache() {
		return dao;
	}

	@Override
	public void put(final Object key, final Object value) {

		Assert.isInstanceOf(String.class, key, "The key should be a string");
		Assert.isInstanceOf(Serializable.class, value, "The value must be serializable");

		cleanupOldCacheValues();

		CacheEntry cachedValue = dao.findByKey(key.toString());
		final long now = new Date().getTime();

		if (cachedValue == null || now + timeToLiveInSeconds > cachedValue.getCreationDate()) {

			cachedValue = new CacheEntryImpl();
			cachedValue.setCalls(0);
			cachedValue.setCreationDate(now);
			cachedValue.setKey((String) key);
			cachedValue.setValue((Serializable) value);

			dao.save(cachedValue);
		}
		else {
			cachedValue.setCalls(cachedValue.getCalls() + 1);

			dao.save(cachedValue);
		}
	}

	@Override
	public long getHits() {
		return hits;
	}

	@Override
	public long getMisses() {
		return misses;
	}

	@Override
	public boolean areStatisticsEnabled() {
		return statisticsEnabled;
	}

	@Override
	public void setStatisticsEnabled(final boolean enabled) {
		statisticsEnabled = enabled;
	}

}
