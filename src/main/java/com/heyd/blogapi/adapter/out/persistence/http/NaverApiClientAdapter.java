package com.heyd.blogapi.adapter.out.persistence.http;

import com.heyd.blogapi.adapter.in.dto.request.NaverBlogSearchRequest;
import com.heyd.blogapi.adapter.in.dto.response.NaverBlogSearchResponse;
import reactor.core.publisher.Mono;

public class NaverApiClientAdapter implements BlogSearchApiClientAdapter<NaverBlogSearchRequest, NaverBlogSearchResponse> {

    @Override
    public Mono<NaverBlogSearchResponse> searchBlog(NaverBlogSearchRequest request) {

        // TODO: implement Naver API

        return null;
    }
}
