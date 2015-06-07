package org.ppodgorsek.cache.manager.memory.dao.impl;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.ppodgorsek.cache.manager.core.dao.SpringDataCacheDao;
import org.ppodgorsek.cache.manager.core.model.CacheEntry;

/**
 * A DAO that stores cache entries in memory.
 *
 * @author Paul Podgorsek
 */
public class InMemoryCacheDao implements SpringDataCacheDao {

	private final Map<String, CacheEntry> cacheEntries;

	private final Queue<CacheEntry> cacheQueue;

	/**
	 * Default constructor.
	 */
	public InMemoryCacheDao() {

		super();

		cacheEntries = new ConcurrentHashMap<String, CacheEntry>();
		cacheQueue = new ConcurrentLinkedQueue<CacheEntry>();
	}

	/**
	 * Constructor allowing to set the cache's initial capacity.
	 *
	 * @param initialCapacity
	 *            The cache's initial capacity.
	 */
	public InMemoryCacheDao(final int initialCapacity) {

		super();

		cacheEntries = new ConcurrentHashMap<String, CacheEntry>(initialCapacity);
		cacheQueue = new ConcurrentLinkedQueue<CacheEntry>();
	}

	@Override
	public long countAll() {
		return cacheEntries.size();
	}

	@Override
	public void deleteAll() {
		cacheEntries.clear();
		cacheQueue.clear();
	}

	@Override
	public void deleteByKey(final String key) {

		final CacheEntry cache = cacheEntries.remove(key);
		cacheQueue.remove(cache);
	}

	@Override
	public void deleteByCreationDateLessThan(final long date) {

		CacheEntry cache = cacheQueue.peek();

		while (cache.getCreationDate() < date) {
			cacheQueue.poll();
			cache = cacheQueue.peek();
		}
	}

	@Override
	public CacheEntry findByKey(final String key) {

		final CacheEntry cache = cacheEntries.get(key);
		cache.setCalls(cache.getCalls() + 1);

		return cache;
	}

	@Override
	public CacheEntry save(final CacheEntry entity) {

		final String key = entity.getKey();

		if (cacheEntries.containsKey(key)) {
			// We remove the entity from the queue to take into consideration its new creation date.
			cacheQueue.remove(entity);
		}

		cacheEntries.put(key, entity);
		cacheQueue.offer(entity);

		return entity;
	}

}
