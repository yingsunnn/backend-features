package ying.backend_features.redis_options;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by ying on 2017-04-16.
 */
@RestController
@RequestMapping(value = "ro")
public class RedisOptionsController {
    private static Logger logger = LoggerFactory.getLogger(RedisOptionsController.class);

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * set key-value
     */
    @RequestMapping(value = "value_set/{message}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public String set(@PathVariable String message) {
        redisTemplate.opsForValue().set("a", message);
        return "{\"message\":\"success\"}";
    }

    /**
     * get key-value
     */
    @RequestMapping(value = "value_get/key", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public String get(@PathVariable String key) {
        String value = redisTemplate.opsForValue().get(key);
        return "{\"message\":\"" + value + "\"}";
    }

    /**
     * set key-value with ttl
     */
    @RequestMapping(value = "value_set_with_ttl/{message}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public String setWithTTL(@PathVariable String message) {
        redisTemplate.opsForValue().set("a_ttl", message, 24, TimeUnit.HOURS);
        return "{\"message\":\"success\"}";
    }

    /**
     * increase key-value
     */
    @RequestMapping(value = "increase_value", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public String increaseValue() {
        redisTemplate.opsForValue().increment("a", 1);
        return "{\"message\":\"success\"}";
    }

    /**
     * set map
     */
    @RequestMapping(value = "map_set", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public String setMap() {
        redisTemplate.opsForHash().put("map", "acc_token", "aaa");
        redisTemplate.opsForHash().put("map", "user_id", "bbb");
        return "{\"message\":\"success\"}";
    }

    /**
     * set map with ttl
     */
    @RequestMapping(value = "map_set_witl_ttl", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public String setMapWithTTL() {
        BoundHashOperations<String, String, String> mapTTL = redisTemplate.boundHashOps("map_ttl");
        mapTTL.put("acc_token", "111");
        mapTTL.put("user_id", "222");
        mapTTL.expire(60, TimeUnit.MINUTES);

        return "{\"message\":\"success\"}";
    }

    /**
     * get map
     */
    @RequestMapping(value = "map_get/{key}/{field}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public String getMap(@PathVariable String key, @PathVariable String field) {
        HashOperations<String, String, String> hashTest1 = redisTemplate.opsForHash();
        String value = hashTest1.get(key, field);
        return "{\"value\":\"" + value + "\"}";
    }

    /**
     * list set
     */
    @RequestMapping(value = "list_set", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public String setRedisList() {
        List<String> list = new ArrayList<String>();
        list.add("one");
        list.add("two");
        list.add("three");

        ListOperations listOperations = redisTemplate.opsForList();
        listOperations.rightPushAll("redis_list", list);

        return "{\"message\":\"success\"}";
    }

    /**
     * list get
     */
    @RequestMapping(value = "list_get", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public String getRedisList() {
        BoundListOperations boundListOperations = redisTemplate.boundListOps("redis_list");
        List<String> list = boundListOperations.range(0, -1);

        return "{\"value\":\"" + list + "\"}";
    }

    /**
     * has key
     */
    @RequestMapping(value = "has_key/{key}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public String hasKey(@PathVariable String key) {

        boolean flag = redisTemplate.hasKey(key);

        return "{\"value\":\"" + flag + "\"}";
    }
}
