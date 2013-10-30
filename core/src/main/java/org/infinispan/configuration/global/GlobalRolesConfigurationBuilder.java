package org.infinispan.configuration.global;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.Subject;

import org.infinispan.commons.configuration.Builder;
import org.infinispan.security.PrincipalRoleMapper;
import org.infinispan.security.Role;
import org.infinispan.security.impl.IdentityRoleMapper;

/**
 * GlobalRolesConfigurationBuilder.
 *
 * @author Tristan Tarrant
 * @since 7.0
 */
public class GlobalRolesConfigurationBuilder extends AbstractGlobalConfigurationBuilder implements Builder<GlobalRolesConfiguration> {
   private PrincipalRoleMapper principalRoleMapper = new IdentityRoleMapper();
   private Map<String, GlobalRoleConfigurationBuilder> roles = new HashMap<String, GlobalRoleConfigurationBuilder>();

   public GlobalRolesConfigurationBuilder(GlobalSecurityConfigurationBuilder builder) {
      super(builder.getGlobalConfig());
   }

   /**
    * The class of a mapper which converts the {@link Principal}s associated with a {@link Subject} into a set of roles
    *
    * @param principalRoleMapper
    */
   public GlobalRolesConfigurationBuilder principalRoleMapper(PrincipalRoleMapper principalRoleMapper) {
      this.principalRoleMapper = principalRoleMapper;
      return this;
   }

   public GlobalRoleConfigurationBuilder role(String name) {
      GlobalRoleConfigurationBuilder role = new GlobalRoleConfigurationBuilder(name, this);
      roles.put(name, role);
      return role;
   }

   @Override
   public void validate() {
   }

   @Override
   public GlobalRolesConfiguration create() {
      Map<String, Role> rolesCfg = new HashMap<String, Role>();
      for(GlobalRoleConfigurationBuilder role : this.roles.values()) {
         Role roleCfg = role.create();
         rolesCfg.put(roleCfg.getName(), roleCfg);
      }
      return new GlobalRolesConfiguration(principalRoleMapper, rolesCfg);
   }

   @Override
   public Builder<?> read(GlobalRolesConfiguration template) {
      this.principalRoleMapper = template.principalRoleMapper();
      this.roles.clear();
      for(Role role : template.roles().values()) {
         this.role(role.getName()).read(role);
      }
      return this;
   }

}
