package com.github.anzlee.lock.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * <pre>springboot自动注解扫描
 * ClassName: LockAutoScanConfig
 * Package: com.trs.fs.pay.config
 * Description:
 * Datetime: Created by AnzLee on 2020-03-26   11:53.
 * </pre>
 *
 * @author <a href="https://github.com/anzlee">Anzlee</a>
 */
@Configuration
@ComponentScan("com.github.anzlee.lock.**")
public class LockAutoScanConfig {
}
