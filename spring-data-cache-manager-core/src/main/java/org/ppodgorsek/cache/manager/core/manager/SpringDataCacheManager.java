package org.ppodgorsek.cache.manager.core.manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.ppodgorsek.cache.manager.core.adapter.SpringDataCacheAdapter;
import org.ppodgorsek.cache.manager.core.annotation.CacheAdapter;
import org.ppodgorsek.cache.manager.core.annotation.CacheDao;
import org.ppodgorsek.cache.manager.core.config.ConfigurationLoader;
import org.ppodgorsek.cache.manager.core.config.SpringDataCacheConfigurationLoader;
import org.ppodgorsek.cache.manager.core.config.SpringDataCachePackageScanner;
import org.ppodgorsek.cache.manager.core.context.SpringDataCacheNamingPolicy;
import org.ppodgorsek.cache.manager.core.dao.SpringDataCacheDao;
import org.ppodgorsek.cache.manager.core.model.CacheRegionConfiguration;
import org.ppodgorsek.cache.manager.core.service.SpringDataCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.transaction.AbstractTransactionSupportingCacheManager;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.util.Assert;

/**
 * Cache manager backed by Spring Data. Once the cache manager has been created, the definitions for the different cache regions are loaded from an XML
 * configuration file.
 * 
 * @author Paul Podgorsek
 */
public class SpringDataCacheManager extends AbstractTransactionSupportingCacheManager implements CacheManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(SpringDataCacheManager.class);

	private static final String DEFAULT_BASE_PACKAGE = "org.ppodgorsek.cache.manager";

	private final ConfigurationLoader configurationLoader;

	private Set<String> basePackages;

	/**
	 * Constructor that uses a default configuration loader.
	 */
	public SpringDataCacheManager() {
		this(new SpringDataCacheConfigurationLoader());
	}

	/**
	 * Constructor that allows to set the configuration loader.
	 * 
	 * @param configurationLoader
	 *            The configuration loader (mustn't be <code>null</code>).
	 */
	public SpringDataCacheManager(final ConfigurationLoader configurationLoader) {
		this(configurationLoader, Collections.singleton(DEFAULT_BASE_PACKAGE));
	}

	/**
	 * Constructor that allows to set the configuration loader.
	 * 
	 * @param configurationLoader
	 *            The configuration loader (mustn't be <code>null</code>).
	 * @param basePackages
	 *            The packages that must be scanned in order to find the DAOs and adapters.
	 */
	public SpringDataCacheManager(final ConfigurationLoader configurationLoader, Set<String> basePackages) {

		super();

		Assert.notNull(configurationLoader, "The configuration loader is required");
		Assert.notNull(basePackages, "The base packages are required");

		this.configurationLoader = configurationLoader;
		this.basePackages = basePackages;
	}

	@Override
	protected Collection<Cache> loadCaches() {

		final Collection<Cache> caches = new ArrayList<Cache>();
		final Map<String, Class<? extends SpringDataCacheDao>> daos = findSupportedDaos(basePackages);
		final Map<String, Class<? extends SpringDataCacheAdapter>> cacheAdapters = findSupportedCacheAdapters(basePackages);

		for (final CacheRegionConfiguration cacheRegionConfiguration : configurationLoader.getCacheRegionConfigurations()) {

			LOGGER.debug("Creating a new cache region: {}", cacheRegionConfiguration);

			final String cacheType = cacheRegionConfiguration.getType();
			final String cacheName = cacheRegionConfiguration.getName();
			final SpringDataCacheDao dao = generateEnhancedObject(daos, cacheType, cacheName);
			final SpringDataCacheAdapter cacheAdapter = generateEnhancedObject(cacheAdapters, cacheType, cacheName);

			cacheAdapter.register(cacheRegionConfiguration);

			final Cache springDataCache = new SpringDataCacheService(cacheName, dao, cacheRegionConfiguration.getTimeToLive(),
					cacheRegionConfiguration.getEvictionStrategyType(), cacheRegionConfiguration.getMaxEntriesInCache());

			addCache(springDataCache);
			caches.add(getCache(cacheName));
		}

		return caches;
	}

	/**
	 * Fetches the supported cache adapters found in the classpath (ones that implement {@link SpringDataCacheAdapter}). The keys of the map represent the type
	 * of cache, based on the adapter's name.
	 * 
	 * @param packages
	 *            The packages that must be scanned.
	 * @return The map of supported adapters found in the provided classpath packages, or an empty map if there are none.
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Class<? extends SpringDataCacheAdapter>> findSupportedCacheAdapters(Set<String> packages) {

		Map<String, Class<? extends SpringDataCacheAdapter>> supportedAdapters = new HashMap<String, Class<? extends SpringDataCacheAdapter>>();

		SpringDataCachePackageScanner packageScanner = new SpringDataCachePackageScanner(packages);
		packageScanner.addInterface(SpringDataCacheAdapter.class);
		packageScanner.addAnnotation(CacheAdapter.class);

		Set<Class<?>> classes = packageScanner.scanForClasses();

		for (Class<?> clazz : classes) {

			CacheAdapter annotation = clazz.getAnnotation(CacheAdapter.class);
			String type = annotation.value();

			supportedAdapters.put(type, (Class<SpringDataCacheAdapter>) clazz);
		}

		return supportedAdapters;
	}

	/**
	 * Fetches the supported DAOs found in the classpath (ones that extend or implement {@link SpringDataCacheDao}). The keys of the map represent the type of
	 * cache, based on the DAO's name.
	 * 
	 * @param packages
	 *            The packages that must be scanned.
	 * @return The map of supported DAOs found in the provided classpath packages, or an empty map if there are none.
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Class<? extends SpringDataCacheDao>> findSupportedDaos(Set<String> packages) {

		Map<String, Class<? extends SpringDataCacheDao>> supportedDaos = new HashMap<String, Class<? extends SpringDataCacheDao>>();

		SpringDataCachePackageScanner packageScanner = new SpringDataCachePackageScanner(packages);
		packageScanner.addInterface(SpringDataCacheDao.class);
		packageScanner.addAnnotation(CacheDao.class);

		Set<Class<?>> classes = packageScanner.scanForClasses();

		for (Class<?> clazz : classes) {

			CacheAdapter annotation = clazz.getAnnotation(CacheAdapter.class);
			String type = annotation.value();

			supportedDaos.put(type, (Class<SpringDataCacheDao>) clazz);
		}

		return supportedDaos;
	}

	@SuppressWarnings("unchecked")
	private <T> T generateEnhancedObject(Map<String, Class<? extends T>> classes, String cacheType, String cacheName) {

		final Class<? extends T> clazz = classes.get(cacheType);

		Assert.notNull(clazz, "Unknown cache type: " + cacheType);

		final Enhancer enhancer = new Enhancer();
		enhancer.setInterfaces(new Class<?>[] { clazz });
		enhancer.setNamingPolicy(new SpringDataCacheNamingPolicy(cacheType, cacheName));

		return (T) enhancer.create();
	}

}
