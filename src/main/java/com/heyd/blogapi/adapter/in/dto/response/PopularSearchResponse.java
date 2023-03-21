package com.heyd.blogapi.adapter.in.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PopularSearchResponse {

    private String keyword;
    private Long count;

}
