package org.ppodgorsek.cache.manager.core.filter;

import java.io.IOException;

import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AssignableTypeFilter;

/**
 * {@link org.springframework.core.type.filter.TypeFilter} that only matches interfaces. Thus setting this up makes only sense providing an interface type as
 * {@code targetType}.
 *
 * @see org.springframework.data.repository.config.RepositoryComponentProvider.InterfaceTypeFilter
 */
public class InterfaceTypeFilter extends AssignableTypeFilter {

	/**
	 * Creates a new {@link InterfaceTypeFilter}.
	 *
	 * @param targetType
	 */
	public InterfaceTypeFilter(final Class<?> targetType) {
		super(targetType);
	}

	@Override
	public boolean match(final MetadataReader metadataReader, final MetadataReaderFactory metadataReaderFactory) throws IOException {
		return metadataReader.getClassMetadata().isInterface() && super.match(metadataReader, metadataReaderFactory);
	}

}
