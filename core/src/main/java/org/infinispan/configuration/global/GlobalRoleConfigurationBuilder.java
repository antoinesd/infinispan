package org.infinispan.configuration.global;

import java.util.HashSet;
import java.util.Set;

import org.infinispan.commons.configuration.Builder;
import org.infinispan.security.CachePermission;
import org.infinispan.security.Role;
import org.infinispan.security.impl.CacheRoleImpl;

/**
 * GlobalRoleConfigurationBuilder.
 *
 * @author Tristan Tarrant
 * @since 7.0
 */
public class GlobalRoleConfigurationBuilder extends AbstractGlobalConfigurationBuilder implements GlobalRolesConfigurationChildBuilder, Builder<Role> {
   private Set<CachePermission> permissions = new HashSet<CachePermission>();
   private final GlobalRolesConfigurationBuilder builder;
   private final String name;

   public GlobalRoleConfigurationBuilder(String name, GlobalRolesConfigurationBuilder builder) {
      super(builder.getGlobalConfig());
      this.builder = builder;
      this.name = name;
   }

   /**
    * Adds a permission to a role
    *
    * @param permission the name of the permission to add to the role. See {@link CachePermission}
    */
   public GlobalRoleConfigurationBuilder permission(String permission) {
      permissions.add(CachePermission.valueOf(permission));
      return this;
   }

   /**
    * Adds a permission to a role
    *
    * @param permission the permission to add to the role. See {@link CachePermission}
    */
   public GlobalRoleConfigurationBuilder permission(CachePermission permission) {
      permissions.add(permission);
      return this;
   }

   @Override
   public GlobalRoleConfigurationBuilder role(String name) {
      return builder.role(name);
   }

   @Override
   public void validate() {
   }

   @Override
   public Role create() {
      return new CacheRoleImpl(name, permissions);
   }

   @Override
   public Builder<?> read(Role template) {
      permissions.clear();
      for(CachePermission perm : template.getPermissions()) {
         permissions.add(perm);
      }
      return this;
   }
}
