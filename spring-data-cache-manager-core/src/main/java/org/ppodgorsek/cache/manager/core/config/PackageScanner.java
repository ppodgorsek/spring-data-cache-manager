package org.ppodgorsek.cache.manager.core.config;

import java.util.Set;

/**
 * @author Paul Podgorsek
 */
public interface PackageScanner {

	Set<Class<?>> scanForClasses();

}
