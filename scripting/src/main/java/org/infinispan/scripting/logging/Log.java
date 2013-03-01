package org.infinispan.scripting.logging;

import static org.jboss.logging.Logger.Level.ERROR;

import org.infinispan.commons.CacheException;
import org.jboss.logging.annotations.Cause;
import org.jboss.logging.annotations.LogMessage;
import org.jboss.logging.annotations.Message;
import org.jboss.logging.annotations.MessageLogger;

/**
 * Log abstraction for the Scripting module. For this module, message ids
 * ranging from 21001 to 22000 inclusively have been reserved.
 *
 * @author Tristan Tarrant
 * @since 7.0
 */
@MessageLogger(projectCode = "ISPN")
public interface Log extends org.infinispan.util.logging.Log {
   @LogMessage(level = ERROR)
   @Message(value = "Could not register interpreter MBean", id = 21001)
   void jmxRegistrationFailed();

   @LogMessage(level = ERROR)
   @Message(value = "Could not unregister interpreter MBean", id = 21002)
   void jmxUnregistrationFailed();

   @Message(value = "Interpreter error", id = 21003)
   CacheException interpreterError(@Cause Throwable t);

   @Message(value = "Compiler error for script '%s'", id = 21004)
   CacheException scriptCompilationException(@Cause Throwable t, String name);
}
