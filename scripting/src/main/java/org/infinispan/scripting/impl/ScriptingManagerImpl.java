package org.infinispan.scripting.impl;

import javax.script.Compilable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.infinispan.factories.annotations.Inject;
import org.infinispan.factories.annotations.Start;
import org.infinispan.factories.annotations.Stop;
import org.infinispan.factories.scopes.Scope;
import org.infinispan.factories.scopes.Scopes;
import org.infinispan.manager.EmbeddedCacheManager;
import org.infinispan.registry.ClusterRegistry;
import org.infinispan.scripting.ScriptingManager;
import org.infinispan.scripting.logging.Log;
import org.infinispan.util.logging.LogFactory;

@Scope(Scopes.GLOBAL)
public class ScriptingManagerImpl implements ScriptingManager {
   private static final String DEFAULT_MIME_TYPE = "text/javascript";
   private static final String SCRIPT_SCOPE = CacheScript.class.getName();
   private static final Log log = LogFactory.getLog(ScriptingManagerImpl.class, Log.class);
   private EmbeddedCacheManager cacheManager;
   private ScriptEngineManager scriptEngineManager;
   private ClusterRegistry<String, String, CacheScript> registry;

   public ScriptingManagerImpl() {
   }

   @Inject
   public void initialize(final EmbeddedCacheManager cacheManager, ClusterRegistry<String, String, CacheScript> registry) {
      this.cacheManager = cacheManager;
      this.registry = registry;
      ClassLoader classLoader = cacheManager.getCacheManagerConfiguration().classLoader();
      this.scriptEngineManager = new ScriptEngineManager(classLoader);
   }

   @Override
   public void compileScript(CacheScript script) {
      String mimeType = script.getMimeType() != null ? script.getMimeType() : DEFAULT_MIME_TYPE;
      ScriptEngine engine = scriptEngineManager.getEngineByMimeType(mimeType);
      if (engine instanceof Compilable) {
         try {
            script.compiled = ((Compilable)engine).compile(script.getScript());
         } catch (ScriptException e) {
            throw log.scriptCompilationException(e, script.getName());
         }
      }
   }

   @Override
   public <T> T runScript(String scriptName) {
      CacheScript script = registry.get(SCRIPT_SCOPE, scriptName);
      CacheScriptBinding binding = new CacheScriptBinding(cacheManager);
      if (script.compiled != null) {
         try {
            return (T) script.compiled.eval(binding);
         } catch (ScriptException e) {
            throw log.interpreterError(e);
         }
      } else {
         String mimeType = script.getMimeType() != null ? script.getMimeType() : DEFAULT_MIME_TYPE;
         ScriptEngine engine = scriptEngineManager.getEngineByMimeType(mimeType);
         try {
            return (T) engine.eval(script.script, binding);
         } catch (ScriptException e) {
            throw log.interpreterError(e);
         }
      }
   }

   @Override
   public void addScript(String name, String script, String mimeType) {
      CacheScript s = new CacheScript();
      s.setName(name);
      s.setScript(script);
      s.setMimeType(mimeType);
      compileScript(s);
      registry.put(SCRIPT_SCOPE, name, s);
   }

}
