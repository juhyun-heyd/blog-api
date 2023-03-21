package com.heyd.blogapi.application.port.in;

import org.springframework.data.domain.Pageable;

public interface BlogSearchQueryUseCase<T> {

    T query(String keyword, Pageable pageable);

}
