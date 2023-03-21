package com.heyd.blogapi.adapter.out.persistence.service;

import com.heyd.blogapi.application.port.out.IncrementKeywordCountRedisOutputPort;
import com.heyd.blogapi.application.port.out.PopularSearchKeywordOutputPort;
import com.heyd.blogapi.domain.exception.RedisZSetOperationException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class LettuceRedisAdapter implements IncrementKeywordCountRedisOutputPort, PopularSearchKeywordOutputPort {

    private final StringRedisTemplate redisTemplate;

    @Override
    public double incrementScore(String key, String value, double delta) {
        var zSetOperations = redisTemplate.opsForZSet();

        try {
            return zSetOperations.incrementScore(key, value, delta);
        } catch (Exception e) {
            // TODO: Logging

            throw new RedisZSetOperationException(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    String.format(
                            "Redis incrementScore error: %s [key: %s, value: %s, delta: %f]",
                            e.getMessage(), key, value, delta
                    )
            );
        }
    }

    @Override
    public Set<ZSetOperations.TypedTuple<String>> searchPopularKeywordList(String key, long start, long end) {
        var zSetOperations = redisTemplate.opsForZSet();

        try {
            return zSetOperations.reverseRangeWithScores(key, start, end);
        } catch (Exception e) {
            // TODO: Logging

            throw new RedisZSetOperationException(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    String.format(
                            "Redis reverseRangeWithScores error: %s [key: %s, start: %d, end: %d]",
                            e.getMessage(), key, start, end
                    )
            );
        }

    }
}
