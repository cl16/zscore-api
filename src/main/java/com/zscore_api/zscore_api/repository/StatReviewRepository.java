package com.zscore_api.zscore_api.repository;

import com.zscore_api.zscore_api.entity.StatReview;
import com.zscore_api.zscore_api.entity.StatReviewWithGameDTO;
import com.zscore_api.zscore_api.key.GamePubKey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface StatReviewRepository extends
        CrudRepository<StatReview, GamePubKey>,
        QuerydslPredicateExecutor<StatReview>,
        JpaRepository<StatReview, GamePubKey>,
        PagingAndSortingRepository<StatReview, GamePubKey> {

    Page<StatReview> findByIdGameId(Integer gameId, Pageable pageable);

    Page<StatReview> findByIdPubId(Integer pubId, Pageable pageable);

    @Query(
            value = """
                SELECT g.game_id, g.title, AVG(r.score) AS scoreAvg, AVG(s.zscore) AS zscoreAvg, COUNT(*) AS reviewCount
                FROM review r
                JOIN stat s ON r.game_id = s.game_id AND r.pub_id = s.pub_id
                JOIN game g ON r.game_id = g.game_id
                GROUP BY g.game_id
                HAVING (reviewCount >= :minReviewCount) AND (scoreAvg BETWEEN :minScoreAvg AND :maxScoreAvg) AND (zscoreAvg BETWEEN :minZscoreAvg AND :maxZscoreAvg)
            """,
            nativeQuery = true
    )
    Page<StatReviewWithGameDTO> findAllStatReviewsGameAggregates(
            Float minReviewCount,
            Float minScoreAvg,
            Float maxScoreAvg,
            Float minZscoreAvg,
            Float maxZscoreAvg,
            Pageable pageable
    );
}
