package org.infinispan.configuration.global;

import java.util.Collections;
import java.util.Map;

import org.infinispan.security.PrincipalRoleMapper;
import org.infinispan.security.Role;

/**
 * GlobalRolesConfiguration.
 *
 * @author Tristan Tarrant
 * @since 7.0
 */
public class GlobalRolesConfiguration {
   private final PrincipalRoleMapper principalRoleMapper;
   private final Map<String, Role> roles;

   public GlobalRolesConfiguration(PrincipalRoleMapper principalRoleMapper, Map<String, Role> roles) {
      this.principalRoleMapper = principalRoleMapper;
      this.roles = Collections.unmodifiableMap(roles);
   }

   public PrincipalRoleMapper principalRoleMapper() {
      return principalRoleMapper;
   }

   public Map<String, Role> roles() {
      return roles;
   }

   @Override
   public String toString() {
      return "GlobalRolesConfiguration [principalRoleMapper=" + principalRoleMapper + ", roles=" + roles + "]";
   }

}
