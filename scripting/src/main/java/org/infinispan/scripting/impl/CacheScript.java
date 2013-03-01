package org.infinispan.scripting.impl;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Set;

import javax.script.CompiledScript;

import org.infinispan.commons.marshall.AbstractExternalizer;
import org.infinispan.commons.util.Util;
import org.infinispan.scripting.ExternalizerIds;
import org.infinispan.scripting.ScriptingManager;

public class CacheScript {
   String name;
   String script;
   String mimeType;
   transient CompiledScript compiled;

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getScript() {
      return script;
   }

   public void setScript(String script) {
      this.script = script;
   }

   public String getMimeType() {
      return mimeType;
   }

   public void setMimeType(String mimeType) {
      this.mimeType = mimeType;
   }

   public static class CacheScriptExternalizer extends AbstractExternalizer<CacheScript> {
      private ScriptingManager scriptingManager;

      public CacheScriptExternalizer(ScriptingManager scriptingManager) {
         this.scriptingManager = scriptingManager;
      }

      @Override
      public void writeObject(ObjectOutput output, CacheScript object) throws IOException {
         output.writeObject(object.name);
         output.writeObject(object.script);
         output.writeObject(object.mimeType);
      }

      @Override
      public CacheScript readObject(ObjectInput input) throws IOException, ClassNotFoundException {
         CacheScript script = new CacheScript();
         script.name = (String) input.readObject();
         script.script = (String) input.readObject();
         script.mimeType = (String) input.readObject();
         scriptingManager.compileScript(script);
         return script;
      }

      @Override
      public Integer getId() {
         return ExternalizerIds.CACHE_SCRIPT;
      }

      @Override
      public Set<Class<? extends CacheScript>> getTypeClasses() {
         return Util.<Class<? extends CacheScript>>asSet(CacheScript.class);
      }
   }

}
