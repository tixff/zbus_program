package com.ti.zbus_manager;

import net.sf.ehcache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

/**
 * @author Ti
 * @date 2018/12/9
 */

@Configuration
@EnableCaching
public class CachingConfig {
    @Bean
    public EhCacheCacheManager cacheManager(CacheManager manager) {
        return new EhCacheCacheManager(manager);
    }

    @Bean
    public EhCacheManagerFactoryBean ehcache() {
        EhCacheManagerFactoryBean bean = new EhCacheManagerFactoryBean();
        bean.setConfigLocation(new ClassPathResource("cache/ehcache.xml"));
        return bean;
    }
}
