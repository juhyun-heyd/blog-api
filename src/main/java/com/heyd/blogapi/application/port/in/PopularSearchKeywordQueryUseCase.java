package com.heyd.blogapi.application.port.in;

import com.heyd.blogapi.adapter.in.dto.response.PopularSearchResponse;

import java.util.List;

public interface PopularSearchKeywordQueryUseCase {

    List<PopularSearchResponse> query(long start, long end);

}
