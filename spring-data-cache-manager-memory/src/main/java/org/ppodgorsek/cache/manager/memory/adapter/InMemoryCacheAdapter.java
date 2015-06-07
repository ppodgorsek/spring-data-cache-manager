package org.ppodgorsek.cache.manager.memory.adapter;

import org.ppodgorsek.cache.manager.core.adapter.SpringDataCacheAdapter;
import org.ppodgorsek.cache.manager.core.annotation.CacheAdapter;
import org.ppodgorsek.cache.manager.core.dao.SpringDataCacheDao;
import org.ppodgorsek.cache.manager.core.model.CacheRegionConfiguration;
import org.ppodgorsek.cache.manager.memory.InMemoryCacheMetadata;
import org.ppodgorsek.cache.manager.memory.dao.impl.InMemoryCacheDao;

/**
 * @author Paul Podgorsek
 */
@CacheAdapter(InMemoryCacheMetadata.CACHE_TYPE)
public class InMemoryCacheAdapter implements SpringDataCacheAdapter {

	@Override
	public SpringDataCacheDao createDao(final CacheRegionConfiguration configuration) {
		return new InMemoryCacheDao(configuration.getMaxEntriesInCache());
	}

}
