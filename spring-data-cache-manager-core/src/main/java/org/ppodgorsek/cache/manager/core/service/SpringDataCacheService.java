package org.ppodgorsek.cache.manager.core.service;

import java.io.Serializable;
import java.util.Date;

import org.ppodgorsek.cache.manager.core.dao.SpringDataCacheDao;
import org.ppodgorsek.cache.manager.core.model.CachedValue;
import org.ppodgorsek.cache.manager.core.model.EvictionStrategyType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.util.Assert;

/**
 * @author Paul Podgorsek
 */
public class SpringDataCacheService implements Cache {

	private static final Logger LOGGER = LoggerFactory.getLogger(SpringDataCacheService.class);

	private SpringDataCacheDao<CachedValue> dao;

	private Class<? extends CachedValue> entityType;

	private boolean eternal;

	private EvictionStrategyType evictionStrategyType;

	private long maxElementsInLocalCache;

	private long maxEntriesInDistantCache;

	private boolean overflowToDisk;

	private boolean statistics;

	private long timeToLiveInSeconds;

	/**
	 * Create an {@link SpringDataCacheService} instance.
	 *
	 * @param cacheDao
	 *            Backing Spring Data cache instance.
	 */
	public SpringDataCacheService(SpringDataCacheDao<CachedValue> cacheDao, Class<? extends CachedValue> clazz, long timeToLive,
			EvictionStrategyType evictionStrategyType, long maxEntries) {

		Assert.notNull(cacheDao, "The cache DAO is required");
		Assert.notNull(clazz, "The entity type is required");
		Assert.notNull(evictionStrategyType, "The eviction strategy type is required");

		dao = cacheDao;
		entityType = clazz;
		timeToLiveInSeconds = timeToLive;
		this.evictionStrategyType = evictionStrategyType;
		maxEntriesInDistantCache = maxEntries;
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

			long numberOfElementsToDelete = dao.countAll() - maxEntriesInDistantCache + 1;

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

		dao.deleteById((String) key);
	}

	@Override
	public ValueWrapper get(Object key) {

		Assert.isInstanceOf(String.class, key, "The key should be a string");

		CachedValue cachedValue = dao.findById((String) key);

		if (cachedValue == null) {
			return null;
		}
		else {
			return new SimpleValueWrapper(cachedValue);
		}
	}

	@Override
	public String getName() {
		return entityType.getSimpleName();
	}

	@Override
	public SpringDataCacheDao<CachedValue> getNativeCache() {
		return dao;
	}

	@Override
	public void put(Object key, Object value) {

		Assert.isInstanceOf(String.class, key, "The key should be a string");
		Assert.isInstanceOf(Serializable.class, value, "The value must be serializable");

		cleanupOldCacheValues();

		CachedValue cachedValue = dao.findById(key.toString());
		long now = new Date().getTime();

		if (cachedValue == null || now + timeToLiveInSeconds > cachedValue.getCreationDate()) {

			try {
				cachedValue = entityType.newInstance();
				cachedValue.setCalls(0);
				cachedValue.setCreationDate(now);
				cachedValue.setId((String) key);
				cachedValue.setValue((Serializable) value);

				dao.save(cachedValue);
			}
			catch (InstantiationException | IllegalAccessException e) {
				LOGGER.error("Impossible to put element '{}' in the cache: {}", key, e.getMessage());
			}
		}
		else {
			cachedValue.setCalls(cachedValue.getCalls() + 1);

			dao.save(cachedValue);
		}
	}

}
