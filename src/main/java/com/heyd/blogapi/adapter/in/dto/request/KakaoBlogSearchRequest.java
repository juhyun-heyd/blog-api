package com.heyd.blogapi.adapter.in.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class KakaoBlogSearchRequest {

    private String query;
    private String sort;
    private int page;
    private int size;
}
