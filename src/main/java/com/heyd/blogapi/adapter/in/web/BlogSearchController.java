package com.heyd.blogapi.adapter.in.web;

import com.heyd.blogapi.adapter.in.dto.ApiResult;
import com.heyd.blogapi.application.port.in.BlogSearchKeywordCountCommandUseCase;
import com.heyd.blogapi.application.port.in.BlogSearchQueryUseCase;
import com.heyd.blogapi.application.port.in.PopularSearchKeywordQueryUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.heyd.blogapi.common.Constant.POPULAR_KEYWORD_LIST_END;
import static com.heyd.blogapi.common.Constant.POPULAR_KEYWORD_LIST_START;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class BlogSearchController {
    private final BlogSearchQueryUseCase<?> blogSearchQueryUseCase;
    private final BlogSearchKeywordCountCommandUseCase blogSearchKeywordCountCommandUseCase;
    private final PopularSearchKeywordQueryUseCase popularSearchKeywordQueryUseCase;

    @GetMapping("/search/blog/{keyword}")
    public ApiResult<?> searchBlog(
            @PathVariable String keyword,
            @RequestParam(required = false, defaultValue = "accurancy") String sort,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size
    ) throws InterruptedException {
        // TODO: Queue 도입
        blogSearchKeywordCountCommandUseCase.command(keyword);

        final var pageRequest = PageRequest.of(page, size, Sort.by(sort).descending());

        return ApiResult.ok(
                blogSearchQueryUseCase.query(
                        keyword,
                        pageRequest
                )
        );
    }

    @GetMapping("/search/popular-keyword")
    public ApiResult<?> searchPopularKeyword() {
        return ApiResult.ok(
                popularSearchKeywordQueryUseCase.query(
                        POPULAR_KEYWORD_LIST_START,
                        POPULAR_KEYWORD_LIST_END
                )
        );
    }

}
