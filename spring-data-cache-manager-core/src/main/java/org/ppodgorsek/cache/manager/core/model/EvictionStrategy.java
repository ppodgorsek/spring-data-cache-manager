package org.ppodgorsek.cache.manager.core.model;

/**
 * Types of eviction strategies.
 *
 * @author Paul Podgorsek
 */
public enum EvictionStrategy {

	/**
	 * First in, first out.
	 */
	FIFO,

	/**
	 * Least frequently used.
	 */
	LFU,

	/**
	 * Least recently used.
	 */
	LRU;

}
