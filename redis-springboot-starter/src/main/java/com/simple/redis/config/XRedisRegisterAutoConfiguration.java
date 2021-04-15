package com.simple.redis.config;

import com.simple.redis.annotation.XRedis;
import com.simple.redis.reflect.XRedisFactoryBean;
import com.simple.redis.util.SimpleMetadataReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.*;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.util.ClassUtils;
import org.springframework.util.SystemPropertyUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.beans.Introspector;

@Configuration
@EnableConfigurationProperties(XRedisProperties.class)
public class XRedisRegisterAutoConfiguration implements InitializingBean {

    private Logger logger = LoggerFactory.getLogger(XRedisRegisterAutoConfiguration.class);

    @Autowired
    private XRedisProperties properties;

    private static final String CLASS_SCAN_PACKAGE_PATH = "com.simple.redis.service";

    @Bean
    @ConditionalOnMissingBean
    public Jedis jedis() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(5);
        config.setTestOnBorrow(false);
        JedisPool jedisPool = new JedisPool(config, properties.getHost(), properties.getPort());
        return jedisPool.getResource();
    }

    public static class XRedisRegister implements BeanFactoryAware, ImportBeanDefinitionRegistrar, ResourceLoaderAware {

        private BeanFactory beanFactory;

        private ResourceLoader resourceLoader;

        @Override
        public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry registry) {
            try {
//                if (!AutoConfigurationPackages.has(this.beanFactory)) {
//                    return;
//                }
//
//                List<String> packages = AutoConfigurationPackages.get(this.beanFactory);
//                String basePackage = StringUtils.collectionToCommaDelimitedString(packages);
//
//                String packageSearchPath = "classpath*:" + basePackage.replace('.', '/') + "/**/*.class";
                //获取要加载的接口文件资源
                ResourceLoader resourceLoader = new PathMatchingResourcePatternResolver();
                ResourcePatternResolver resourcePatternResolver = ResourcePatternUtils.getResourcePatternResolver(resourceLoader);
                String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
                        .concat(ClassUtils.convertClassNameToResourcePath(SystemPropertyUtils.resolvePlaceholders(CLASS_SCAN_PACKAGE_PATH)).concat("/**/*.class"));
                Resource[] resources = resourcePatternResolver.getResources(packageSearchPath);

                for (Resource resource : resources) {
                    MetadataReader metadataReader = new SimpleMetadataReader(resource, ClassUtils.getDefaultClassLoader());
                    // 判断是否有注解
                    XRedis annotation = Class.forName(metadataReader.getClassMetadata().getClassName()).getAnnotation(XRedis.class);
                    if (null == annotation) continue;

                    ScannedGenericBeanDefinition beanDefinition = new ScannedGenericBeanDefinition(metadataReader);
                    String beanName = Introspector.decapitalize(ClassUtils.getShortName(beanDefinition.getBeanClassName()));

                    beanDefinition.setResource(resource);
                    beanDefinition.setSource(resource);
                    beanDefinition.setScope("singleton");
                    beanDefinition.getConstructorArgumentValues().addGenericArgumentValue(beanDefinition.getBeanClassName());
                    beanDefinition.setBeanClass(XRedisFactoryBean.class);

                    BeanDefinitionHolder definitionHolder = new BeanDefinitionHolder(beanDefinition, beanName);
                    registry.registerBeanDefinition(beanName, definitionHolder.getBeanDefinition());
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
            this.beanFactory = beanFactory;
        }

        @Override
        public void setResourceLoader(ResourceLoader resourceLoader) {
            this.resourceLoader = resourceLoader;
        }
    }

    @Configuration
    @Import(XRedisRegister.class)
    public static class MapperScannerRegistrarNotFoundConfiguration implements InitializingBean {

        @Override
        public void afterPropertiesSet() {
        }

    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }

}
