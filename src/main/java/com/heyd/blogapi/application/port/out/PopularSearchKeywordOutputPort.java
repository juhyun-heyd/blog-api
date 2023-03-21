package com.heyd.blogapi.application.port.out;

import org.springframework.data.redis.core.ZSetOperations;

import java.util.Set;

public interface PopularSearchKeywordOutputPort {

    Set<ZSetOperations.TypedTuple<String>> searchPopularKeywordList(String key, long start, long end);

}
