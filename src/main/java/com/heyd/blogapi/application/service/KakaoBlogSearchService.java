package com.heyd.blogapi.application.service;

import com.heyd.blogapi.adapter.in.dto.request.KakaoBlogSearchRequest;
import com.heyd.blogapi.adapter.in.dto.response.KakaoBlogSearchResponse;
import com.heyd.blogapi.application.port.in.BlogSearchQueryUseCase;
import com.heyd.blogapi.application.port.out.KakaoApiClientOutputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@Transactional
@RequiredArgsConstructor
public class KakaoBlogSearchService implements BlogSearchQueryUseCase<KakaoBlogSearchResponse> {

    private final KakaoApiClientOutputPort outputPort;

    @Override
    public KakaoBlogSearchResponse query(String keyword, Pageable pageable) {
        Mono<KakaoBlogSearchResponse> response = outputPort.searchBlog(
                new KakaoBlogSearchRequest(
                        keyword,
                        pageable.getSort().toString(),
                        pageable.getPageNumber(),
                        pageable.getPageSize()
                )
        );

        return response.block();
    }
}
