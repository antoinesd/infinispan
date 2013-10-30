package org.infinispan.configuration.cache;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Configuration {

   private final WeakReference<ClassLoader> classLoader; //TODO remove this
   private final ClusteringConfiguration clusteringConfiguration;
   private final CustomInterceptorsConfiguration customInterceptorsConfiguration;
   private final DataContainerConfiguration dataContainerConfiguration;
   private final DeadlockDetectionConfiguration deadlockDetectionConfiguration;
   private final EvictionConfiguration evictionConfiguration;
   private final ExpirationConfiguration expirationConfiguration;
   private final IndexingConfiguration indexingConfiguration;
   private final InvocationBatchingConfiguration invocationBatchingConfiguration;
   private final JMXStatisticsConfiguration jmxStatisticsConfiguration;
   private final PersistenceConfiguration persistenceConfiguration;
   private final LockingConfiguration lockingConfiguration;
   private final StoreAsBinaryConfiguration storeAsBinaryConfiguration;
   private final TransactionConfiguration transactionConfiguration;
   private final VersioningConfiguration versioningConfiguration;
   private final UnsafeConfiguration unsafeConfiguration;
   private final Map<Class<?>, ?> moduleConfiguration;
   private final SecurityConfiguration securityConfiguration;
   private final SitesConfiguration sitesConfiguration;
   private final CompatibilityModeConfiguration compatibilityConfiguration;

   Configuration(ClusteringConfiguration clusteringConfiguration,
                 CustomInterceptorsConfiguration customInterceptorsConfiguration,
                 DataContainerConfiguration dataContainerConfiguration, DeadlockDetectionConfiguration deadlockDetectionConfiguration,
                 EvictionConfiguration evictionConfiguration, ExpirationConfiguration expirationConfiguration,
                 IndexingConfiguration indexingConfiguration, InvocationBatchingConfiguration invocationBatchingConfiguration,
                 JMXStatisticsConfiguration jmxStatisticsConfiguration,
                 PersistenceConfiguration persistenceConfiguration,
                 LockingConfiguration lockingConfiguration,
                 SecurityConfiguration securityConfiguration,
                 StoreAsBinaryConfiguration storeAsBinaryConfiguration,
                 TransactionConfiguration transactionConfiguration, UnsafeConfiguration unsafeConfiguration,
                 VersioningConfiguration versioningConfiguration,
                 SitesConfiguration sitesConfiguration,
                 CompatibilityModeConfiguration compatibilityConfiguration,
                 List<?> modules, ClassLoader cl) {
      this.clusteringConfiguration = clusteringConfiguration;
      this.customInterceptorsConfiguration = customInterceptorsConfiguration;
      this.dataContainerConfiguration = dataContainerConfiguration;
      this.deadlockDetectionConfiguration = deadlockDetectionConfiguration;
      this.evictionConfiguration = evictionConfiguration;
      this.expirationConfiguration = expirationConfiguration;
      this.indexingConfiguration = indexingConfiguration;
      this.invocationBatchingConfiguration = invocationBatchingConfiguration;
      this.jmxStatisticsConfiguration = jmxStatisticsConfiguration;
      this.persistenceConfiguration = persistenceConfiguration;
      this.lockingConfiguration = lockingConfiguration;
      this.storeAsBinaryConfiguration = storeAsBinaryConfiguration;
      this.transactionConfiguration = transactionConfiguration;
      this.unsafeConfiguration = unsafeConfiguration;
      this.versioningConfiguration = versioningConfiguration;
      this.securityConfiguration = securityConfiguration;
      this.sitesConfiguration = sitesConfiguration;
      this.compatibilityConfiguration = compatibilityConfiguration;
      Map<Class<?>, Object> modulesMap = new HashMap<Class<?>, Object>();
      for(Object module : modules) {
         modulesMap.put(module.getClass(), module);
      }
      this.moduleConfiguration = Collections.unmodifiableMap(modulesMap);
      this.classLoader = new WeakReference<ClassLoader>(cl);
   }

   /**
    * Will be removed with no replacement
    */
   @Deprecated
   public ClassLoader classLoader() {
      return classLoader == null ? null : classLoader.get();
   }

   public ClusteringConfiguration clustering() {
      return clusteringConfiguration;
   }

   public CustomInterceptorsConfiguration customInterceptors() {
      return customInterceptorsConfiguration;
   }

   public DataContainerConfiguration dataContainer() {
      return dataContainerConfiguration;
   }

   public DeadlockDetectionConfiguration deadlockDetection() {
      return deadlockDetectionConfiguration;
   }

   public EvictionConfiguration eviction() {
      return evictionConfiguration;
   }

   public ExpirationConfiguration expiration() {
      return expirationConfiguration;
   }

   public IndexingConfiguration indexing() {
      return indexingConfiguration;
   }

   public InvocationBatchingConfiguration invocationBatching() {
      return invocationBatchingConfiguration;
   }

   public JMXStatisticsConfiguration jmxStatistics() {
      return jmxStatisticsConfiguration;
   }

   public PersistenceConfiguration persistence() {
      return persistenceConfiguration;
   }

   public LockingConfiguration locking() {
      return lockingConfiguration;
   }

   @SuppressWarnings("unchecked")
   public <T> T module(Class<T> moduleClass) {
      return (T)moduleConfiguration.get(moduleClass);
   }

   public Map<Class<?>, ?> modules() {
      return moduleConfiguration;
   }

   public StoreAsBinaryConfiguration storeAsBinary() {
      return storeAsBinaryConfiguration;
   }

   public TransactionConfiguration transaction() {
      return transactionConfiguration;
   }

   public UnsafeConfiguration unsafe() {
      return unsafeConfiguration;
   }
   
   public SecurityConfiguration security() {
      return securityConfiguration;
   }

   public SitesConfiguration sites() {
      return sitesConfiguration;
   }

   public VersioningConfiguration versioning() {
      return versioningConfiguration;
   }

   public CompatibilityModeConfiguration compatibility() {
      return compatibilityConfiguration;
   }

   @Override
   public String toString() {
      return "Configuration{" +
            "classLoader=" + classLoader +
            ", clustering=" + clusteringConfiguration +
            ", customInterceptors=" + customInterceptorsConfiguration +
            ", dataContainer=" + dataContainerConfiguration +
            ", deadlockDetection=" + deadlockDetectionConfiguration +
            ", eviction=" + evictionConfiguration +
            ", expiration=" + expirationConfiguration +
            ", indexing=" + indexingConfiguration +
            ", invocationBatching=" + invocationBatchingConfiguration +
            ", jmxStatistics=" + jmxStatisticsConfiguration +
            ", persistence=" + persistenceConfiguration +
            ", locking=" + lockingConfiguration +
            ", modules=" + moduleConfiguration +
            ", security=" + securityConfiguration +
            ", storeAsBinary=" + storeAsBinaryConfiguration +
            ", transaction=" + transactionConfiguration +
            ", versioning=" + versioningConfiguration +
            ", unsafe=" + unsafeConfiguration +
            ", sites=" + sitesConfiguration +
            ", compatibility=" + compatibilityConfiguration +
            '}';
   }
}
