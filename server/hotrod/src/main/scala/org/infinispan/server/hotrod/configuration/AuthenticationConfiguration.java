package org.infinispan.server.hotrod.configuration;

import java.util.Map;
import java.util.Set;

import javax.security.auth.callback.CallbackHandler;

/**
 * AuthenticationConfiguration.
 *
 * @author Tristan Tarrant
 * @since 7.0
 */
public class AuthenticationConfiguration {
   private final boolean enabled;
   private final Set<String> allowedMechs;
   private final CallbackHandler callbackHandler;
   private final Map<String, String> mechProperties;

   AuthenticationConfiguration(boolean enabled, Set<String> set, CallbackHandler callbackHandler,
         Map<String, String> mechProperties) {
      this.enabled = enabled;
      this.allowedMechs = set;
      this.callbackHandler = callbackHandler;
      this.mechProperties = mechProperties;

   }

   public boolean enabled() {
      return enabled;
   }

   public Set<String> allowedMechs() {
      return allowedMechs;
   }

   public CallbackHandler callbackHandler() {
      return callbackHandler;
   }

   public Map<String, String> mechProperties() {
      return mechProperties;
   }

   @Override
   public String toString() {
      return "AuthenticationConfiguration [enabled=" + enabled + ", allowedMechs=" + allowedMechs
            + ", callbackHandler=" + callbackHandler + ", mechProperties=" + mechProperties + "]";
   }
}
