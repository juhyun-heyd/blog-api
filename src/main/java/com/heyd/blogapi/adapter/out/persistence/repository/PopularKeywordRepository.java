package com.heyd.blogapi.adapter.out.persistence.repository;

import com.heyd.blogapi.domain.entity.PopularKeyword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PopularKeywordRepository extends JpaRepository<PopularKeyword, Long> {

    Optional<PopularKeyword> findByKeyword(String keyword);

}
