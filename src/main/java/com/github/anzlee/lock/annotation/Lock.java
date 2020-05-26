package com.github.anzlee.lock.annotation;

import java.lang.annotation.*;

/**
 * <pre> 分布式锁
 * ClassName: Lock
 * Package: com.trs.fs.dataauth.annotation
 * Description:
 * Datetime: Created by AnzLee on 2020-02-27   12:50.
 * </pre>
 *
 * @author <a href="https://github.com/anzlee">Anzlee</a>
 */

/**
 * 如果加此注解，会使用redis生成分布式锁
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Lock {
    String key() default  "";
}
