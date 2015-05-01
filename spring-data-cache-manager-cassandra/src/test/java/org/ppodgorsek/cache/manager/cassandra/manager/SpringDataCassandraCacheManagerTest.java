package org.ppodgorsek.cache.manager.cassandra.manager;

import org.junit.Before;
import org.junit.Test;
import org.ppodgorsek.cache.manager.core.manager.SpringDataCacheManager;

/**
 * Tests the Spring Data cache manager with Cassandra.
 *
 * @author Paul Podgorsek
 */
public class SpringDataCassandraCacheManagerTest {

	private SpringDataCacheManager springDataCacheManager = new SpringDataCacheManager();

	@Before
	public void initValues() {
		springDataCacheManager.afterPropertiesSet();
	}

	@Test
	public void test() {

	}

}
