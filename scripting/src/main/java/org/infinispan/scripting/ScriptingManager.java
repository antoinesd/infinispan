package org.infinispan.scripting;

import org.infinispan.scripting.impl.CacheScript;

public interface ScriptingManager {

   public abstract void compileScript(CacheScript script);

   public abstract <T> T runScript(String scriptName);

   public abstract void addScript(String name, String script, String mimeType);

}