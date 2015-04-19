package org.ppodgorsek.cache.manager.cassandra.dao;

import java.io.Serializable;

import org.ppodgorsek.cache.manager.cassandra.model.CassandraCachedValue;
import org.ppodgorsek.cache.manager.core.dao.SpringDataCacheDao;

/**
 * @author Paul Podgorsek
 */
public interface CassandraSpringDataCacheDao<VALUE extends Serializable> extends SpringDataCacheDao<CassandraCachedValue<VALUE>> {

}
