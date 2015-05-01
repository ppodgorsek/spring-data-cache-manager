package org.ppodgorsek.cache.manager.core.config;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

import org.ppodgorsek.cache.manager.core.filter.InterfaceTypeFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

/**
 * @author Paul Podgorsek
 */
public class SpringDataCachePackageScanner {

	private static final Logger LOGGER = LoggerFactory.getLogger(SpringDataCachePackageScanner.class);

	private final Set<Class<? extends Annotation>> annotations = new HashSet<Class<? extends Annotation>>();
	private final Set<Class<?>> interfaces = new HashSet<Class<?>>();
	private final Set<String> packages;

	public SpringDataCachePackageScanner(final Set<String> packages) {

		super();

		Assert.notNull(packages, "The packages to scan are required");

		this.packages = packages;
	}

	public void addAnnotation(final Class<? extends Annotation> annotation) {

		Assert.notNull(annotation, "The annotation is required");

		annotations.add(annotation);
	}

	public void addInterface(final Class<?> interfaceType) {

		Assert.notNull(interfaceType, "The interface type is required");

		interfaces.add(interfaceType);
	}

	public Set<Class<?>> scanForClasses() {

		final Set<Class<?>> classes = new HashSet<Class<?>>();

		for (final String basePackage : packages) {
			classes.addAll(scanBasePackage(basePackage));
		}

		return classes;
	}

	private Set<Class<?>> scanBasePackage(final String basePackage) {

		final Set<Class<?>> classes = new HashSet<Class<?>>();

		if (StringUtils.hasText(basePackage)) {

			final ClassPathScanningCandidateComponentProvider componentProvider = new ClassPathScanningCandidateComponentProvider(false);

			for (final Class<? extends Annotation> annotation : annotations) {
				componentProvider.addIncludeFilter(new AnnotationTypeFilter(annotation, false, true));
			}

			for (final Class<?> interfaceType : interfaces) {
				componentProvider.addIncludeFilter(new InterfaceTypeFilter(interfaceType));
			}

			for (final BeanDefinition candidate : componentProvider.findCandidateComponents(basePackage)) {
				try {
					classes.add(ClassUtils.forName(candidate.getBeanClassName(), null));
				}
				catch (ClassNotFoundException | LinkageError e) {
					LOGGER.error("Impossible to load a candidate component", e);
				}
			}
		}

		return classes;
	}

}
