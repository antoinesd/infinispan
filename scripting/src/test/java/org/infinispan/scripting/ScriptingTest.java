package org.infinispan.scripting;

import static org.testng.AssertJUnit.assertTrue;

import org.infinispan.manager.EmbeddedCacheManager;
import org.infinispan.test.SingleCacheManagerTest;
import org.infinispan.test.fwk.TestCacheManagerFactory;
import org.testng.annotations.Test;

@Test(groups="functional", testName="scripting.ScriptingTest")
public class ScriptingTest extends SingleCacheManagerTest {

   @Override
   protected EmbeddedCacheManager createCacheManager() throws Exception {
      return TestCacheManagerFactory.createCacheManager();
   }

   public void testSimpleScript() {
      ScriptingManager scriptingManager = cacheManager.getGlobalComponentRegistry().getComponent(ScriptingManager.class);
      scriptingManager.addScript("test", "true;", "text/javascript");
      Boolean b = scriptingManager.runScript("test");
      assertTrue(b);
   }


}
