package com.heyd.blogapi.domain.entity;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PopularKeywo가rdTest {

    @Test
    void keyword_count를_정상적으로_증가시킨다() {
        final var entity = new PopularKeyword("keyword", 0L);

        entity.incrementCount();

        assertThat(entity.getCount()).isEqualTo(1L);
    }

    @Test
    void 객체생성시_유효성검증에_실패하면_예외를_반환한다() {
        assertThrows(
                IllegalStateException.class,
                () -> new PopularKeyword("keyword", -10L)
        );
    }

}