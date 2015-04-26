package org.ppodgorsek.cache.manager.core.dao;

import org.ppodgorsek.cache.manager.core.model.CacheEntry;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

/**
 * DAO used to interact with a cache region using Spring Data.
 * 
 * @author Paul Podgorsek
 */
@NoRepositoryBean
public interface SpringDataCacheDao extends Repository<CacheEntry, String> {

	/**
	 * Counts the total number of entries in the cache.
	 * 
	 * @return The total number of entries in the cache region.
	 */
	long countAll();

	/**
	 * Deletes all entries from the cache region.
	 */
	void deleteAll();

	/**
	 * Deletes a cache entry by its key.
	 * 
	 * @param key
	 *            The cache entry's key (mustn't be <code>null</code>).
	 */
	void deleteByKey(String key);

	/**
	 * Deletes all entries that are older than a date.
	 * 
	 * @param date
	 *            The date before which the entries must be deleted (mustn't be <code>null</code>).
	 */
	void deleteByLowerCreationDate(long date);

	/**
	 * Finds a cache entry by its key.
	 * 
	 * @param key
	 *            The cache entry's key.
	 * @return The cache entry that has the provided key, or <code>null</code> if there isn't one.
	 */
	CacheEntry findByKey(String key);

	/**
	 * Persists a cache entry.
	 * 
	 * @param entity
	 *            The entry that must be persisted.
	 * @return The persisted cache entry.
	 */
	CacheEntry save(CacheEntry entity);

}
