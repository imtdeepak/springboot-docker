package sample.config;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfiguration {
    private static Logger logger = LoggerFactory.getLogger(RedisConfiguration.class);
    /**
     * REDIS_SERVICE_HOST and REDIS_SERVICE_PORT are provided as part of environment variables by Kubernetes
     * as both are in same cluster.
     */
    private @Value("${REDIS_SERVICE_HOST}")      String redisHost;
    private @Value("${REDIS_SERVICE_PORT}")      int    redisPort;
    private @Value("${REDIS_SERVICE_PASSWORD:}") String redisPassword;

    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        logger.info("Redis config {} {}", redisHost, redisPort);
        JedisConnectionFactory factory = new JedisConnectionFactory();
        factory.setHostName(redisHost);
        factory.setPort(redisPort);
        factory.setPassword(redisPassword);
        factory.setUsePool(true);
        return factory;
    }

    @Bean
    RedisTemplate<String, Object> redisTemplate() {
        final RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new JdkSerializationRedisSerializer());
        template.setValueSerializer(new GenericToStringSerializer<Object>(Object.class));
        return template;
    }

}
