package org.infinispan.security;

import static org.testng.AssertJUnit.fail;

import java.security.Principal;
import java.security.PrivilegedAction;
import java.security.PrivilegedExceptionAction;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.security.auth.Subject;

import org.infinispan.Cache;
import org.infinispan.commons.util.InfinispanCollections;
import org.infinispan.configuration.cache.AuthorizationConfigurationBuilder;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.configuration.global.GlobalRolesConfigurationBuilder;
import org.infinispan.manager.EmbeddedCacheManager;
import org.infinispan.security.impl.IdentityRoleMapper;
import org.infinispan.test.SingleCacheManagerTest;
import org.infinispan.test.fwk.TestCacheManagerFactory;
import org.testng.annotations.Test;

@Test
public class CacheAuthorizationTest extends SingleCacheManagerTest {
   static final Map<CachePermission, Subject> subjects;

   static {
      // Initialize one subject per permission
      subjects = new HashMap<CachePermission, Subject>(CachePermission.values().length);
      for(CachePermission perm : CachePermission.values()) {
         subjects.put(perm, makeSubject(perm.toString()+"_user", perm.toString()));
      }
   }

   @Override
   protected EmbeddedCacheManager createCacheManager() throws Exception {
      GlobalConfigurationBuilder global = new GlobalConfigurationBuilder();
      GlobalRolesConfigurationBuilder globalRoles = global
         .security()
            .roles()
               .principalRoleMapper(new IdentityRoleMapper());
      ConfigurationBuilder config = new ConfigurationBuilder();
      AuthorizationConfigurationBuilder authConfig = config
            .security()
               .enable()
               .authorization();

      for (CachePermission perm : CachePermission.values()) {
         globalRoles.role(perm.toString()).permission(perm);
         authConfig.role(perm.toString());
      }
      return TestCacheManagerFactory.createCacheManager(global, config);
   }


   @Override
   protected void setup() throws Exception {
      cacheManager = createCacheManager();
   }

   @Override
   protected void teardown() {
      Subject.doAs(subjects.get(CachePermission.ALL), new PrivilegedAction<Void>() {
         @Override
         public Void run() {
            CacheAuthorizationTest.super.teardown();
            return null;
         }
      });
   }

   public void testCacheAccess() throws Exception {
      Subject.doAs(subjects.get(CachePermission.READ), new PrivilegedExceptionAction<Void>() {
         @Override
         public Void run() throws Exception {
            try {
               Cache<Object, Object> cache = cacheManager.getCache();
               fail("READ should not be able to start a cache");
            } catch (SecurityException e) {
               // Expected, the cache wasn't started and READER doens't have LIFECYCLE permissions
            }
            return null;
         }
      });

      Subject.doAs(subjects.get(CachePermission.ALL), new PrivilegedExceptionAction<Void>() {
         @Override
         public Void run() throws Exception {
            Cache<Object, Object> cache = cacheManager.getCache();
            cache.put("a", "a");
            return null;
         }
      });

      Subject.doAs(subjects.get(CachePermission.READ), new PrivilegedExceptionAction<Void>() {
         @Override
         public Void run() throws Exception {
            Cache<Object, Object> cache = cacheManager.getCache();
            cache.get("a");
            cache.getAsync("a");
            return null;
         }
      });

   }

   static Subject makeSubject(String... principals) {
      Set<Principal> set = new HashSet<Principal>();
      for(String principal : principals) {
         set.add(new TestPrincipal(principal));
      }
      return new Subject(true, set, InfinispanCollections.emptySet(), InfinispanCollections.emptySet());
   }

   public static class TestPrincipal implements Principal {
      String name;

      public TestPrincipal(String name) {
         this.name = name;
      }

      @Override
      public String getName() {
         return name;
      }

      @Override
      public String toString() {
         return "TestPrincipal [name=" + name + "]";
      }
   }

}
