package com.heyd.blogapi.application.service;

import com.heyd.blogapi.application.port.out.IncrementKeywordCountRdbOutputPort;
import com.heyd.blogapi.application.port.out.IncrementKeywordCountRedisOutputPort;
import com.heyd.blogapi.domain.exception.DistributedLockException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class IncrementKeywordCountServiceTest {

    @InjectMocks
    private IncrementKeywordCountService service;

    @Mock
    private IncrementKeywordCountRedisOutputPort redisOutputPort;

    @Mock
    private IncrementKeywordCountRdbOutputPort rdbOutputPort;

    @Mock
    private RedissonClient redissonClient;

    private RLock lock;

    private static final String KEYWORD = "keyword";

    private static final long WAIT_TIME = 1500;

    private static final long LEASE_TIME = 2000;

    @BeforeEach
    void setUp() {
        lock = mock(RLock.class);
    }

    @Test
    void Redis_incrementScore_메서드를_정상적으로_호출한다() throws InterruptedException {
        final var key = "com.heyd.blog_search_keyword:";

        given(redissonClient.getLock(anyString()))
                .willReturn(lock);
        given(lock.tryLock(WAIT_TIME, LEASE_TIME, TimeUnit.MILLISECONDS))
                .willReturn(true);
        given(redisOutputPort.incrementScore(key, KEYWORD, 1.0d))
                .willReturn(1.0d);
        given(rdbOutputPort.insertOrUpdateKeyword(KEYWORD))
                .willReturn(1L);

        service.command(KEYWORD);

        verify(redissonClient, times(1)).getLock(anyString());
        verify(redisOutputPort, times(1))
                .incrementScore(key, KEYWORD, 1.0d);
        verify(rdbOutputPort, times(1))
                .insertOrUpdateKeyword(KEYWORD);
    }

    @Test
    void Lock을_획득하지_못할경우_메서드를_종료한다() throws InterruptedException {
        final var key = "com.heyd.blog_search_keyword:";

        given(redissonClient.getLock(anyString()))
                .willReturn(lock);
        given(lock.tryLock(WAIT_TIME, LEASE_TIME, TimeUnit.MILLISECONDS))
                .willReturn(false);

        service.command(KEYWORD);

        verify(redissonClient, times(1))
                .getLock(anyString());
        verify(redisOutputPort, never())
                .incrementScore(key, KEYWORD, 1.0d);
        verify(rdbOutputPort, never())
                .insertOrUpdateKeyword(KEYWORD);
    }

    @Test
    void Lock_획득시_에외가_발생할경우_Custom_예외가_발생한다() throws InterruptedException {
        given(redissonClient.getLock(anyString()))
                .willReturn(lock);
        given(lock.tryLock(WAIT_TIME, LEASE_TIME, TimeUnit.MILLISECONDS))
                .willThrow(InterruptedException.class);

        assertThrows(
                DistributedLockException.class,
                () -> service.command(KEYWORD)
        );
    }
}