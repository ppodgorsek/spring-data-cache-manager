package org.ppodgorsek.cache.manager.cassandra.adapter;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.ppodgorsek.cache.manager.cassandra.CassandraCacheMetadata;
import org.ppodgorsek.cache.manager.cassandra.dao.CassandraSpringDataCacheDao;
import org.ppodgorsek.cache.manager.core.adapter.SpringDataCacheAdapter;
import org.ppodgorsek.cache.manager.core.annotation.CacheAdapter;
import org.ppodgorsek.cache.manager.core.context.SpringDataCacheNamingPolicy;
import org.ppodgorsek.cache.manager.core.dao.SpringDataCacheDao;
import org.ppodgorsek.cache.manager.core.model.CacheRegionConfiguration;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.data.mapping.context.MappingContext;

/**
 * @author Paul Podgorsek
 */
@CacheAdapter(CassandraCacheMetadata.CACHE_TYPE)
public class CassandraCacheAdapter implements SpringDataCacheAdapter {

	private final Map<String, MappingContext> mappingContexts = new HashMap<String, MappingContext>();

	/**
	 * Default constructor.
	 */
	public CassandraCacheAdapter() {
		super();
	}

	@PostConstruct
	public void init() {

		// TODO: fetch the different mapping contexts beans
		// These beans will then be stored in the local map and used according to the region and type of cache.

	}

	@Override
	public SpringDataCacheDao createDao(final CacheRegionConfiguration configuration) {
		// TODO Auto-generated method stub

		return generateEnhancedObject(configuration.getType(), configuration.getName());
	}

	@SuppressWarnings("unchecked")
	private <T> T generateEnhancedObject(final String cacheType, final String cacheName) {

		final Enhancer enhancer = new Enhancer();
		enhancer.setInterfaces(new Class<?>[] { CassandraSpringDataCacheDao.class });
		enhancer.setNamingPolicy(new SpringDataCacheNamingPolicy(cacheType, cacheName));

		return (T) enhancer.create();
	}

}
