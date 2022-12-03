package com.fairy.rocketmq.config;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * @author 鹿少年
 * @date 2022/7/12 9:44
 */
@Slf4j
@Configuration
public class CacheConfig {

    @Bean
    public Cache<String,String> cache(){
        Cache<String, String> numCache = CacheBuilder.newBuilder()
                .expireAfterWrite(1, TimeUnit.MINUTES)
                .maximumSize(1000)
                .initialCapacity(100)
                .concurrencyLevel(Runtime.getRuntime().availableProcessors())
                .build(new CacheLoader<String, String>() {
                    @Override
                    public String load(String key) throws Exception {
                        log.info("缓存不存在数据");
                        return null;
                    }
                });
        return numCache;
    }

}
