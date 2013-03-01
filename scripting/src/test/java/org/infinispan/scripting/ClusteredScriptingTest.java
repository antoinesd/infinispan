package org.infinispan.scripting;

import static org.testng.AssertJUnit.assertTrue;

import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.test.MultipleCacheManagersTest;
import org.testng.annotations.Test;

@Test(groups="functional", testName="scripting.ScriptingTest")
public class ClusteredScriptingTest extends MultipleCacheManagersTest {

   @Override
   protected void createCacheManagers() throws Exception {
      final ConfigurationBuilder conf = getDefaultClusteredCacheConfig(CacheMode.REPL_SYNC, false);
      createCluster(conf, 2);
      waitForClusterToForm();
   }

   private void executeScriptOnManager(int num) {
      ScriptingManager scriptingManager = manager(num).getGlobalComponentRegistry().getComponent(ScriptingManager.class);
      Boolean b = scriptingManager.runScript("test");
      assertTrue(b);
   }

   public void testClusteredScriptExec() {
      ScriptingManager scriptingManager = manager(0).getGlobalComponentRegistry().getComponent(ScriptingManager.class);
      scriptingManager.addScript("test", "true;", "text/javascript");
      executeScriptOnManager(0);
      executeScriptOnManager(1);
   }

}
