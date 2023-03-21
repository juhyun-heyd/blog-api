package com.heyd.blogapi.adapter.out.persistence.config;

import com.heyd.blogapi.adapter.out.persistence.config.properties.KakaoSearchProperties;
import com.heyd.blogapi.adapter.out.persistence.config.properties.NaverSearchProperties;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.util.concurrent.TimeUnit;

@Configuration
@RequiredArgsConstructor
public class WebClientConfig {

    public static final int DEFAULT_TIME_OUT_MILLIS = 2_000;
    public static final String KAKAO_AUTHORIZATION = "Authorization";
    public static final String NAVER_AUTHORIZATION_ID = "X-Naver-Client-Id";
    public static final String NAVER_AUTHORIZATION_SECRET = "X-Naver-Client-Secret";

    private final KakaoSearchProperties kakaoSearchProperties;
    private final NaverSearchProperties naverSearchProperties;

    @Bean
    @Primary
    public WebClient kakaoApiWebClient() {
        return createKakaoWebClient(
                createDefaultHttpClient(),
                kakaoSearchProperties.getHost(),
                kakaoSearchProperties.getKey()
        );
    }

    @Bean
    public WebClient naverApiWebClient() {
        return createNaverWebClient(
                createDefaultHttpClient(),
                naverSearchProperties.getHost(),
                naverSearchProperties.getClientId(),
                naverSearchProperties.getClientSecret()
        );
    }

    private WebClient createKakaoWebClient(HttpClient httpClient, String baseUrl, String key) {
        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(KAKAO_AUTHORIZATION, key)
                .baseUrl(baseUrl)
                .build();
    }

    private WebClient createNaverWebClient(HttpClient httpClient, String baseUrl, String... key) {
        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(NAVER_AUTHORIZATION_ID, key[0])
                .defaultHeader(NAVER_AUTHORIZATION_SECRET, key[1])
                .baseUrl(baseUrl)
                .build();
    }

    private HttpClient createDefaultHttpClient() {
        return HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 2000)
                .doOnConnected(connection ->
                        connection
                                .addHandlerLast(new ReadTimeoutHandler(DEFAULT_TIME_OUT_MILLIS, TimeUnit.MILLISECONDS))
                                .addHandlerLast(new WriteTimeoutHandler(DEFAULT_TIME_OUT_MILLIS, TimeUnit.MILLISECONDS))
                );
    }

}
