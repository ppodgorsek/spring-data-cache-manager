package org.ppodgorsek.cache.manager.cassandra.config;

import org.ppodgorsek.cache.manager.cassandra.CassandraCacheMetadata;
import org.ppodgorsek.cache.manager.core.model.CacheRegionConfiguration;
import org.ppodgorsek.cache.manager.core.model.EvictionStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Paul Podgorsek
 */
@Configuration
public class CassandraCacheRegionConfigurationContext {

	@Bean
	public CacheRegionConfiguration getUserCacheRegion() {

		final CacheRegionConfiguration cacheRegionConfiguration = new CacheRegionConfiguration();
		cacheRegionConfiguration.setEvictionStrategy(EvictionStrategy.LFU);
		cacheRegionConfiguration.setMaxEntriesInCache(20);
		cacheRegionConfiguration.setName("User");
		cacheRegionConfiguration.setTimeToLive(600);
		cacheRegionConfiguration.setType(CassandraCacheMetadata.CACHE_TYPE);

		return cacheRegionConfiguration;
	}

}
