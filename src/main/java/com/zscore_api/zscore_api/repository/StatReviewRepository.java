package com.zscore_api.zscore_api.repository;

import com.zscore_api.zscore_api.entity.StatReview;
import com.zscore_api.zscore_api.entity.StatReviewWithGameDTO;
import com.zscore_api.zscore_api.key.GamePubKey;
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

    List<StatReview> findByIdGameId(Integer gameId, Pageable pageable);

    Iterable<StatReview> findByIdPubId(Integer pubId);

    @Query(
            value = """
                SELECT g.game_id, g.title, AVG(r.score) AS avgScore, AVG(s.zscore) AS avgZscore, COUNT(*) AS reviewCount
                FROM review r
                JOIN stat s ON r.game_id = s.game_id AND r.pub_id = s.pub_id
                JOIN game g ON r.game_id = g.game_id
                GROUP BY g.game_id
                HAVING (reviewCount >= :minReviewCount) AND (avgScore BETWEEN :minAvgScore AND :maxAvgScore) AND (avgZscore BETWEEN :minAvgZscore AND :maxAvgZscore)
            """,
            nativeQuery = true
    )
    List<StatReviewWithGameDTO> findAllStatReviewsGameGroupsWithAverages(
            Float minReviewCount,
            Float minAvgScore,
            Float maxAvgScore,
            Float minAvgZscore,
            Float maxAvgZscore,
            Pageable pageable
    );
}
