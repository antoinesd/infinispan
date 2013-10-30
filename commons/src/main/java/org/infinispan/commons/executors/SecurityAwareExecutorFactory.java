package org.infinispan.commons.executors;

import java.security.AccessControlContext;
import java.util.Properties;
import java.util.concurrent.ExecutorService;

/**
 * Used to configure and create executors which are aware of the current security context
 *
 * @author Tristan Tarrant
 * @since 6.1
 */
public interface SecurityAwareExecutorFactory extends ExecutorFactory {
   public ExecutorService getExecutor(Properties p, AccessControlContext context);
}
