package org.ppodgorsek.cache.manager.core.service;

import java.io.Serializable;
import java.util.Date;

import org.ppodgorsek.cache.manager.core.dao.SpringDataCacheDao;
import org.ppodgorsek.cache.manager.core.model.CacheEntry;
import org.ppodgorsek.cache.manager.core.model.CacheEntryImpl;
import org.ppodgorsek.cache.manager.core.model.EvictionStrategyType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.util.Assert;

/**
 * TODO: each cache service should have a local cache that is used in priority, the Spring Data cache should be called asynchronously when persisting an entry
 * in order to maximise performance.
 * 
 * @author Paul Podgorsek
 */
public class SpringDataCacheService implements Cache {

	private static final Logger LOGGER = LoggerFactory.getLogger(SpringDataCacheService.class);

	private SpringDataCacheDao dao;

	private String name;

	private boolean eternal;

	private EvictionStrategyType evictionStrategyType;

	private long maxEntries;

	/**
	 * TODO: check how are stats used by ehcache
	 */
	private boolean statistics;

	private long timeToLiveInSeconds;

	/**
	 * <p>
	 * FIXME: useful?
	 * </p>
	 * <p>
	 * All elements that are put in the cache will also be pushed to this other cache (ehcache for example).
	 * </p>
	 * 1
	 * <p>
	 * This would make the long maxEntriesInDistantCache obsolete
	 * </p>
	 */
	private Cache backendCache;

	/**
	 * Create an {@link SpringDataCacheService} instance.
	 * 
	 * @param cacheDao
	 *            Backing Spring Data cache instance.
	 */
	public SpringDataCacheService(String name, SpringDataCacheDao cacheDao, long timeToLive, EvictionStrategyType evictionStrategyType, long maxEntriesInCache) {

		Assert.notNull(name, "The name is required");
		Assert.notNull(cacheDao, "The cache DAO is required");
		Assert.notNull(evictionStrategyType, "The eviction strategy type is required");

		dao = cacheDao;
		this.name = name;
		timeToLiveInSeconds = timeToLive;
		this.evictionStrategyType = evictionStrategyType;
		maxEntries = maxEntriesInCache;
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

			dao.deleteByLowerCreationDate(new Date().getTime() - timeToLiveInSeconds);

			long numberOfElementsToDelete = dao.countAll() - maxEntries + 1;

			if (numberOfElementsToDelete >= 0) {
				switch (evictionStrategyType) {
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
	public void evict(Object key) {

		Assert.isInstanceOf(String.class, key, "The key should be a string");

		dao.deleteByKey((String) key);
	}

	@Override
	public ValueWrapper get(Object key) {

		Assert.isInstanceOf(String.class, key, "The key should be a string");

		CacheEntry cachedValue = dao.findByKey((String) key);

		if (cachedValue == null) {
			return null;
		}
		else {
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
	public void put(Object key, Object value) {

		Assert.isInstanceOf(String.class, key, "The key should be a string");
		Assert.isInstanceOf(Serializable.class, value, "The value must be serializable");

		cleanupOldCacheValues();

		CacheEntry cachedValue = dao.findByKey(key.toString());
		long now = new Date().getTime();

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

}
