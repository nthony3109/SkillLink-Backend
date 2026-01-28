package com.skillLink.skillLink.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final StringRedisTemplate redisTemplate;

    //to save code for 30 mins
    public void saveCode(String email, String code) {
        redisTemplate.opsForValue()
                .set(email, code, 15, TimeUnit.MINUTES);
    }

    public boolean verifyCode(String email, String code) {
        String codeNotNull = code != null ? code : "";
        String storedCode = redisTemplate.opsForValue().get(email);
        if(storedCode != null && storedCode.equals(codeNotNull)  ) {
            redisTemplate.delete(email);
            return true;
        }
        return false;
    }
}
