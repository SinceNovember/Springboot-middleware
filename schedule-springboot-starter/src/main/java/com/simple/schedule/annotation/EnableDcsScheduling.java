package com.simple.schedule.annotation;

import com.simple.schedule.DoJoinPoint;
import com.simple.schedule.config.DcsSchedulingConfiguration;
import com.simple.schedule.task.CronTaskRegister;
import com.simple.schedule.task.SchedulingConfig;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({DcsSchedulingConfiguration.class})
@ImportAutoConfiguration({SchedulingConfig.class, CronTaskRegister.class, DoJoinPoint.class})
@ComponentScan("com.simple.*")
public @interface EnableDcsScheduling {
}
