package redis.demo.Cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @CacheEvict(value = "redisCache", key = "#key")
    public void setKey(String key, String value) {
        System.out.println("Cache deleted / key = " + key);
        stringRedisTemplate.opsForValue().set(key, value);
    }

    @Cacheable(value = "redisCache", key = "#key")
    public String getKey(String key) {
        System.out.println("Method runs / key = " + key);
        return stringRedisTemplate.opsForValue().get(key);
    }
}
