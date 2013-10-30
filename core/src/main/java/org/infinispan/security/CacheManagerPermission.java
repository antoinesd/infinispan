package org.infinispan.security;

/**
 * CachePermissions.
 *
 * @author Tristan Tarrant
 * @since 7.0
 */
public enum CacheManagerPermission {
   CONFIGURATION, LIFECYCLE, LISTEN, ALL(Integer.MAX_VALUE);

   private final int flag;

   CacheManagerPermission() {
      this.flag = 1 << ordinal();
   }

   CacheManagerPermission(int flag) {
      this.flag = flag;
   }

   public int getFlag() {
      return flag;
   }
}