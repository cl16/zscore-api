package com.zscore_api.zscore_api.repository;

import com.zscore_api.zscore_api.entity.Review;
import com.zscore_api.zscore_api.key.GamePubKey;
import com.zscore_api.zscore_api.record.StatReview;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface AdvancedRepository extends CrudRepository<Review, GamePubKey> {

    @Query("""
        SELECT NEW com.zscore_api.zscore_api.record.StatReview(
            r.id.gameId,
            r.id.pubId,
            g.title,
            p.name,
            r.score,
            s.zscore
            )
        FROM Review r 
        JOIN Stat s ON r.id.gameId = s.id.gameId AND r.id.pubId = s.id.pubId
        JOIN Game g ON r.id.gameId = g.gameId
        JOIN Publication p ON r.id.pubId = p.pubId
        WHERE r.id.gameId = :gameId
    """)
    Iterable<StatReview> findStatReviewByGameId(Integer gameId);
}
