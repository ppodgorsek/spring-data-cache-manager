package org.ppodgorsek.cache.manager.core.config;

import java.util.ArrayList;
import java.util.List;

import org.ppodgorsek.cache.manager.core.model.CacheRegionConfiguration;
import org.ppodgorsek.cache.manager.core.model.EvictionStrategyType;

/**
 * @author Paul Podgorsek
 */
public class SpringDataCacheConfigurationLoader implements ConfigurationLoader {

	private static final String DEFAULT_FILE_LOCATION = "classpath:config/spring-data-cache-configuration.xml";

	/**
	 * Default constructor.
	 */
	public SpringDataCacheConfigurationLoader() {
		super();
	}

	@Override
	public List<CacheRegionConfiguration> getCacheRegionConfigurations() {

		List<CacheRegionConfiguration> cacheRegionConfigurations = new ArrayList<CacheRegionConfiguration>();

		// FIXME: this is just a cache that has been manually created, it must be removed when the config file is properly loaded.

		CacheRegionConfiguration cacheRegionConfiguration = new CacheRegionConfiguration();
		cacheRegionConfiguration.setEvictionStrategyType(EvictionStrategyType.LFU);
		cacheRegionConfiguration.setMaxEntriesInCache(20);
		cacheRegionConfiguration.setName("User");
		cacheRegionConfiguration.setTimeToLive(600);
		cacheRegionConfiguration.setType("cassandra");

		cacheRegionConfigurations.add(cacheRegionConfiguration);

		return cacheRegionConfigurations;
	}

}
