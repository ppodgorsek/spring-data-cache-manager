package org.ppodgorsek.cache.manager.core.config;

import java.util.List;

import org.ppodgorsek.cache.manager.core.model.CacheRegionConfiguration;

/**
 * @author Paul Podgorsek
 */
public interface ConfigurationLoader {

	List<CacheRegionConfiguration> getCacheRegionConfigurations();

}
