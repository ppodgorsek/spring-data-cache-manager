package org.ppodgorsek.cache.manager.ehcache.config;

import java.util.List;

import org.ppodgorsek.cache.manager.core.config.ConfigurationLoader;
import org.ppodgorsek.cache.manager.core.model.CacheRegionConfiguration;

/**
 * FIXME: move this to a separate module? (could be added on the classpath only if required by ehcache users)
 * 
 * @author Paul Podgorsek
 */
public class EhcacheConfigurationLoader implements ConfigurationLoader {

	/**
	 * 
	 */
	public EhcacheConfigurationLoader() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<CacheRegionConfiguration> getCacheRegionConfigurations() {
		// TODO Auto-generated method stub
		return null;
	}

}
