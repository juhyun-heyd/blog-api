package com.heyd.blogapi.adapter.in.web;

import com.heyd.blogapi.application.port.in.BlogSearchKeywordCountCommandUseCase;
import com.heyd.blogapi.application.port.in.BlogSearchQueryUseCase;
import com.heyd.blogapi.application.port.in.PopularSearchKeywordQueryUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BlogSearchController.class)
class BlogSearchControllerTest {

    @MockBean
    private BlogSearchQueryUseCase<?> blogSearchQueryUseCase;

    @MockBean
    private BlogSearchKeywordCountCommandUseCase scoreUseCase;

    @MockBean
    private PopularSearchKeywordQueryUseCase keywordQueryUseCase;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void 블로그_검색_API() throws Exception {
        final var resultActions = getResultAction("/v1/search/blog/keyword", mockMvc);

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andDo(print());
    }

    @Test
    void 인기_검색어_목록_API() throws Exception {
        final var resultActions = getResultAction("/v1/search/popular-keyword", mockMvc);

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andDo(print());
    }

    private ResultActions getResultAction(String url, MockMvc mockMvc) throws Exception {
        return mockMvc.perform(get(url)
                .content(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8));
    }

}