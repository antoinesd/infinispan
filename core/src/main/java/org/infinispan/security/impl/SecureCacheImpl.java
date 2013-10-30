 package org.infinispan.security.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.security.auth.Subject;
import javax.transaction.TransactionManager;
import javax.transaction.xa.XAResource;

import org.infinispan.AdvancedCache;
import org.infinispan.atomic.Delta;
import org.infinispan.batch.BatchContainer;
import org.infinispan.configuration.cache.Configuration;
import org.infinispan.container.DataContainer;
import org.infinispan.container.entries.CacheEntry;
import org.infinispan.context.Flag;
import org.infinispan.context.InvocationContextContainer;
import org.infinispan.distribution.DistributionManager;
import org.infinispan.eviction.EvictionManager;
import org.infinispan.factories.ComponentRegistry;
import org.infinispan.interceptors.base.CommandInterceptor;
import org.infinispan.lifecycle.ComponentStatus;
import org.infinispan.manager.EmbeddedCacheManager;
import org.infinispan.metadata.Metadata;
import org.infinispan.notifications.KeyFilter;
import org.infinispan.remoting.rpc.RpcManager;
import org.infinispan.security.AuthorizationManager;
import org.infinispan.security.CachePermission;
import org.infinispan.security.SecureCache;
import org.infinispan.stats.Stats;
import org.infinispan.util.concurrent.NotifyingFuture;
import org.infinispan.util.concurrent.locks.LockManager;

/**
 * SecureCacheImpl.
 *
 * @author Tristan Tarrant
 * @since 7.0
 */
public final class SecureCacheImpl<K, V> implements SecureCache<K, V> {

   private final org.infinispan.security.AuthorizationManager authzManager;
   private final AdvancedCache<K, V> delegate;
   private final Subject subject;

   public SecureCacheImpl(AdvancedCache<K, V> delegate) {
      this(delegate, null);
   }

   public SecureCacheImpl(AdvancedCache<K, V> delegate, Subject subject) {
      this.authzManager = delegate.getAuthorizationManager();
      this.delegate = delegate;
      this.subject = subject;
   }

   @Override
   public Subject getSubject() {
      return subject;
   }

   @Override
   public boolean startBatch() {
      authzManager.checkPermission(CachePermission.WRITE);
      return delegate.startBatch();
   }

   @Override
   public void addListener(Object listener, KeyFilter filter) {
      authzManager.checkPermission(CachePermission.LISTEN);
      delegate.addListener(listener, filter);
   }

   @Override
   public void addListener(Object listener) {
      authzManager.checkPermission(CachePermission.LISTEN);
      delegate.addListener(listener);
   }

   @Override
   public void start() {
      authzManager.checkPermission(CachePermission.LIFECYCLE);
      delegate.start();
   }

   @Override
   public void stop() {
      authzManager.checkPermission(CachePermission.LIFECYCLE);
      delegate.stop();
   }

   @Override
   public NotifyingFuture<V> putAsync(K key, V value) {
      authzManager.checkPermission(CachePermission.WRITE);
      return delegate.putAsync(key, value);
   }

   @Override
   public void endBatch(boolean successful) {
      authzManager.checkPermission(CachePermission.WRITE);
      delegate.endBatch(successful);
   }

   @Override
   public void removeListener(Object listener) {
      authzManager.checkPermission(CachePermission.LISTEN);
      delegate.removeListener(listener);
   }

   @Override
   public Set<Object> getListeners() {
      authzManager.checkPermission(CachePermission.LISTEN);
      return delegate.getListeners();
   }

   @Override
   public NotifyingFuture<V> putAsync(K key, V value, long lifespan, TimeUnit unit) {
      authzManager.checkPermission(CachePermission.WRITE);
      return delegate.putAsync(key, value, lifespan, unit);
   }

   @Override
   public AdvancedCache<K, V> withFlags(Flag... flags) {
      return delegate.withFlags(flags); //TODO,
   }

   @Override
   public V putIfAbsent(K key, V value) {
      authzManager.checkPermission(CachePermission.WRITE);
      return delegate.putIfAbsent(key, value);
   }

   @Override
   public NotifyingFuture<V> putAsync(K key, V value, long lifespan, TimeUnit lifespanUnit, long maxIdle,
         TimeUnit maxIdleUnit) {
      authzManager.checkPermission(CachePermission.WRITE);
      return delegate.putAsync(key, value, lifespan, lifespanUnit, maxIdle, maxIdleUnit);
   }

   @Override
   public String getName() {
      return delegate.getName();
   }

   @Override
   public String getVersion() {
      return delegate.getVersion();
   }

   @Override
   public V put(K key, V value) {
      authzManager.checkPermission(CachePermission.WRITE);
      return delegate.put(key, value);
   }

   @Override
   public AdvancedCache<K, V> as(Subject subject) {
      return delegate.as(subject); //FIXME: handle impersonation
   }

   @Override
   public NotifyingFuture<Void> putAllAsync(Map<? extends K, ? extends V> data) {
      authzManager.checkPermission(CachePermission.WRITE);
      return delegate.putAllAsync(data);
   }

   @Override
   public V put(K key, V value, long lifespan, TimeUnit unit) {
      authzManager.checkPermission(CachePermission.WRITE);
      return delegate.put(key, value, lifespan, unit);
   }

   @Override
   public boolean remove(Object key, Object value) {
      authzManager.checkPermission(CachePermission.WRITE);
      return delegate.remove(key, value);
   }

   @Override
   public NotifyingFuture<Void> putAllAsync(Map<? extends K, ? extends V> data, long lifespan, TimeUnit unit) {
      authzManager.checkPermission(CachePermission.WRITE);
      return delegate.putAllAsync(data, lifespan, unit);
   }

   @Override
   public void addInterceptor(CommandInterceptor i, int position) {
      delegate.addInterceptor(i, position);
   }

   @Override
   public V putIfAbsent(K key, V value, long lifespan, TimeUnit unit) {
      authzManager.checkPermission(CachePermission.WRITE);
      return delegate.putIfAbsent(key, value, lifespan, unit);
   }

   @Override
   public boolean addInterceptorAfter(CommandInterceptor i, Class<? extends CommandInterceptor> afterInterceptor) {
      return delegate.addInterceptorAfter(i, afterInterceptor);
   }

   @Override
   public NotifyingFuture<Void> putAllAsync(Map<? extends K, ? extends V> data, long lifespan, TimeUnit lifespanUnit,
         long maxIdle, TimeUnit maxIdleUnit) {
      authzManager.checkPermission(CachePermission.WRITE);
      return delegate.putAllAsync(data, lifespan, lifespanUnit, maxIdle, maxIdleUnit);
   }

   @Override
   public void putAll(Map<? extends K, ? extends V> map, long lifespan, TimeUnit unit) {
      authzManager.checkPermission(CachePermission.WRITE);
      delegate.putAll(map, lifespan, unit);
   }

   @Override
   public boolean addInterceptorBefore(CommandInterceptor i, Class<? extends CommandInterceptor> beforeInterceptor) {
      return delegate.addInterceptorBefore(i, beforeInterceptor);
   }

   @Override
   public boolean replace(K key, V oldValue, V newValue) {
      authzManager.checkPermission(CachePermission.WRITE);
      return delegate.replace(key, oldValue, newValue);
   }

   @Override
   public NotifyingFuture<Void> clearAsync() {
      authzManager.checkPermission(CachePermission.WRITE);
      return delegate.clearAsync();
   }

   @Override
   public V replace(K key, V value, long lifespan, TimeUnit unit) {
      authzManager.checkPermission(CachePermission.WRITE);
      return delegate.replace(key, value, lifespan, unit);
   }

   @Override
   public void removeInterceptor(int position) {
      delegate.removeInterceptor(position);
   }

   @Override
   public NotifyingFuture<V> putIfAbsentAsync(K key, V value) {
      authzManager.checkPermission(CachePermission.WRITE);
      return delegate.putIfAbsentAsync(key, value);
   }

   @Override
   public void removeInterceptor(Class<? extends CommandInterceptor> interceptorType) {
      delegate.removeInterceptor(interceptorType);
   }

   @Override
   public boolean replace(K key, V oldValue, V value, long lifespan, TimeUnit unit) {
      authzManager.checkPermission(CachePermission.WRITE);
      return delegate.replace(key, oldValue, value, lifespan, unit);
   }

   @Override
   public List<CommandInterceptor> getInterceptorChain() {
      return delegate.getInterceptorChain();
   }

   @Override
   public NotifyingFuture<V> putIfAbsentAsync(K key, V value, long lifespan, TimeUnit unit) {
      authzManager.checkPermission(CachePermission.WRITE);
      return delegate.putIfAbsentAsync(key, value, lifespan, unit);
   }

   @Override
   public V replace(K key, V value) {
      authzManager.checkPermission(CachePermission.WRITE);
      return delegate.replace(key, value);
   }

   @Override
   public EvictionManager getEvictionManager() {
      return delegate.getEvictionManager();
   }

   @Override
   public V put(K key, V value, long lifespan, TimeUnit lifespanUnit, long maxIdleTime, TimeUnit maxIdleTimeUnit) {
      authzManager.checkPermission(CachePermission.WRITE);
      return delegate.put(key, value, lifespan, lifespanUnit, maxIdleTime, maxIdleTimeUnit);
   }

   @Override
   public void putForExternalRead(K key, V value) {
      authzManager.checkPermission(CachePermission.WRITE);
      delegate.putForExternalRead(key, value);
   }

   @Override
   public ComponentRegistry getComponentRegistry() {
      return delegate.getComponentRegistry();
   }

   @Override
   public DistributionManager getDistributionManager() {
      authzManager.checkPermission(CachePermission.WRITE);
      return delegate.getDistributionManager();
   }

   @Override
   public AuthorizationManager getAuthorizationManager() {
      authzManager.checkPermission(CachePermission.ADMIN);
      return delegate.getAuthorizationManager();
   }

   @Override
   public NotifyingFuture<V> putIfAbsentAsync(K key, V value, long lifespan, TimeUnit lifespanUnit, long maxIdle,
         TimeUnit maxIdleUnit) {
      authzManager.checkPermission(CachePermission.WRITE);
      return delegate.putIfAbsentAsync(key, value, lifespan, lifespanUnit, maxIdle, maxIdleUnit);
   }

   @Override
   public boolean isEmpty() {
      authzManager.checkPermission(CachePermission.READ);
      return delegate.isEmpty();
   }

   @Override
   public boolean lock(K... keys) {
      authzManager.checkPermission(CachePermission.READ); // ??
      return delegate.lock(keys);
   }

   @Override
   public boolean containsKey(Object key) {
      authzManager.checkPermission(CachePermission.READ);
      return delegate.containsKey(key);
   }

   @Override
   public V putIfAbsent(K key, V value, long lifespan, TimeUnit lifespanUnit, long maxIdleTime, TimeUnit maxIdleTimeUnit) {
      authzManager.checkPermission(CachePermission.WRITE);
      return delegate.putIfAbsent(key, value, lifespan, lifespanUnit, maxIdleTime, maxIdleTimeUnit);
   }

   @Override
   public boolean lock(Collection<? extends K> keys) {
      authzManager.checkPermission(CachePermission.READ); // ??
      return delegate.lock(keys);
   }

   @Override
   public NotifyingFuture<V> removeAsync(Object key) {
      authzManager.checkPermission(CachePermission.WRITE);
      return delegate.removeAsync(key);
   }

   @Override
   public boolean containsValue(Object value) {
      authzManager.checkPermission(CachePermission.READ);
      return delegate.containsValue(value);
   }

   @Override
   public void putAll(Map<? extends K, ? extends V> map, long lifespan, TimeUnit lifespanUnit, long maxIdleTime,
         TimeUnit maxIdleTimeUnit) {
      authzManager.checkPermission(CachePermission.WRITE);
      delegate.putAll(map, lifespan, lifespanUnit, maxIdleTime, maxIdleTimeUnit);
   }

   @Override
   public NotifyingFuture<Boolean> removeAsync(Object key, Object value) {
      authzManager.checkPermission(CachePermission.WRITE);
      return delegate.removeAsync(key, value);
   }

   @Override
   public void applyDelta(K deltaAwareValueKey, Delta delta, Object... locksToAcquire) {
      delegate.applyDelta(deltaAwareValueKey, delta, locksToAcquire);
   }

   @Override
   public void evict(K key) {
      authzManager.checkPermission(CachePermission.READ); // ??
      delegate.evict(key);
   }

   @Override
   public NotifyingFuture<V> replaceAsync(K key, V value) {
      authzManager.checkPermission(CachePermission.WRITE);
      return delegate.replaceAsync(key, value);
   }

   @Override
   public RpcManager getRpcManager() {
      authzManager.checkPermission(CachePermission.ADMIN);
      return delegate.getRpcManager();
   }

   @Override
   public V replace(K key, V value, long lifespan, TimeUnit lifespanUnit, long maxIdleTime, TimeUnit maxIdleTimeUnit) {
      authzManager.checkPermission(CachePermission.WRITE);
      return delegate.replace(key, value, lifespan, lifespanUnit, maxIdleTime, maxIdleTimeUnit);
   }

   @Override
   public V get(Object key) {
      authzManager.checkPermission(CachePermission.READ);
      return delegate.get(key);
   }

   @Override
   public NotifyingFuture<V> replaceAsync(K key, V value, long lifespan, TimeUnit unit) {
      authzManager.checkPermission(CachePermission.WRITE);
      return delegate.replaceAsync(key, value, lifespan, unit);
   }

   @Override
   public BatchContainer getBatchContainer() {
      return delegate.getBatchContainer();
   }

   @Override
   public Configuration getCacheConfiguration() {
      authzManager.checkPermission(CachePermission.ADMIN);
      return delegate.getCacheConfiguration();
   }

   @Override
   public EmbeddedCacheManager getCacheManager() {
      return delegate.getCacheManager();
   }

   @Override
   public InvocationContextContainer getInvocationContextContainer() {
      return delegate.getInvocationContextContainer();
   }

   @Override
   public AdvancedCache<K, V> getAdvancedCache() {
      return this;
   }

   @Override
   public ComponentStatus getStatus() {
      return delegate.getStatus();
   }

   @Override
   public int size() {
      authzManager.checkPermission(CachePermission.READ);
      return delegate.size();
   }

   @Override
   public boolean replace(K key, V oldValue, V value, long lifespan, TimeUnit lifespanUnit, long maxIdleTime,
         TimeUnit maxIdleTimeUnit) {
      authzManager.checkPermission(CachePermission.WRITE);
      return delegate.replace(key, oldValue, value, lifespan, lifespanUnit, maxIdleTime, maxIdleTimeUnit);
   }

   @Override
   public DataContainer getDataContainer() {
      authzManager.checkPermission(CachePermission.ADMIN);
      return delegate.getDataContainer();
   }

   @Override
   public NotifyingFuture<V> replaceAsync(K key, V value, long lifespan, TimeUnit lifespanUnit, long maxIdle,
         TimeUnit maxIdleUnit) {
      authzManager.checkPermission(CachePermission.WRITE);
      return delegate.replaceAsync(key, value, lifespan, lifespanUnit, maxIdle, maxIdleUnit);
   }

   @Override
   public TransactionManager getTransactionManager() {
      return delegate.getTransactionManager();
   }

   @Override
   public Set<K> keySet() {
      authzManager.checkPermission(CachePermission.BULK);
      return delegate.keySet();
   }

   @Override
   public V remove(Object key) {
      authzManager.checkPermission(CachePermission.WRITE);
      return delegate.remove(key);
   }

   @Override
   public LockManager getLockManager() {
      return delegate.getLockManager();
   }

   @Override
   public NotifyingFuture<Boolean> replaceAsync(K key, V oldValue, V newValue) {
      authzManager.checkPermission(CachePermission.WRITE);
      return delegate.replaceAsync(key, oldValue, newValue);
   }

   @Override
   public Stats getStats() {
      return delegate.getStats();
   }

   @Override
   public XAResource getXAResource() {
      return delegate.getXAResource();
   }

   @Override
   public ClassLoader getClassLoader() {
      return delegate.getClassLoader();
   }

   @Override
   public NotifyingFuture<Boolean> replaceAsync(K key, V oldValue, V newValue, long lifespan, TimeUnit unit) {
      authzManager.checkPermission(CachePermission.WRITE);
      return delegate.replaceAsync(key, oldValue, newValue, lifespan, unit);
   }

   @Override
   public Collection<V> values() {
      authzManager.checkPermission(CachePermission.BULK);
      return delegate.values();
   }

   @Override
   public AdvancedCache<K, V> with(ClassLoader classLoader) {
      return delegate.with(classLoader);
   }

   @Override
   public NotifyingFuture<Boolean> replaceAsync(K key, V oldValue, V newValue, long lifespan, TimeUnit lifespanUnit,
         long maxIdle, TimeUnit maxIdleUnit) {
      return delegate.replaceAsync(key, oldValue, newValue, lifespan, lifespanUnit, maxIdle, maxIdleUnit);
   }

   @Override
   public Set<java.util.Map.Entry<K, V>> entrySet() {
      authzManager.checkPermission(CachePermission.BULK);
      return delegate.entrySet();
   }

   @Override
   public NotifyingFuture<V> getAsync(K key) {
      authzManager.checkPermission(CachePermission.READ);
      return delegate.getAsync(key);
   }

   @Override
   public V put(K key, V value, Metadata metadata) {
      authzManager.checkPermission(CachePermission.WRITE);
      return delegate.put(key, value, metadata);
   }

   @Override
   public void putAll(Map<? extends K, ? extends V> m) {
      authzManager.checkPermission(CachePermission.WRITE);
      delegate.putAll(m);
   }

   @Override
   public V replace(K key, V value, Metadata metadata) {
      authzManager.checkPermission(CachePermission.WRITE);
      return delegate.replace(key, value, metadata);
   }

   @Override
   public boolean replace(K key, V oldValue, V newValue, Metadata metadata) {
      authzManager.checkPermission(CachePermission.WRITE);
      return delegate.replace(key, oldValue, newValue, metadata);
   }

   @Override
   public void clear() {
      authzManager.checkPermission(CachePermission.WRITE);
      delegate.clear();
   }

   @Override
   public V putIfAbsent(K key, V value, Metadata metadata) {
      authzManager.checkPermission(CachePermission.WRITE);
      return delegate.putIfAbsent(key, value, metadata);
   }

   @Override
   public NotifyingFuture<V> putAsync(K key, V value, Metadata metadata) {
      authzManager.checkPermission(CachePermission.WRITE);
      return delegate.putAsync(key, value, metadata);
   }

   @Override
   public CacheEntry getCacheEntry(K key) {
      authzManager.checkPermission(CachePermission.READ);
      return delegate.getCacheEntry(key);
   }

   @Override
   public boolean equals(Object o) {
      return delegate.equals(o);
   }

   @Override
   public int hashCode() {
      return delegate.hashCode();
   }
}
