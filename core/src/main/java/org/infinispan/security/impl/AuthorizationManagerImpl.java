package org.infinispan.security.impl;

import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.Principal;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.security.auth.Subject;

import org.infinispan.Cache;
import org.infinispan.configuration.cache.AuthorizationConfiguration;
import org.infinispan.configuration.cache.Configuration;
import org.infinispan.configuration.global.GlobalConfiguration;
import org.infinispan.configuration.global.GlobalSecurityConfiguration;
import org.infinispan.factories.annotations.Inject;
import org.infinispan.factories.annotations.Start;
import org.infinispan.factories.annotations.Stop;
import org.infinispan.registry.ClusterRegistry;
import org.infinispan.security.AuthorizationManager;
import org.infinispan.security.CachePermission;
import org.infinispan.util.logging.Log;
import org.infinispan.util.logging.LogFactory;

/**
 * AuthorizationManagerImpl. An implementation of the {@link AuthorizationManager} interface. In
 * order to increase performance, the access mask computed from the {@link Subject}'s
 * {@link Principal}s is cached for future uses.
 *
 * @author Tristan Tarrant
 * @since 7.0
 */
public class AuthorizationManagerImpl implements AuthorizationManager {
   private static final Log log = LogFactory.getLog(AuthorizationManagerImpl.class);
   private GlobalSecurityConfiguration globalConfiguration;
   private AuthorizationConfiguration configuration;
   private ClusterRegistry<String, Subject, Integer> subjectRoleMaskCache;
   private Map<String, CacheRoleImpl> roles;
   private String authCacheScope;

   public AuthorizationManagerImpl() {
   }

   @Inject
   public void init(Cache<?, ?> cache, GlobalConfiguration globalConfiguration, Configuration configuration,
         ClusterRegistry<String, Subject, Integer> clusterRegistry) {
      this.globalConfiguration = globalConfiguration.security();
      this.configuration = configuration.security().authorization();
      this.subjectRoleMaskCache = clusterRegistry;
      authCacheScope = String.format("%s_%s", AuthorizationManager.class.getName(), cache.getName());
   }

   @Start
   public void start() throws Exception {
   }

   @Stop
   public void stop() {
   }

   @Override
   public void checkPermission(CachePermission perm) {
      Subject subject = Subject.getSubject(AccessController.getContext());
      Integer subjectMask = subjectRoleMaskCache.get(authCacheScope, subject);
      if (subjectMask == null) {
         subjectMask = AuthorizationHelper.computeSubjectRoleMask(subject, globalConfiguration, configuration);
         subjectRoleMaskCache.put(authCacheScope, subject, subjectMask, globalConfiguration.securityCacheTimeout(),
               TimeUnit.MILLISECONDS);
      }
      AuthorizationHelper.checkPermission(subject, subjectMask, perm);
   }
}
