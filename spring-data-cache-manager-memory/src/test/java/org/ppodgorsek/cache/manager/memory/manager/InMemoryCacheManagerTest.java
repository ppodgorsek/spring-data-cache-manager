package org.ppodgorsek.cache.manager.memory.manager;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.ppodgorsek.cache.manager.core.manager.SpringDataCacheManager;
import org.ppodgorsek.cache.manager.memory.config.InMemoryCacheRegionConfigurationContext;
import org.ppodgorsek.cache.manager.test.config.SpringDataCacheManagerContext;
import org.springframework.cache.Cache;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Tests the cache manager in memory.
 *
 * @author Paul Podgorsek
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { SpringDataCacheManagerContext.class, InMemoryCacheRegionConfigurationContext.class })
public class InMemoryCacheManagerTest {

	@Resource
	private SpringDataCacheManager springDataCacheManager;

	@Test
	public void getExistingCache() {

		final Cache cache = springDataCacheManager.getCache("user");

		assertNotNull("The cache should exist", cache);
	}

	@Test
	public void getUnknownCache() {

		final Cache cache = springDataCacheManager.getCache("unknown-cache");

		assertNull("The cache shouldn't exist", cache);
	}

	@Test
	public void test() {

		final Cache cache = springDataCacheManager.getCache("user");

		cache.clear();
	}

}
