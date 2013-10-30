package org.infinispan.configuration.global;

/**
 * GlobalSecurityConfiguration.
 *
 * @author Tristan Tarrant
 * @since 7.0
 */
public class GlobalSecurityConfiguration {
   private final GlobalRolesConfiguration roles;
   private final long securityCacheTimeout;

   GlobalSecurityConfiguration(GlobalRolesConfiguration roles, long securityCacheTimeout) {
      this.roles = roles;
      this.securityCacheTimeout = securityCacheTimeout;
   }

   public GlobalRolesConfiguration roles() {
      return roles;
   }

   public long securityCacheTimeout() {
      return securityCacheTimeout;
   }

   @Override
   public String toString() {
      return "GlobalSecurityConfiguration [roles=" + roles + ", securityCacheTimeout=" + securityCacheTimeout + "]";
   }



}
