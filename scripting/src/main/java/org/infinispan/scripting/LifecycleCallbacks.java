package org.infinispan.scripting;

import org.infinispan.configuration.global.GlobalConfiguration;
import org.infinispan.factories.GlobalComponentRegistry;
import org.infinispan.lifecycle.AbstractModuleLifecycle;
import org.infinispan.scripting.impl.ScriptingManagerImpl;
import org.infinispan.scripting.impl.CacheScript.CacheScriptExternalizer;
import org.infinispan.scripting.logging.Log;
import org.infinispan.util.logging.LogFactory;

public class LifecycleCallbacks extends AbstractModuleLifecycle {
   private static final Log log = LogFactory.getLog(LifecycleCallbacks.class, Log.class);

   @Override
   public void cacheManagerStarting(GlobalComponentRegistry gcr, GlobalConfiguration gc) {
      ScriptingManager scriptingManager = new ScriptingManagerImpl();
      gcr.registerComponent(scriptingManager, ScriptingManager.class);
      CacheScriptExternalizer ext = new CacheScriptExternalizer(scriptingManager);
      gc.serialization().advancedExternalizers().put(ext.getId(), ext);
   }
}
