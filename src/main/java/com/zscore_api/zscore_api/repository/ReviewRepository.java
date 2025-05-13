package com.zscore_api.zscore_api.repository;

import com.zscore_api.zscore_api.key.GamePubKey;
import com.zscore_api.zscore_api.entity.Review;
import com.zscore_api.zscore_api.record.GameAverageScore;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ReviewRepository extends
        CrudRepository<Review, GamePubKey>,
        PagingAndSortingRepository<Review, GamePubKey> {

    Page<Review> findByIdGameId(Integer gameId, Pageable pageable);
    Page<Review> findByGameTitle(String gameTitle, Pageable pageable);

    @Query("""
        SELECT NEW com.zscore_api.zscore_api.record.GameAverageScore(
            g.gameId,
            g.title,
            AVG(r.score),
            COUNT(*)
            )
        FROM Review r
        JOIN Game g ON r.game.gameId = g.gameId
        GROUP BY r.game.gameId
        HAVING COUNT(*) > 4
        ORDER BY AVG(r.score) DESC
    """)
    Page<GameAverageScore> findAllGamesByAverageScore(Pageable pageable);
}
