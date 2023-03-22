package com.heyd.blogapi.adapter.out.persistence.service;

import com.heyd.blogapi.domain.exception.RedisZSetOperationException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import redis.embedded.RedisServer;
import redis.embedded.RedisServerBuilder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Import(value = {LettuceRedisAdapter.class})
class LettuceRedisAdapterTest {

    @Autowired
    private LettuceRedisAdapter redisService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static RedisServer redisServer;

    @BeforeAll
    public static void startRedisServer() {
        redisServer = new RedisServerBuilder()
                .setting("bind localhost")
                .setting("port 16378")
                .build();

        redisServer.start();
    }

    @AfterAll
    public static void stopRedisServer() {
        redisServer.stop();
    }

    @Test
    void incrementScore_메서드를_정상적으로_수행한다() {
        final var expected = 1.0;

        final var actual = redisService.incrementScore("key", "value", 1.0);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void incrementScore_메서드_수행시_key가_null이면_예외가_발생한다() {
        assertThrows(
                RedisZSetOperationException.class,
                () -> redisService.incrementScore(null, "value", 1.0)
        );
    }

    @Test
    void searchPopularKeywordList_메서드를_통해_인기_검색어_목록을_조회한다() {
        redisTemplate.opsForZSet().add("key", "value1", 1.0);
        redisTemplate.opsForZSet().add("key", "value5", 5.0);
        redisTemplate.opsForZSet().add("key", "value2", 2.0);

        final var result = redisService.searchPopularKeywordList("key", 0, -1);
        final var tuple = result.iterator().next();

        assertAll(
                () -> assertThat(result).hasSize(3),
                () -> assertThat(tuple.getValue()).isEqualTo("value5"),
                () -> assertThat(tuple.getScore()).isEqualTo(5.0)
        );
    }

    @Test
    void searchPopularKeywordList_메서드_수행시_key가_null이면_예외가_발생한다() {
        assertThrows(
                RedisZSetOperationException.class,
                () -> redisService.searchPopularKeywordList(null, 0, 9)
        );
    }

}