package org.ppodgorsek.cache.manager.core.dao;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

/**
 * @author Paul Podgorsek
 */
@NoRepositoryBean
public interface SpringDataCacheDao<TYPE> extends Repository<TYPE, String> {

	long countAll();

	void deleteAll();

	void deleteById(String id);

	void deleteByLowerCreationDate(long date);

	TYPE findById(String id);

	TYPE save(TYPE entity);

}
