package org.ppodgorsek.cache.manager.core.config.impl;

import java.util.Collection;
import java.util.Map;

import org.ppodgorsek.cache.manager.core.config.ConfigurationLoader;
import org.ppodgorsek.cache.manager.core.model.CacheRegionConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;

/**
 * @author Paul Podgorsek
 */
public class SpringDataCacheConfigurationLoader implements ConfigurationLoader {

	private final ApplicationContext applicationContext;

	/**
	 * Default constructor.
	 * 
	 * @param applicationContext
	 *            The application context in which the configuration beans will be looked up.
	 */
	public SpringDataCacheConfigurationLoader(final ApplicationContext applicationContext) {

		super();

		Assert.notNull(applicationContext, "The application context is required");

		this.applicationContext = applicationContext;
	}

	@Override
	public Collection<CacheRegionConfiguration> getCacheRegionConfigurations() {

		final Map<String, CacheRegionConfiguration> configurations = applicationContext.getBeansOfType(CacheRegionConfiguration.class);

		return configurations.values();
	}

}
