package com.heyd.blogapi.application.port.out;

import com.heyd.blogapi.adapter.in.dto.request.KakaoBlogSearchRequest;
import com.heyd.blogapi.adapter.in.dto.response.KakaoBlogSearchResponse;
import reactor.core.publisher.Mono;

public interface KakaoApiClientOutputPort {

    Mono<KakaoBlogSearchResponse> searchBlog(KakaoBlogSearchRequest kakaoBlogSearchRequest);

}
