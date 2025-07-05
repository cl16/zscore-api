package com.zscore_api.zscore_api.repository;

import com.zscore_api.zscore_api.entity.Game;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface GameRepository extends CrudRepository<Game, Integer>,
        QuerydslPredicateExecutor<Game>,
        JpaRepository<Game, Integer>,
        PagingAndSortingRepository<Game, Integer> {

    Page<Game> findByTitle(String title, Pageable pageable);

    Page<Game> findByTitleContains(String title, Pageable pageable);
}
