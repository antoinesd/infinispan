package org.infinispan.configuration.cache;

import java.util.Collections;
import java.util.Set;

/**
 * AuthorizationConfiguration.
 *
 * @author Tristan Tarrant
 * @since 7.0
 */
public class AuthorizationConfiguration {
   private final Set<String> roles;

   AuthorizationConfiguration(Set<String> roles) {
      this.roles = Collections.unmodifiableSet(roles);
   }

   public Set<String> roles() {
      return roles;
   }

   @Override
   public String toString() {
      return "AuthorizationConfiguration [roles=" + roles + "]";
   }

}
