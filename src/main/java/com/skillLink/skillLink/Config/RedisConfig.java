package com.skillLink.skillLink.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import tools.jackson.databind.ObjectMapper;

import java.time.Duration;


@Configuration
public class RedisConfig {
  private final ObjectMapper objectMapper;

  public RedisConfig(ObjectMapper om) {
      this.objectMapper =  om;
  }
    // using redisTemplate for email verification processes

    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory connectionFactory) {
      return new  StringRedisTemplate(connectionFactory);
    }


     @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
         RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                 .disableCachingNullValues()
                 .entryTtl(Duration.ofMinutes(15))
                 .serializeValuesWith( RedisSerializationContext.SerializationPair
                         .fromSerializer(new GenericJacksonJsonRedisSerializer(objectMapper))
                 );
        return RedisCacheManager
                .builder(connectionFactory)
                .cacheDefaults(redisCacheConfiguration)
                .build();
        // i am done with class move to methods

     }
}
