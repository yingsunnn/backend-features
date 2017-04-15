package ying.backend_features.method_annotation;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;

/**
 * Created by ying on 2017-04-15.
 */
public class ServicePermissionsNeedMethodInterceptor implements MethodInterceptor {
    private static Logger logger = LoggerFactory.getLogger(ServicePermissionsNeedMethodInterceptor.class);

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        final StopWatch stopWatch = new StopWatch(invocation.getMethod().toGenericString());
        stopWatch.start("invocation.proceed()");

        try {
            logger.debug("~~~~~~~~ START METHOD {} ~~~~~~~~", invocation.getMethod().toGenericString());
            return invocation.proceed();
        } finally {
            stopWatch.stop();
            logger.debug(stopWatch.prettyPrint());
            logger.info("~~~~~~~~ END METHOD {} ~~~~~~~~", invocation.getMethod().toGenericString());
        }
    }
}
