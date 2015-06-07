package org.ppodgorsek.cache.manager.ehcache.config;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.Configuration;
import net.sf.ehcache.config.ConfigurationFactory;
import net.sf.ehcache.store.MemoryStoreEvictionPolicy;

import org.ppodgorsek.cache.manager.core.config.ConfigurationLoader;
import org.ppodgorsek.cache.manager.core.model.CacheRegionConfiguration;
import org.ppodgorsek.cache.manager.core.model.EvictionStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 * Loads the cache configuration using an Ehcache configuration file.
 *
 * @author Paul Podgorsek
 */
public class EhcacheConfigurationLoader implements ConfigurationLoader {

	private static final Logger LOGGER = LoggerFactory.getLogger(EhcacheConfigurationLoader.class);

	private static final EvictionStrategy DEFAULT_EVICTION_STRATEGY = EvictionStrategy.FIFO;

	private final String cacheType;

	private final Configuration configuration;

	/**
	 * Default constructor.
	 */
	public EhcacheConfigurationLoader(final String defaultCacheType, final File ehcacheConfigurationFile) {

		super();

		Assert.notNull(defaultCacheType, "The default cache type is required");
		Assert.notNull(ehcacheConfigurationFile, "Ehcache's configuration file is required");

		cacheType = defaultCacheType;
		configuration = ConfigurationFactory.parseConfiguration(ehcacheConfigurationFile);
	}

	@Override
	public Collection<CacheRegionConfiguration> getCacheRegionConfigurations() {

		final List<CacheRegionConfiguration> configurations = new ArrayList<CacheRegionConfiguration>();

		for (final CacheConfiguration config : configuration.getCacheConfigurations().values()) {

			final CacheRegionConfiguration cacheRegionConfiguration = new CacheRegionConfiguration();
			cacheRegionConfiguration.setEternal(config.isEternal());
			cacheRegionConfiguration.setEvictionStrategyType(getEvictionStrategy(config));
			cacheRegionConfiguration.setMaxEntriesInCache(config.getMaxEntriesLocalHeap());
			cacheRegionConfiguration.setName(config.getName());
			cacheRegionConfiguration.setTimeToLive(config.getTimeToLiveSeconds());
			cacheRegionConfiguration.setType(cacheType);

			configurations.add(cacheRegionConfiguration);
		}

		return configurations;
	}

	private EvictionStrategy getEvictionStrategy(final CacheConfiguration configuration) {

		final MemoryStoreEvictionPolicy evictionPolicy = configuration.getMemoryStoreEvictionPolicy();

		if (evictionPolicy != null) {
			if (MemoryStoreEvictionPolicy.LRU.equals(evictionPolicy)) {
				return EvictionStrategy.LRU;
			}
			else if (MemoryStoreEvictionPolicy.LFU.equals(evictionPolicy)) {
				return EvictionStrategy.LFU;
			}
			else if (MemoryStoreEvictionPolicy.FIFO.equals(evictionPolicy)) {
				return EvictionStrategy.FIFO;
			}
		}

		LOGGER.warn("The MemoryStoreEvictionPolicy of {} cannot be resolved. The policy will be set to {}", evictionPolicy, DEFAULT_EVICTION_STRATEGY);

		return DEFAULT_EVICTION_STRATEGY;
	}

}
