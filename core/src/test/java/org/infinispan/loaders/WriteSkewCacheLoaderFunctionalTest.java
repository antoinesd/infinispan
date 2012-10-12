package org.infinispan.loaders;

import org.infinispan.Cache;
import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.cache.VersioningScheme;
import org.infinispan.container.DataContainer;
import org.infinispan.container.entries.InternalCacheEntry;
import org.infinispan.loaders.dummy.DummyInMemoryCacheStoreConfigurationBuilder;
import org.infinispan.manager.EmbeddedCacheManager;
import org.infinispan.test.SingleCacheManagerTest;
import org.infinispan.test.TestingUtil;
import org.infinispan.test.fwk.TestCacheManagerFactory;
import org.infinispan.util.concurrent.IsolationLevel;
import org.testng.annotations.Test;

import javax.transaction.TransactionManager;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * @author Pedro Ruivo
 * @author Tristan Tarrant
 * @since 5.2
 */
@Test(groups = "functional", testName = "loaders.WriteSkewCacheLoaderFunctionalTest")
public class WriteSkewCacheLoaderFunctionalTest extends SingleCacheManagerTest {

   static final long LIFESPAN = 60000000; // very large lifespan so nothing actually expires

   @Override
   protected EmbeddedCacheManager createCacheManager() throws Exception {
      ConfigurationBuilder builder = getDefaultClusteredCacheConfig(CacheMode.REPL_SYNC, true);
      builder.
         versioning().enable().scheme(VersioningScheme.SIMPLE).
         locking().isolationLevel(IsolationLevel.REPEATABLE_READ).writeSkewCheck(true).
         loaders().preload(true).addStore(DummyInMemoryCacheStoreConfigurationBuilder.class).storeName("preloadingStore");
      return TestCacheManagerFactory.createClusteredCacheManager(builder);
   }

   private void assertInCacheAndStore(Cache<String, String> cache, CacheStore store, Object key, Object value) throws CacheLoaderException {
      assertInCacheAndStore(cache, store, key, value, -1);
   }

   private void assertInCacheAndStore(Cache<String, String> cache, CacheStore store, Object key, Object value, long lifespanMillis) throws CacheLoaderException {
      InternalCacheEntry se = cache.getAdvancedCache().getDataContainer().get(key);
      testStoredEntry(se, value, lifespanMillis, "Cache", key);
      se = store.load(key);
      testStoredEntry(se, value, lifespanMillis, "Store", key);
   }

   private void testStoredEntry(InternalCacheEntry entry, Object expectedValue, long expectedLifespan, String src, Object key) {
      assert entry != null : src + " entry for key " + key + " should NOT be null";
      assert entry.getValue().equals(expectedValue) : src + " should contain value " + expectedValue + " under key " + entry.getKey() + " but was " + entry.getValue()
            + ". Entry is " + entry;
      assert entry.getLifespan() == expectedLifespan : src + " expected lifespan for key " + key + " to be " + expectedLifespan + " but was " + entry.getLifespan() + ". Entry is "
            + entry;
   }

   private void assertNotInCacheAndStore(Cache<String, String> cache, CacheStore store, String... keys) throws CacheLoaderException {
      for (Object key : keys) {
         assert !cache.getAdvancedCache().getDataContainer().containsKey(key) : "Cache should not contain key " + key;
         assert !store.containsKey(key) : "Store should not contain key " + key;
      }
   }

   public void testPreloadingInTransactionalCache() throws Exception {
      Cache<String, String> preloadingCache = cache();
      TransactionManager transactionManager = preloadingCache.getAdvancedCache().getTransactionManager();
      CacheStore preloadingStore = TestingUtil.extractComponent(preloadingCache, CacheLoaderManager.class).getCacheStore();

      assert preloadingCache.getCacheConfiguration().loaders().preload();

      assertNotInCacheAndStore(preloadingCache, preloadingStore, "k1", "k2", "k3", "k4");

      preloadingCache.put("k1", "v1");
      preloadingCache.put("k2", "v2", LIFESPAN, MILLISECONDS);
      preloadingCache.put("k3", "v3");
      preloadingCache.put("k4", "v4", LIFESPAN, MILLISECONDS);

      for (int i = 1; i < 5; i++) {
         if (i % 2 == 1)
            assertInCacheAndStore(preloadingCache, preloadingStore, "k" + i, "v" + i);
         else
            assertInCacheAndStore(preloadingCache, preloadingStore, "k" + i, "v" + i, LIFESPAN);
      }

      DataContainer c = preloadingCache.getAdvancedCache().getDataContainer();

      assert c.size() == 4;
      preloadingCache.stop();
      assert c.size() == 0;

      preloadingCache.start();
      preloadingStore = TestingUtil.extractComponent(preloadingCache, CacheLoaderManager.class).getCacheStore();
      assert preloadingCache.getCacheConfiguration().loaders().preload();

      c = preloadingCache.getAdvancedCache().getDataContainer();
      assert c.size() == 4;

      for (int i = 1; i < 5; i++) {
         if (i % 2 == 1)
            assertInCacheAndStore(preloadingCache, preloadingStore, "k" + i, "v" + i);
         else
            assertInCacheAndStore(preloadingCache, preloadingStore, "k" + i, "v" + i, LIFESPAN);
      }

      transactionManager.begin();
      assert preloadingCache.get("k1").equals("v1") : "k1 value is different from v1";
      preloadingCache.put("k1", "new-v1");
      transactionManager.commit();
   }

}
