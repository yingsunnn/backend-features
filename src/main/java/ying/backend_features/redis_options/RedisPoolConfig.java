package ying.backend_features.redis_options;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by ying on 2017-04-16.
 */
@Configuration
public class RedisPoolConfig {
    @Value("${redis.connection.hostname}")
    private String redisHostName = "localhost";

    @Value("${redis.connection.port}")
    private Integer redisPort = 6379;

    @Value("${redis.connection.password}")
    private String redisPassword = "1ver.onica!";

    @Value("${redis.connection.pool.max-idle}")
    private int maxIdle = 1000;

    @Value("${redis.connection.pool.min-idle}")
    private int minIdle = 10;

    @Value("${redis.connection.pool.max-total}")
    private int maxTotal = 1000;

    @Value("${redis.connection.pool.max-wait}")
    private long maxWaitMillis = 100000;

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMinIdle(minIdle);
        jedisPoolConfig.setJmxEnabled(true);
        jedisPoolConfig.setMaxTotal(maxTotal);
        jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);

        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(jedisPoolConfig);
        jedisConnectionFactory.setHostName(redisHostName);
        jedisConnectionFactory.setPassword(redisPassword);
        jedisConnectionFactory.setPort(redisPort);
        return jedisConnectionFactory;
    }

    @Bean
    @Autowired
    public RedisTemplate<String, String> redisTemplate(JedisConnectionFactory jedisConnectionFactory) {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<String, String>();
        redisTemplate.setConnectionFactory(jedisConnectionFactory);

        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());

        return redisTemplate;
    }
}
