package com.zscore_api.zscore_api.repository;

import com.zscore_api.zscore_api.entity.StatReview;
import com.zscore_api.zscore_api.key.GamePubKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface StatReviewRepository extends
        CrudRepository<StatReview, GamePubKey>,
        QuerydslPredicateExecutor<StatReview>,
        JpaRepository<StatReview, GamePubKey>,
        PagingAndSortingRepository<StatReview, GamePubKey> {

    Iterable<StatReview> findByIdGameId(Integer gameId);

    Iterable<StatReview> findByIdPubId(Integer pubId);
}
