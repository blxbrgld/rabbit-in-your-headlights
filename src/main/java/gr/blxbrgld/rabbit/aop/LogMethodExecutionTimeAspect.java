package gr.blxbrgld.rabbit.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.perf4j.slf4j.Slf4JStopWatch;
import org.springframework.stereotype.Component;

/**
 * Cross-cutting Concern Of Method Execution Time Logging
 * @author blxbrgld
 */
@Aspect
@Component
@Slf4j
public class LogMethodExecutionTimeAspect {

    @Around("@annotation(gr.blxbrgld.rabbit.aop.LogMethodExecutionTime)")
    public Object logMethodInvocation(ProceedingJoinPoint joinPoint) throws Throwable {
        final Slf4JStopWatch stopWatch = new Slf4JStopWatch();
        stopWatch.start(((MethodSignature) joinPoint.getSignature()).getMethod().getName());
        //Just call the annotated method (Join point)
        Object proceed = joinPoint.proceed();
        stopWatch.stop();
        return proceed;
    }
}
