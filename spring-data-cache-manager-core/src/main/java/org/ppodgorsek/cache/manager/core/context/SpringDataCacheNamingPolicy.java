package org.ppodgorsek.cache.manager.core.context;

import org.springframework.cglib.core.DefaultNamingPolicy;
import org.springframework.cglib.core.NamingPolicy;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * @author Paul Podgorsek
 */
public class SpringDataCacheNamingPolicy extends DefaultNamingPolicy implements NamingPolicy {

	private static final String SUFFIX = "BySpringDataCacheCglib";

	private final String tag;

	/**
	 * Default constructor.
	 */
	public SpringDataCacheNamingPolicy(final String type, final String name) {

		super();

		Assert.notNull(type, "The type is required");
		Assert.notNull(name, "The name is required");

		final String cleanType = StringUtils.capitalize(type.toLowerCase());
		final String cleanName = StringUtils.capitalize(name.toLowerCase());

		tag = cleanName + cleanType + SUFFIX;
	}

	@Override
	protected String getTag() {
		return tag;
	}

}
