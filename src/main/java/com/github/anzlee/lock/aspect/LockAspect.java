package com.github.anzlee.lock.aspect;

import com.github.anzlee.lock.annotation.Lock;
import com.github.anzlee.lock.redisson.DistributedRedisLock;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <pre>加锁切面处理
 * ClassName: LockAspect
 * Package: com.trs.fs.com.github.anzlee.lock.aspect
 * Description:
 * Datetime: Created by AnzLee on 2020-03-26   16:11.
 * </pre>
 *
 * @author <a href="https://github.com/anzlee">Anzlee</a>
 */
@Aspect
@Component
public class LockAspect {

    @Autowired
    private DistributedRedisLock distributedRedisLock;

    @Pointcut("execution(* *(..))")
    public void doLock() {
    }

    @Before("doLock() && @annotation(lock)")
    public void lockBefore(JoinPoint joinPoint, Lock lock) {
        distributedRedisLock.lock(this.generateKey(joinPoint, lock));
    }

    @After("doLock() && @annotation(lock)")
    public void lockAfter(JoinPoint joinPoint, Lock lock) {
        distributedRedisLock.unlock(this.generateKey(joinPoint, lock));
    }

    @AfterThrowing("doLock() && @annotation(lock)")
    public void demoAfterThrowing(JoinPoint joinPoint, Lock lock) {
        //....
    }

    @Around("doLock() && @annotation(lock)")
    public Object demoAround(ProceedingJoinPoint pjp, Lock lock) throws Throwable {
        return pjp.proceed();
    }

    private String generateKey(JoinPoint joinPoint, Lock lock) {
        // redis_key = 目标方法所属类的简单类名 + 目标方法名 + key + 参数
        StringBuffer buffer = new StringBuffer(joinPoint.getSignature().getDeclaringType().getSimpleName());
        buffer.append("_");
        buffer.append(joinPoint.getSignature().getName());
        buffer.append("_");
        buffer.append(lock.key());
        for(Object object : joinPoint.getArgs()) {
            buffer.append("_");
            buffer.append(object.toString());
        }
        return buffer.toString();
    }
}
