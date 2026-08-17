package com.library.security.infrastructure.cache;

import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Thread-safe size-bounded cache with automatic expiration - Production-ready.
 * 
 * <p>Implements a robust caching solution with:
 * <ul>
 *   <li>Automatic expiration of entries</li>
 *   <li>Size-bounded eviction (LRU-style when max size reached)</li>
 *   <li>Thread-safe operations</li>
 *   <li>Memory leak prevention through automatic cleanup</li>
 *   <li>Configurable cleanup intervals</li>
 * </ul>
 * 
 * <p>This is designed for the security infrastructure where memory leaks would be
 * catastrophic in production environments.</p>
 * 
 * @param <K> the type of keys maintained by this cache
 * @param <V> the type of mapped values
 */
public class ExpiringCache<K, V> {
    
    private static final int DEFAULT_MAX_SIZE = 10000;
    
    private final ConcurrentHashMap<K, CacheEntry<V>> cache;
    private final ScheduledExecutorService cleanupExecutor;
    private final long expireAfterWriteSeconds;
    private final long cleanupIntervalSeconds;
    private final int maxSize;
    private final AtomicInteger cleanupCount = new AtomicInteger(0);
    
    /**
     * Creates a new expiring cache with default max size (10000).
     * 
     * @param expireAfterWriteSeconds the time in seconds after which entries expire
     * @param cleanupIntervalSeconds the interval in seconds between cleanup runs
     */
    public ExpiringCache(long expireAfterWriteSeconds, long cleanupIntervalSeconds) {
        this(expireAfterWriteSeconds, cleanupIntervalSeconds, DEFAULT_MAX_SIZE);
    }
    
    /**
     * Creates a new expiring cache with specified max size.
     * 
     * @param expireAfterWriteSeconds the time in seconds after which entries expire
     * @param cleanupIntervalSeconds the interval in seconds between cleanup runs
     * @param maxSize the maximum number of entries in the cache
     */
    public ExpiringCache(long expireAfterWriteSeconds, long cleanupIntervalSeconds, int maxSize) {
        this.cache = new ConcurrentHashMap<>();
        this.expireAfterWriteSeconds = expireAfterWriteSeconds;
        this.cleanupIntervalSeconds = cleanupIntervalSeconds;
        this.maxSize = maxSize;
        this.cleanupExecutor = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "security-cache-cleaner");
            t.setDaemon(true);
            return t;
        });
        
        startCleanupTask();
    }
    
    /**
     * Associates the specified value with the specified key in this cache.
     * Evicts oldest entries if max size is reached.
     * 
     * @param key the key with which the specified value is to be associated
     * @param value the value to be associated with the key
     */
    public void put(K key, V value) {
        if (key == null || value == null) {
            return;
        }
        
        CacheEntry<V> entry = new CacheEntry<>(value, Instant.now().plusSeconds(expireAfterWriteSeconds));
        
        if (cache.size() >= maxSize && !cache.containsKey(key)) {
            evictOldest();
        }
        
        cache.put(key, entry);
    }
    
    /**
     * Returns the value to which the specified key is mapped, or null if expired or not present.
     * 
     * @param key the key whose associated value is to be returned
     * @return the value to which the specified key is mapped, or null if expired or not present
     */
    public V get(K key) {
        if (key == null) {
            return null;
        }
        
        CacheEntry<V> entry = cache.get(key);
        if (entry == null) {
            return null;
        }
        
        if (entry.isExpired()) {
            cache.remove(key);
            return null;
        }
        
        return entry.getValue();
    }
    
    /**
     * Removes the mapping for a key from this cache if it is present.
     * 
     * @param key the key whose mapping is to be removed from the cache
     */
    public void remove(K key) {
        if (key != null) {
            cache.remove(key);
        }
    }
    
    /**
     * Returns the number of entries in the cache.
     * 
     * @return the current cache size
     */
    public int size() {
        return cache.size();
    }
    
    /**
     * Returns true if this cache contains no key-value mappings.
     * 
     * @return true if this cache contains no key-value mappings
     */
    public boolean isEmpty() {
        return cache.isEmpty();
    }
    
    /**
     * Clears all entries from the cache.
     */
    public void clear() {
        cache.clear();
    }
    
    /**
     * Removes all expired entries from the cache.
     * Uses snapshot iteration to avoid ConcurrentModificationException.
     * 
     * @return the number of entries removed
     */
    public int cleanup() {
        int removed = 0;
        
        for (java.util.Map.Entry<K, CacheEntry<V>> entry : cache.entrySet()) {
            if (entry.getValue().isExpired()) {
                cache.remove(entry.getKey());
                removed++;
            }
        }
        
        return removed;
    }
    
    /**
     * Shuts down the cleanup executor gracefully.
     */
    public void shutdown() {
        cleanupExecutor.shutdown();
        try {
            if (!cleanupExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                cleanupExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    private void startCleanupTask() {
        cleanupExecutor.scheduleAtFixedRate(() -> {
            try {
                int removed = cleanup();
                int count = cleanupCount.incrementAndGet();
                if (removed > 0) {
                    System.out.println("Security cache cleanup #" + count + ": removed " + removed + " expired entries");
                }
            } catch (Exception e) {
                System.err.println("Error during security cache cleanup: " + e.getMessage());
            }
        }, cleanupIntervalSeconds, cleanupIntervalSeconds, TimeUnit.SECONDS);
    }
    
    private void evictOldest() {
        long oldestTime = Long.MAX_VALUE;
        K oldestKey = null;
        
        for (java.util.Map.Entry<K, CacheEntry<V>> entry : cache.entrySet()) {
            if (entry.getValue().getExpirationTime() < oldestTime) {
                oldestTime = entry.getValue().getExpirationTime();
                oldestKey = entry.getKey();
            }
        }
        
        if (oldestKey != null) {
            cache.remove(oldestKey);
        }
    }
    
    /**
     * Internal cache entry with expiration time.
     */
    private static class CacheEntry<V> {
        private final V value;
        private final Instant expirationTime;
        
        CacheEntry(V value, Instant expirationTime) {
            this.value = value;
            this.expirationTime = expirationTime;
        }
        
        V getValue() {
            return value;
        }
        
        boolean isExpired() {
            return Instant.now().isAfter(expirationTime);
        }
        
        long getExpirationTime() {
            return expirationTime.toEpochMilli();
        }
    }
}
