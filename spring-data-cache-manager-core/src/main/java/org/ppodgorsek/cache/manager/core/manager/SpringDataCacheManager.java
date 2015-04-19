package org.ppodgorsek.cache.manager.core.manager;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.transaction.AbstractTransactionSupportingCacheManager;
import org.springframework.util.Assert;

/**
 * CacheManager backed by Spring Data.
 *
 * @author Paul Podgorsek
 */
public class SpringDataCacheManager extends AbstractTransactionSupportingCacheManager implements CacheManager {

	private Map<String, Cache> cacheMap;

	@Override
	public Cache getCache(String name) {

		Cache cache = super.getCache(name);

		if (cache == null) {
			// Check the caches again (in case the cache was added at runtime)
			cache = cacheMap.get(name);

			if (cache != null) {
				addCache(cache);
				cache = super.getCache(name); // Potentially decorated
			}
		}

		return cache;
	}

	@Override
	public Collection<String> getCacheNames() {
		return cacheMap.keySet();
	}

	public Map<String, Cache> getCaches() {
		return cacheMap;
	}

	@Override
	protected Collection<Cache> loadCaches() {

		Assert.notNull(cacheMap, "A list of Spring Data cache services is required");

		Collection<Cache> caches = cacheMap.values();

		for (Cache cache : caches) {
			addCache(cache);
		}

		return caches;
	}

	@Required
	public void setCaches(List<Cache> caches) {

		Assert.notNull(caches, "The list of caches is required");

		Map<String, Cache> cacheServicesMap = new HashMap<String, Cache>();

		for (Cache cacheService : caches) {
			cacheServicesMap.put(cacheService.getName(), cacheService);
		}

		cacheMap = cacheServicesMap;
	}

}
