package com.example.jobportal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final long CACHE_EXPIRATION = 10; // Cache expires in 10 minutes

    public void saveToRedis(String key, String value) {
        redisTemplate.opsForValue().set(key, value, CACHE_EXPIRATION, TimeUnit.MINUTES);
    }

    public String getFromRedis(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void deleteFromRedis(String key) {
        redisTemplate.delete(key);
    }
}
