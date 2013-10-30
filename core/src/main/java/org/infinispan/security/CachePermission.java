package org.infinispan.security;

import org.infinispan.Cache;

/**
 * CachePermissions.
 *
 * @author Tristan Tarrant
 * @since 7.0
 */
public enum CachePermission {
   /**
    * Allows control of a cache's lifecycle (i.e. invoke {@link Cache#start()} and {@link Cache#stop()}
    */
   LIFECYCLE,
   /**
    * Allows reading data from a cache
    */
   READ,
   /**
    * Allows writing data to a cache
    */
   WRITE,
   /**
    * Allows performing task execution (e.g. distributed executors, map/reduce) on a cache
    */
   EXEC,
   /**
    * Allows attaching listeners to a cache
    */
   LISTEN,
   /**
    * Allows bulk-read operations (e.g. {@link Cache#keySet()}) on a cache
    */
   BULK,
   /**
    * Allows performing "administrative" operations on a cache
    */
   ADMIN,
   /**
    * Synthetic permission which implies all of the others
    */
   ALL(Integer.MAX_VALUE);

   private final int mask;

   CachePermission() {
      this.mask = 1 << ordinal();
   }

   CachePermission(int mask) {
      this.mask = mask;
   }

   public int getMask() {
      return mask;
   }
}
