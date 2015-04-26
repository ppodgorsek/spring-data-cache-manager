package org.ppodgorsek.cache.manager.cassandra.dao;

import org.ppodgorsek.cache.manager.core.annotation.CacheDao;
import org.ppodgorsek.cache.manager.core.dao.SpringDataCacheDao;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * @author Paul Podgorsek
 */
@CacheDao("cassandra")
@NoRepositoryBean
public interface CassandraSpringDataCacheDao extends SpringDataCacheDao {

}
