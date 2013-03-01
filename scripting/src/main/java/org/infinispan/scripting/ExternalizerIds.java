package org.infinispan.scripting;

/**
 * Identifiers used by the Marshaller to delegate to specialized Externalizers.
 * For details, read http://community.jboss.org/docs/DOC-16198
 *
 * The range reserved for the Scripting module is from 1600 to 1699.
 *
 * @author Tristan Tarrant
 * @since 7.0
 */
@SuppressWarnings("boxing")
public interface ExternalizerIds {
   static final Integer CACHE_SCRIPT = 1600;
}
