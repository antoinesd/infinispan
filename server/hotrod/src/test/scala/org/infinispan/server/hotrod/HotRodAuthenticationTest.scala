package org.infinispan.server.hotrod

import org.testng.annotations.Test
import java.lang.reflect.Method
import test.HotRodTestingUtil._
import org.testng.Assert._
import java.util.Arrays
import org.infinispan.server.hotrod.OperationStatus._
import org.infinispan.server.hotrod.test._
import org.infinispan.test.TestingUtil.generateRandomString
import java.util.concurrent.TimeUnit
import org.infinispan.server.core.test.Stoppable
import org.infinispan.server.hotrod.configuration.HotRodServerConfiguration
import org.infinispan.test.fwk.TestCacheManagerFactory
import org.infinispan.server.core.QueryFacade
import org.infinispan.AdvancedCache
import org.infinispan.manager.EmbeddedCacheManager
import org.infinispan.server.hotrod.configuration.HotRodServerConfigurationBuilder

/**
 * Hot Rod server authentication test.
 *
 * @author Tristan Tarrant
 * @since 7.0
 */
@Test(groups = Array("functional"), testName = "server.hotrod.HotRodAuthenticationTest")
class HotRodAuthenticationTest extends HotRodSingleNodeTest {

   override def createStartHotRodServer(cacheManager: EmbeddedCacheManager): HotRodServer = {
      val builder = new HotRodServerConfigurationBuilder
      builder.authentication().enable().addAllowedMech("DIGEST-MD5").callbackHandler(new TestCallbackHandler("user", "password", "realm"))
      startHotRodServer(cacheManager, UniquePortThreadLocal.get.intValue, 0, builder)
   }

   def testAuthMechList(m: Method) {
      val a = client.authMechList
      assertEquals(a.mechs.size, 1)
      assertTrue(a.mechs.contains("DIGEST-MD5"))
   }

}
