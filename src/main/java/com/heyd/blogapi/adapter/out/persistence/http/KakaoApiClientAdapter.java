package com.heyd.blogapi.adapter.out.persistence.http;

import com.heyd.blogapi.adapter.in.dto.request.KakaoBlogSearchRequest;
import com.heyd.blogapi.adapter.in.dto.response.KakaoBlogSearchResponse;
import com.heyd.blogapi.application.port.out.KakaoApiClientOutputPort;
import com.heyd.blogapi.domain.exception.WebClientBadRequestException;
import com.heyd.blogapi.domain.exception.WebClientBadResponseException;
import com.heyd.blogapi.adapter.out.persistence.config.properties.KakaoSearchProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class KakaoApiClientAdapter implements
        BlogSearchApiClientAdapter<KakaoBlogSearchRequest, KakaoBlogSearchResponse>,
        KakaoApiClientOutputPort {

    private final WebClient webClient;

    private final KakaoSearchProperties properties;

    @Override
    public Mono<KakaoBlogSearchResponse> searchBlog(KakaoBlogSearchRequest request) {
        return webClient
                .method(HttpMethod.GET)
                .uri(builder -> builder.path(properties.getUri())
                        .queryParam("query", request.getQuery())
                        .queryParam("sort", request.getSize())
                        .queryParam("page", request.getPage())
                        .queryParam("size", request.getSize())
                        .build()
                )
                .retrieve()
                // TODO: 장애 발생 시 네이버 블로그 검색 API 호출하도록 우회
                .onStatus(
                        HttpStatus::is4xxClientError,
                        response -> response.bodyToMono(String.class).flatMap(
                                body -> {
                                    log.error("4xx Client Error - statusCode: {}, response: {}", response.rawStatusCode(), body);

                                    return Mono.error(
                                            new WebClientBadRequestException(
                                                    response.rawStatusCode(),
                                                    body
                                            )
                                    );
                                })
                )
                .onStatus(
                        HttpStatus::is5xxServerError,
                        response -> response.bodyToMono(String.class).flatMap(
                                body -> {
                                    log.error("5xx Server Error - statusCode: {}, response: {}", response.rawStatusCode(), body);

                                    return Mono.error(
                                            new WebClientBadResponseException(
                                                    response.rawStatusCode(),
                                                    body
                                            )
                                    );
                                })
                )
                .bodyToMono(KakaoBlogSearchResponse.class)
                .onErrorResume(WebClientBadRequestException.class, e -> Mono.empty());
    }
}
