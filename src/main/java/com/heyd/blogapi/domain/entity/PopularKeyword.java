package com.heyd.blogapi.domain.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PopularKeyword extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String keyword;

    @Column
    private Long count;

    public PopularKeyword(String keyword) {
        this(keyword, 0L);
    }

    public PopularKeyword(String keyword, Long count) {
        Assert.state(count >= 0, "count must greater than zero");

        this.keyword = keyword;
        this.count = count;
    }

    public void incrementCount() {
        this.count += 1;
    }
}
