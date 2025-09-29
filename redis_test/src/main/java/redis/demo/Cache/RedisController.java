package redis.demo.Cache;

import org.springframework.web.bind.annotation.*;

@RestController
public class RedisController {

    private final RedisService redisService;

    public RedisController(RedisService redisService) {
        this.redisService = redisService;
    }

    @GetMapping("/{key}")
    public String getKey(@PathVariable String key) {
        String value = redisService.getKey(key);
        return "Value for key: " + value;
    }

    @PostMapping("/{key}")
    public String setValue(@PathVariable String key, @RequestParam String value) {
        redisService.setKey(key, value);
        return "Set key: " + key + " to value: " + value;
    }
}
