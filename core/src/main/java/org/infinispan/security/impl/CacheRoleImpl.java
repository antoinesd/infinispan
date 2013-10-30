package org.infinispan.security.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import org.infinispan.security.CachePermission;
import org.infinispan.security.Role;

/**
 * CacheRoleImpl.
 *
 * @author Tristan Tarrant
 * @since 7.0
 */
public class CacheRoleImpl implements Role {
   private final String name;
   private final Set<CachePermission> permissions;
   private final int mask;

   public CacheRoleImpl(String name, Set<CachePermission> permissions) {
      this.name = name;
      this.permissions = Collections.unmodifiableSet(permissions);
      int permMask = 0;
      for (CachePermission permission : permissions) {
         permMask |= permission.getMask();
      }
      this.mask = permMask;
   }

   @Override
   public String getName() {
      return name;
   }

   @Override
   public Collection<CachePermission> getPermissions() {
      return permissions;
   }

   @Override
   public int getMask() {
      return mask;
   }

   @Override
   public String toString() {
      return "CacheRoleImpl [name=" + name + ", permissions=" + permissions + ", mask=" + mask + "]";
   }

}
