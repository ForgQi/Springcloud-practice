package com.forgqi.resourcebaseserver.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@EnableCaching
@Configuration
public class CacheConfig {
    private final RedisConnectionFactory redisConnectionFactory;
    private final ObjectMapper om;

    public CacheConfig(RedisConnectionFactory redisConnectionFactory, ObjectMapper om) {
        this.redisConnectionFactory = redisConnectionFactory;
        this.om = om;
    }

    /**
     * 通过Spring提供的RedisCacheConfiguration类，构造一个自己的redis配置类，从该配置类中可以设置一些初始化的缓存命名空间
     * 及对应的默认过期时间等属性，再利用RedisCacheManager中的builder.build()的方式生成cacheManager：
     *
     * @return 项目的缓存管理类
     */
    @Bean
    public CacheManager cacheManager() {


        // 生成一个默认配置，通过config对象即可对缓存进行自定义配置
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(RedisSerializationContext
                        .SerializationPair
                        .fromSerializer(jackson2JsonRedisSerializer()))
                .entryTtl(Duration.ofMinutes(30));  // 设置缓存的默认过期时间，也是使用Duration设置
        // .disableCachingNullValues();     // 不缓存空值

        // 对每个缓存空间应用不同的配置
        Map<String, RedisCacheConfiguration> configMap = new HashMap<>();
//        configMap.put("my-redis-cache1", redisCacheConfiguration);
        configMap.put("notice", redisCacheConfiguration.entryTtl(Duration.ofHours(12)));
        configMap.put("studyMode", redisCacheConfiguration.entryTtl(Duration.ofMinutes(5)));
        return RedisCacheManager.builder(redisConnectionFactory)
//                .initialCacheNames(cacheNames)  // 给这些缓存用默认配置，后修改的默认配置对此时设置的不起效，详见源码
                .withInitialCacheConfigurations(configMap)
                .cacheDefaults(redisCacheConfiguration) //修改默认配置
                .build();
    }

    // 设置RedisTemplate的序列化方式
    @Bean
    public RedisTemplate<Object, Object> redisTemplate() {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        // 不用json-starter装配的objectmapper的话json序列化不通用会有各种问题
//        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer();
        template.setValueSerializer(jackson2JsonRedisSerializer());
        //使用StringRedisSerializer来序列化和反序列化redis的key值
        template.setKeySerializer(new StringRedisSerializer());
        // 应该不需要，会在 Bean 初始化之后执行
//        template.afterPropertiesSet();
        return template;
    }

    private Jackson2JsonRedisSerializer<?> jackson2JsonRedisSerializer() {
        //使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值（默认使用JDK的序列化方式）
        Jackson2JsonRedisSerializer<?> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
        serializer.setObjectMapper(om);
        return serializer;
    }

//    private ObjectMapper objectMapper(){
//        //json转对象类，不设置默认的会将json转成hashmap
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
////        mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
////        mapper.registerModule(new ParameterNamesModule());
////        mapper.registerModule(new Jdk8Module());
////        mapper.registerModule(new JavaTimeModule());
//        JavaTimeModule module = new JavaTimeModule();
//        module.addDeserializer(Instant.class, InstantDeserializer.INSTANT);
//        module.addSerializer(Instant.class, InstantSerializer.INSTANCE);
//
//        mapper.registerModule(module);
//
//
//        return mapper;
//    }
}
