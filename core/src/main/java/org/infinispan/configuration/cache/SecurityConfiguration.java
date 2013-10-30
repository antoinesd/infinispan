package org.infinispan.configuration.cache;

/**
 * SecurityConfiguration.
 *
 * @author Tristan Tarrant
 * @since 7.0
 */
public class SecurityConfiguration {

   private final boolean enabled;
   private final AuthorizationConfiguration authorization;

   SecurityConfiguration(AuthorizationConfiguration authorization, boolean enabled) {
      this.authorization = authorization;
      this.enabled = enabled;
   }

   public AuthorizationConfiguration authorization() {
      return authorization;
   }

   public boolean enabled() {
      return enabled;
   }

   @Override
   public String toString() {
      return "SecurityConfiguration [enabled=" + enabled + ", authorization=" + authorization + "]";
   }

}
