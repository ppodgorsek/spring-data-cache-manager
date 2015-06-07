package org.ppodgorsek.cache.manager.core.manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.ppodgorsek.cache.manager.core.adapter.SpringDataCacheAdapter;
import org.ppodgorsek.cache.manager.core.annotation.CacheAdapter;
import org.ppodgorsek.cache.manager.core.config.ConfigurationLoader;
import org.ppodgorsek.cache.manager.core.config.impl.SpringDataCacheConfigurationLoader;
import org.ppodgorsek.cache.manager.core.config.impl.SpringDataCachePackageScanner;
import org.ppodgorsek.cache.manager.core.dao.SpringDataCacheDao;
import org.ppodgorsek.cache.manager.core.model.CacheRegionConfiguration;
import org.ppodgorsek.cache.manager.core.service.impl.SpringDataCacheServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.transaction.AbstractTransactionSupportingCacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;

/**
 * Cache manager backed by Spring Data. Once the cache manager has been created, the definitions for the different cache regions are loaded from an XML
 * configuration file.
 *
 * @author Paul Podgorsek
 */
public class SpringDataCacheManager extends AbstractTransactionSupportingCacheManager implements CacheManager, ApplicationContextAware {

	private static final Logger LOGGER = LoggerFactory.getLogger(SpringDataCacheManager.class);

	private static final String DEFAULT_BASE_PACKAGE = "org.ppodgorsek.cache.manager";

	private ApplicationContext applicationContext;

	private ConfigurationLoader configurationLoader;

	private final Set<String> basePackages;

	/**
	 * Default constructor.
	 */
	public SpringDataCacheManager() {
		this(null);
	}

	/**
	 * Constructor that allows to set the configuration loader.
	 *
	 * @param configurationLoader
	 *            The configuration loader.
	 */
	public SpringDataCacheManager(final ConfigurationLoader configurationLoader) {
		this(configurationLoader, Collections.singleton(DEFAULT_BASE_PACKAGE));
	}

	/**
	 * Constructor that allows to set the configuration loader and the packages that will be scanned.
	 *
	 * @param configurationLoader
	 *            The configuration loader.
	 * @param basePackages
	 *            The packages that must be scanned in order to find the DAOs and adapters.
	 */
	public SpringDataCacheManager(final ConfigurationLoader configurationLoader, final Set<String> basePackages) {

		super();

		Assert.notNull(basePackages, "The base packages are required");

		this.basePackages = basePackages;
		this.configurationLoader = configurationLoader;
	}

	/**
	 * If no configuration loader was defined, a default one will be created which will look up cache configuration beans in the application context.
	 */
	@PostConstruct
	public void initValues() {

		if (configurationLoader == null) {
			configurationLoader = new SpringDataCacheConfigurationLoader(applicationContext);
		}
	}

	@Override
	protected Collection<Cache> loadCaches() {

		final Collection<Cache> caches = new ArrayList<Cache>();
		final Map<String, Class<? extends SpringDataCacheAdapter>> cacheAdapters = findSupportedCacheAdapters(basePackages);

		for (final CacheRegionConfiguration cacheRegionConfiguration : configurationLoader.getCacheRegionConfigurations()) {

			LOGGER.debug("Creating a new cache region: {}", cacheRegionConfiguration);

			final String cacheName = cacheRegionConfiguration.getName();
			final SpringDataCacheAdapter cacheAdapter = instantiateObject(cacheAdapters, cacheRegionConfiguration.getType(), cacheName);
			final SpringDataCacheDao dao = cacheAdapter.createDao(cacheRegionConfiguration);

			final Cache springDataCache = new SpringDataCacheServiceImpl(cacheName, dao, cacheRegionConfiguration.getTimeToLive(),
					cacheRegionConfiguration.getEvictionStrategy(), cacheRegionConfiguration.getMaxEntriesInCache(), cacheRegionConfiguration.isEternal());

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
	private Map<String, Class<? extends SpringDataCacheAdapter>> findSupportedCacheAdapters(final Set<String> packages) {

		final Map<String, Class<? extends SpringDataCacheAdapter>> supportedAdapters = new HashMap<String, Class<? extends SpringDataCacheAdapter>>();

		final SpringDataCachePackageScanner packageScanner = new SpringDataCachePackageScanner(packages);
		packageScanner.addInterface(SpringDataCacheAdapter.class);
		packageScanner.addAnnotation(CacheAdapter.class);

		for (final Class<?> clazz : packageScanner.scanForClasses()) {

			final CacheAdapter annotation = clazz.getAnnotation(CacheAdapter.class);
			final String type = annotation.value();

			supportedAdapters.put(type, (Class<SpringDataCacheAdapter>) clazz);
		}

		return supportedAdapters;
	}

	private <T> T instantiateObject(final Map<String, Class<? extends T>> classes, final String cacheType, final String cacheName) {

		final Class<? extends T> clazz = classes.get(cacheType);

		Assert.notNull(clazz, "Unknown cache type: " + cacheType);

		try {
			return clazz.newInstance();
		}
		catch (InstantiationException | IllegalAccessException e) {
			LOGGER.error("Impossible to create a new instance of the {} class: {}", clazz, e.getMessage());
			throw new IllegalArgumentException("A cache adapter couldn't be instantiated", e);
		}
	}

	@Override
	public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

}
