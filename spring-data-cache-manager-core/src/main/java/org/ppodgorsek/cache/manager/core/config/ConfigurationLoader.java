package org.ppodgorsek.cache.manager.core.config;

import java.util.Collection;

import org.ppodgorsek.cache.manager.core.model.CacheRegionConfiguration;

/**
 * @author Paul Podgorsek
 */
public interface ConfigurationLoader {

	Collection<CacheRegionConfiguration> getCacheRegionConfigurations();

}
