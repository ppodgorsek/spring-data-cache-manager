package org.ppodgorsek.cache.manager.cassandra.adapter;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.ppodgorsek.cache.manager.core.adapter.SpringDataCacheAdapter;
import org.ppodgorsek.cache.manager.core.annotation.CacheAdapter;
import org.ppodgorsek.cache.manager.core.model.CacheRegionConfiguration;
import org.springframework.data.mapping.context.MappingContext;

/**
 * @author Paul Podgorsek
 */
@CacheAdapter("cassandra")
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
	public void register(CacheRegionConfiguration configuration) {
		// TODO Auto-generated method stub

	}

}
