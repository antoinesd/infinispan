package org.infinispan.configuration.global;

import org.infinispan.commons.configuration.Builder;

/**
 * GlobalSecurityConfigurationBuilder.
 *
 * @author Tristan Tarrant
 * @since 7.0
 */
public class GlobalSecurityConfigurationBuilder extends AbstractGlobalConfigurationBuilder implements GlobalSecurityConfigurationChildBuilder, Builder<GlobalSecurityConfiguration> {
   private GlobalRolesConfigurationBuilder rolesBuilder;
   private boolean enabled = false;
   private long securityCacheTimeout = 30000;

   public GlobalSecurityConfigurationBuilder(GlobalConfigurationBuilder builder) {
      super(builder);
      rolesBuilder = new GlobalRolesConfigurationBuilder(this);
   }

   @Override
   public GlobalRolesConfigurationBuilder roles() {
      return rolesBuilder;
   }

   @Override
   public GlobalSecurityConfigurationBuilder securityCacheTimeout(long securityCacheTimeout) {
      this.securityCacheTimeout = securityCacheTimeout;
      return this;
   }

   @Override
   public void validate() {
   }

   @Override
   public GlobalSecurityConfiguration create() {
      return new GlobalSecurityConfiguration(rolesBuilder.create(), securityCacheTimeout);
   }

   @Override
   public GlobalSecurityConfigurationBuilder read(GlobalSecurityConfiguration template) {
      this.rolesBuilder.read(template.roles());
      return this;
   }


}
