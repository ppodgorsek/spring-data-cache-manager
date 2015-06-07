package org.ppodgorsek.cache.manager.cassandra.dao;

import org.ppodgorsek.cache.manager.core.dao.SpringDataCacheDao;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * @author Paul Podgorsek
 */
@NoRepositoryBean
public interface CassandraCacheDao extends SpringDataCacheDao {

}
