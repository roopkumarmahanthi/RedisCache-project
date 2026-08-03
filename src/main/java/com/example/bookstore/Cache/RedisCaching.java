package com.example.bookstore.Cache;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisCaching {

	    @Bean
	    public RedisTemplate<String, Object> redisTemplate(
	            RedisConnectionFactory connectionFactory) {

	        RedisTemplate<String, Object> template = new RedisTemplate<>();
	        template.setConnectionFactory(connectionFactory);
	        return template;
	    }
}
