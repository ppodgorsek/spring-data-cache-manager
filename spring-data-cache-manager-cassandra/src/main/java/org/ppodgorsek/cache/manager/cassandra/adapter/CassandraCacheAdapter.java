package org.ppodgorsek.cache.manager.cassandra.adapter;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.ppodgorsek.cache.manager.cassandra.CassandraCacheMetadata;
import org.ppodgorsek.cache.manager.cassandra.dao.CassandraCacheDao;
import org.ppodgorsek.cache.manager.core.adapter.SpringDataCacheAdapter;
import org.ppodgorsek.cache.manager.core.annotation.CacheAdapter;
import org.ppodgorsek.cache.manager.core.context.SpringDataCacheNamingPolicy;
import org.ppodgorsek.cache.manager.core.dao.SpringDataCacheDao;
import org.ppodgorsek.cache.manager.core.model.CacheRegionConfiguration;
import org.springframework.cglib.proxy.Callback;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.NoOp;
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

		// final CassandraTemplate cassandraTemplate = new CassandraTemplate();
		// final CassandraRepositoryFactory repositoryFactorySupport = new CassandraRepositoryFactory(cassandraTemplate);

		return generateEnhancedObject(configuration.getType(), configuration.getName());
	}

	@SuppressWarnings("unchecked")
	private <T> T generateEnhancedObject(final String cacheType, final String cacheName) {

		final Enhancer enhancer = new Enhancer();
		enhancer.setInterfaces(new Class<?>[] { CassandraCacheDao.class });
		enhancer.setNamingPolicy(new SpringDataCacheNamingPolicy(cacheType, cacheName));
		enhancer.setInterceptDuringConstruction(false);

		// TODO: remove?
		// enhancer.setStrategy(new MemorySafeUndeclaredThrowableStrategy(UndeclaredThrowableException.class));

		final Callback[] callbacks = getCallbacks(CassandraCacheDao.class);
		final Class<?>[] types = new Class<?>[callbacks.length];

		for (int x = 0; x < types.length; x++) {
			types[x] = callbacks[x].getClass();
		}

		enhancer.setCallbackTypes(types);
		enhancer.setCallbacks(callbacks);

		return (T) enhancer.create();
	}

	private Callback[] getCallbacks(final Class<?> rootClass) {// throws Exception {

		// // Parameters used for optimisation choices...
		// final boolean exposeProxy = advised.isExposeProxy();
		// final boolean isFrozen = advised.isFrozen();
		// final boolean isStatic = advised.getTargetSource().isStatic();
		//
		// // Choose an "aop" interceptor (used for AOP calls).
		// final Callback aopInterceptor = new DynamicAdvisedInterceptor(advised);
		//
		// // Choose a "straight to target" interceptor. (used for calls that are
		// // unadvised but can return this). May be required to expose the proxy.
		// Callback targetInterceptor;
		// if (exposeProxy) {
		// targetInterceptor = isStatic ? new StaticUnadvisedExposedInterceptor(advised.getTargetSource().getTarget())
		// : new DynamicUnadvisedExposedInterceptor(advised.getTargetSource());
		// }
		// else {
		// targetInterceptor = isStatic ? new StaticUnadvisedInterceptor(advised.getTargetSource().getTarget()) : new DynamicUnadvisedInterceptor(
		// advised.getTargetSource());
		// }
		//
		// // Choose a "direct to target" dispatcher (used for
		// // unadvised calls to static targets that cannot return this).
		// final Callback targetDispatcher = isStatic ? new StaticDispatcher(advised.getTargetSource().getTarget()) : new SerializableNoOp();
		//
		// final Callback[] mainCallbacks = new Callback[] { aopInterceptor, // for normal advice
		// targetInterceptor, // invoke target without considering advice, if optimized
		// new SerializableNoOp(), // no override for methods mapped to this
		// targetDispatcher, advisedDispatcher, new EqualsInterceptor(advised), new HashCodeInterceptor(advised) };

		final Callback[] mainCallbacks = new Callback[] { new NoOp() {
		} };

		return mainCallbacks;
	}

}
