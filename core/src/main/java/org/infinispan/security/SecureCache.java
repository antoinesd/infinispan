
 package org.infinispan.security;

import javax.security.auth.Subject;

import org.infinispan.AdvancedCache;

/**
 * SecureCache.
 *
 * @author Tristan Tarrant
 * @since 6.1
 */
public interface SecureCache<K, V> extends AdvancedCache<K, V> {
   /**
    * Returns the subject which was used to obtain this {@link SecureCache}
    *
    * @return
    */
   Subject getSubject();

}
