package org.infinispan.scripting.impl;

import javax.script.SimpleBindings;

import org.infinispan.manager.EmbeddedCacheManager;

public class CacheScriptBinding extends SimpleBindings {

   private final EmbeddedCacheManager cacheManager;

   public CacheScriptBinding(EmbeddedCacheManager cacheManager) {
      this.cacheManager = cacheManager;
   }

   @Override
   public boolean containsKey(Object key) {
      return super.containsKey(key);
   }

   @Override
   public Object get(Object key) {
      return super.get(key);
   }

}
