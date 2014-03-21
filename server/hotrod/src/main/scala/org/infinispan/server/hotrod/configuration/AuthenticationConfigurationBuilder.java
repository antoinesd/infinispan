package org.infinispan.server.hotrod.configuration;

import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.security.auth.callback.CallbackHandler;
import javax.security.sasl.Sasl;
import javax.security.sasl.SaslServerFactory;

import org.infinispan.commons.configuration.Builder;
import org.infinispan.commons.logging.LogFactory;
import org.infinispan.server.hotrod.logging.JavaLog;

/**
 * AuthenticationConfigurationBuilder.
 *
 * @author Tristan Tarrant
 * @since 7.0
 */
public class AuthenticationConfigurationBuilder extends AbstractHotRodServerChildConfigurationBuilder implements Builder<AuthenticationConfiguration> {
   private static final JavaLog log = LogFactory.getLog(AuthenticationConfigurationBuilder.class, JavaLog.class);
   private boolean enabled = false;
   private CallbackHandler callbackHandler;
   private Set<String> allowedMechs = new LinkedHashSet<String>();
   private Map<String, String> mechProperties;

   AuthenticationConfigurationBuilder(HotRodServerChildConfigurationBuilder builder) {
      super(builder);
   }

   public AuthenticationConfigurationBuilder enable() {
      this.enabled = true;
      return this;
   }

   public AuthenticationConfigurationBuilder disable() {
      this.enabled = false;
      return this;
   }

   public AuthenticationConfigurationBuilder enabled(boolean enabled) {
      this.enabled = enabled;
      return this;
   }

   public AuthenticationConfigurationBuilder callbackHandler(CallbackHandler callbackHandler) {
      this.callbackHandler = callbackHandler;
      return this;
   }

   public AuthenticationConfigurationBuilder addAllowedMech(String mech) {
      this.allowedMechs.add(mech);
      return this;
   }

   public AuthenticationConfigurationBuilder mechProperties(Map<String, String> mechProperties) {
      this.mechProperties = mechProperties;
      return this;
   }

   public AuthenticationConfigurationBuilder addMechProperty(String key, String value) {
      this.mechProperties.put(key, value);
      return this;
   }

   @Override
   public void validate() {
      if (enabled) {
         if (callbackHandler == null) {
            throw log.invalidCallbackHandler();
         }
         Set<String> allMechs = new LinkedHashSet<String>();
         for (Enumeration<SaslServerFactory> factories = Sasl.getSaslServerFactories(); factories.hasMoreElements(); ) {
            SaslServerFactory factory = factories.nextElement();
            for(String mech : factory.getMechanismNames(mechProperties)) {
               allMechs.add(mech);
            }
         }
         if (allowedMechs.isEmpty()) {
            allowedMechs = allMechs;
         } else if (!allMechs.containsAll(allowedMechs)){
            throw log.invalidAllowedMechs(allowedMechs, allMechs);
         }
      }
   }

   @Override
   public AuthenticationConfiguration create() {
      return new AuthenticationConfiguration(enabled, Collections.unmodifiableSet(allowedMechs), callbackHandler, mechProperties);
   }

   @Override
   public Builder<?> read(AuthenticationConfiguration template) {
      this.enabled = template.enabled();
      this.allowedMechs.clear();
      this.allowedMechs.addAll(template.allowedMechs());
      this.callbackHandler = template.callbackHandler();
      this.mechProperties = template.mechProperties();
      return this;
   }


}
