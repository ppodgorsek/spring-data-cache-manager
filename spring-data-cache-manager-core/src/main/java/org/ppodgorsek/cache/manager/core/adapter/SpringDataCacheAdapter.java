package org.ppodgorsek.cache.manager.core.adapter;

import org.ppodgorsek.cache.manager.core.model.CacheRegionConfiguration;

/**
 * @author Paul Podgorsek
 */
public interface SpringDataCacheAdapter {

	// TODO: declare the DAO in the according Spring Data mapping context (Cassandra, Redis, etc)
	void register(CacheRegionConfiguration configuration);

}
