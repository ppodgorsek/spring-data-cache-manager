package org.ppodgorsek.cache.manager.ehcache.config;

import java.util.List;

import org.ppodgorsek.cache.manager.core.config.ConfigurationLoader;
import org.ppodgorsek.cache.manager.core.model.CacheRegionConfiguration;

/**
 * This module should be added to the classpath only if required by ehcache users.
 *
 * @author Paul Podgorsek
 */
public class EhcacheConfigurationLoader implements ConfigurationLoader {

	/**
	 * Default constructor.
	 */
	public EhcacheConfigurationLoader() {
		super();
	}

	@Override
	public List<CacheRegionConfiguration> getCacheRegionConfigurations() {
		// TODO Auto-generated method stub
		return null;
	}

}
