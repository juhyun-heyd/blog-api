package com.heyd.blogapi.application.service;

import com.heyd.blogapi.adapter.in.dto.response.KakaoBlogSearchResponse;
import com.heyd.blogapi.adapter.out.persistence.http.KakaoApiClientAdapter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.client.HttpClientErrorException;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class KakaoBlogSearchServiceTest {

    @InjectMocks
    private KakaoBlogSearchService service;

    @Mock
    private KakaoApiClientAdapter client;

    // 정상 Mono 반환
    @Test
    void 카카오_블로그_API를_정상_호출한다() {
        final var meta = new KakaoBlogSearchResponse.Meta(10, 1, false);
        final var response = new KakaoBlogSearchResponse(meta, null);
        final var pageable = PageRequest.of(1, 10, Sort.by("accurancy").descending());

        given(client.searchBlog(any()))
                .willReturn(Mono.just(response));

        final var actual = service.query(anyString(), pageable);

        assertAll(
                () -> assertThat(actual.getMeta().getTotalCount()).isEqualTo(meta.getTotalCount()),
                () -> assertThat(actual.getMeta().getPageableCount()).isEqualTo(meta.getPageableCount()),
                () -> assertThat(actual.getMeta().getIsEnd()).isEqualTo(meta.getIsEnd())
        );
    }

    @Test
    void 외부_API_호출시_예외가_발생한다() {
        final var pageable = PageRequest.of(1, 10, Sort.by("accurancy").descending());

        given(client.searchBlog(any()))
                .willThrow(HttpClientErrorException.BadRequest.class);

        assertThrows(
                HttpClientErrorException.BadRequest.class,
                () -> service.query(anyString(), pageable)
        );

    }
}