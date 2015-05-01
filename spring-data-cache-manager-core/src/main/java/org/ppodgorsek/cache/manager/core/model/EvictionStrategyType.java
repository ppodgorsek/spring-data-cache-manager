package org.ppodgorsek.cache.manager.core.model;

/**
 * Types of eviction strategies.
 *
 * @author Paul Podgorsek
 */
public enum EvictionStrategyType {

	/**
	 * Least frequently used.
	 */
	LFU,

	/**
	 * Least recently used.
	 */
	LRU;

}
