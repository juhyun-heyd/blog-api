package com.heyd.blogapi.adapter.out.persistence.config.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Getter
@RequiredArgsConstructor
@Validated
@ConstructorBinding
@ConfigurationProperties("blog.search.kakao")
public class KakaoSearchProperties {

    @NotBlank
    private final String host;

    @NotBlank
    private final String uri;

    @NotBlank
    private final String key;

}
