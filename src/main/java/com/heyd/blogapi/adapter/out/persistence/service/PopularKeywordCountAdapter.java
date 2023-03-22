package com.heyd.blogapi.adapter.out.persistence.service;

import com.heyd.blogapi.adapter.out.persistence.repository.PopularKeywordRepository;
import com.heyd.blogapi.application.port.out.IncrementKeywordCountRdbOutputPort;
import com.heyd.blogapi.domain.entity.PopularKeyword;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class PopularKeywordCountAdapter implements IncrementKeywordCountRdbOutputPort {

    private final PopularKeywordRepository repository;

    @Override
    public long insertOrUpdateKeyword(String keyword) {
        var popularKeyword = repository.findByKeyword(keyword)
                .orElseGet(() -> new PopularKeyword(keyword));

        popularKeyword.incrementCount();

        if (isNotExist(popularKeyword)) {
            repository.save(popularKeyword);
        }

        return popularKeyword.getCount();
    }

    private static boolean isNotExist(PopularKeyword popularKeyword) {
        return Objects.isNull(popularKeyword.getId());
    }
}
