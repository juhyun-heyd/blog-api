package com.heyd.blogapi.domain.entity;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PopularKeywo가rdTest {

    @Test
    void keyword_count를_정상적으로_증가시킨다() {
        final var entity = new PopularKeyword("keyword", 0L);

        entity.incrementCount();

        assertThat(entity.getCount()).isEqualTo(1L);
    }

}