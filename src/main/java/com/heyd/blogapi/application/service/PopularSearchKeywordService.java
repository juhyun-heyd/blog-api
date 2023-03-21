package com.heyd.blogapi.application.service;

import com.heyd.blogapi.adapter.in.dto.response.PopularSearchResponse;
import com.heyd.blogapi.application.port.in.PopularSearchKeywordQueryUseCase;
import com.heyd.blogapi.application.port.out.PopularSearchKeywordOutputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PopularSearchKeywordService implements PopularSearchKeywordQueryUseCase {

    private final PopularSearchKeywordOutputPort outputPort;

    private static final String KEY_PREFIX = "com.heyd.blog_search_keyword:";

    @Override
    public List<PopularSearchResponse> query(long start, long end) {
        final var topNKeyword = outputPort.searchPopularKeywordList(KEY_PREFIX, start, end);

        return topNKeyword.stream()
                .map(tuple -> new PopularSearchResponse(tuple.getValue(), tuple.getScore().longValue()))
                .collect(Collectors.toList());
    }
}
