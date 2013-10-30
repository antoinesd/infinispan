package org.infinispan.security.impl;

import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.Principal;
import java.util.Set;

import javax.security.auth.Subject;

import org.infinispan.configuration.cache.AuthorizationConfiguration;
import org.infinispan.configuration.global.GlobalSecurityConfiguration;
import org.infinispan.security.CachePermission;
import org.infinispan.security.PrincipalRoleMapper;
import org.infinispan.security.Role;
import org.infinispan.util.logging.Log;
import org.infinispan.util.logging.LogFactory;

/**
 * AuthorizationHelper. Some utility methods for computing access masks and verifying them against permissions
 *
 * @author Tristan Tarrant
 * @since 7.0
 */
public class AuthorizationHelper {
   private static final Log log = LogFactory.getLog(AuthorizationHelper.class);

   public static void checkPermission(Subject subject, int subjectMask, CachePermission perm) {
      if ((subjectMask & perm.getMask()) != perm.getMask()) {
         throw log.unauthorizedAccess(subject.toString(), perm.toString());
      }
   }

   public static void checkPermission(GlobalSecurityConfiguration globalConfiguration, AuthorizationConfiguration configuration, CachePermission perm) {
      AccessControlContext acc = AccessController.getContext();
      Subject subject = Subject.getSubject(acc);
      int subjectMask = computeSubjectRoleMask(subject, globalConfiguration, configuration);
      checkPermission(subject, subjectMask, perm);
   }

   public static int computeSubjectRoleMask(Subject subject, GlobalSecurityConfiguration globalConfiguration, AuthorizationConfiguration configuration) {
      PrincipalRoleMapper roleMapper = globalConfiguration.roles().principalRoleMapper();
      int mask = 0;
      for (Principal principal : subject.getPrincipals()) {
         Set<String> roleNames = roleMapper.principalToRoles(principal);
         if (roleNames != null) {
            for(String roleName : roleNames) {
               Role role = globalConfiguration.roles().roles().get(roleName);
               if (role != null) {
                  mask |= role.getMask();
               }
            }
         }
      }
      return mask;
   }

}
