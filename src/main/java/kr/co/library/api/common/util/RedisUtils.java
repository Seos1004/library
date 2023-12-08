package kr.co.library.api.common.util;

import kr.co.library.api.model.util.redis.CreateKeyModel;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.Instant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
public class RedisUtils {

    @Autowired
    private RedisTemplate redisTemplate;

    public String createHashTypeKey(String key , List<CreateKeyModel> createKeyModels , int expireTime){
        log.info("[RedisUtils.addRedis] START {}.{}" , key , expireTime);
        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
        if(null != createKeyModels){
            if(createKeyModels.size() > 0){
                for(CreateKeyModel createKeyModel : createKeyModels){
                    hash.put(key, createKeyModel.getKey() , createKeyModel.getValue());
                }
                redisTemplate.expire(key , expireTime , TimeUnit.MILLISECONDS);
            }else{
                log.info("[RedisUtils.addRedis] {} size is 0");
            }
        }else{
            log.info("[RedisUtils.addRedis] {} is null");
        }
        log.info("[RedisUtils.addRedis] END {}.{}" , key , expireTime);
        return key;
    }

    public void removeHashTypeKey(String key){
        log.info("[RedisUtils.removeKey] START {}" , key);
        redisTemplate.delete(key);
        log.info("[RedisUtils.removeKey] END {}" , key);
    }

    public boolean removeHashTypeRow(String key , String rowKey){
        log.info("[RedisUtils.removeHashTypeRow] START {}.{}" , key , rowKey);
        boolean result = false;
        Long deleteCount = redisTemplate.opsForHash().delete(key, rowKey);
        if(deleteCount > 0){
            result = true;
        }else{
            log.info("[RedisUtils.removeHashTypeRow] 삭제된 key 없음");
        }
        log.info("[RedisUtils.removeHashTypeRow] END {}.{}" , key , rowKey);
        return result;
    }
}
