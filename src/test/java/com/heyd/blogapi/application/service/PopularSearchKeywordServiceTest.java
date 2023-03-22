package com.heyd.blogapi.application.service;

import com.heyd.blogapi.application.port.out.PopularSearchKeywordOutputPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class PopularSearchKeywordServiceTest {

    @InjectMocks
    private PopularSearchKeywordService service;

    @Mock
    private PopularSearchKeywordOutputPort outputPort;

    @Test
    void top_N_인기_검색어_목록을_조회한다() {
        var popularKeywords = new HashSet<ZSetOperations.TypedTuple<String>>();
        popularKeywords.add(new DefaultTypedTuple<>("keyword1", 10d));
        popularKeywords.add(new DefaultTypedTuple<>("keyword2", 8d));

        given(outputPort.searchPopularKeywordList(any(), anyLong(), anyLong()))
                .willReturn(popularKeywords);

        final var topKeywords = service.query(0, 9);

        assertAll(
                () -> assertThat(topKeywords).hasSize(2),
                () -> assertThat(topKeywords.get(0).getKeyword()).isEqualTo("keyword1"),
                () -> assertThat(topKeywords.get(1).getKeyword()).isEqualTo("keyword2")
        );
    }

}