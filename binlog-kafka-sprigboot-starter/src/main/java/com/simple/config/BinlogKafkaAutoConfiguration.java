package com.simple.config;

import com.simple.binlog.BinlogListener;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * 配置binlog
 */
public class BinlogKafkaAutoConfiguration implements InitializingBean, ImportBeanDefinitionRegistrar {


    @Override
    public void afterPropertiesSet() throws Exception {

    }

    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        registry.registerBeanDefinition("binlogListener", new RootBeanDefinition(BinlogListener.class));
    }
}
