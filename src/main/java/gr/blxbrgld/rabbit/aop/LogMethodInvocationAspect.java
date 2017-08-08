package gr.blxbrgld.rabbit.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

/**
 * This module will capture the cross-cutting concern of method invocation logging.
 * @author blxbrgld
 */
@Aspect
@Component
@Slf4j
public class LogMethodInvocationAspect {

    /**
     * We have annotated our method with @Around. This is our advice, and around
     * advice means we are adding extra code both before and after method execution.
     * Our @Around annotation has a point cut argument. Our pointcut just says,
     * ‘Apply this advice any method which is annotated with @LogMethodInvocation.’
     */
    @Around("@annotation(gr.blxbrgld.rabbit.aop.LogMethodInvocation)")
    public Object logMethodInvocation(ProceedingJoinPoint joinPoint) throws Throwable {
        String name = ((MethodSignature) joinPoint.getSignature()).getMethod().getName();
        log.debug("Method {}() Invoked.", name);
        //Just call the annotated method (Join point)
        Object proceed = joinPoint.proceed();
        log.debug("Method {}() Exiting.", name);
        return proceed;
    }
}