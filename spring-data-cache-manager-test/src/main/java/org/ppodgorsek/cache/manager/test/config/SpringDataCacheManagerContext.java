package org.ppodgorsek.cache.manager.test.config;

import org.ppodgorsek.cache.manager.core.manager.SpringDataCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Paul Podgorsek
 */
@Configuration
public class SpringDataCacheManagerContext {

	@Bean
	public SpringDataCacheManager getSpringDataCacheManager() {
		return new SpringDataCacheManager();
	}

}
