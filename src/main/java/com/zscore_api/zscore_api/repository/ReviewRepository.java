package com.zscore_api.zscore_api.repository;

import com.zscore_api.zscore_api.key.GamePubKey;
import com.zscore_api.zscore_api.entity.Review;
import com.zscore_api.zscore_api.record.GameAverageScore;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ReviewRepository extends CrudRepository<Review, GamePubKey> {
    Iterable<Review> findByIdGameId(Integer gameId);
    Iterable<Review> findByGameTitle(String gameTitle);

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
    List<GameAverageScore> findAllGamesByAverageScore();
}
