package com.zscore_api.zscore_api.repository;

import com.zscore_api.zscore_api.key.GamePubKey;
import com.zscore_api.zscore_api.entity.Review;
import org.springframework.data.repository.CrudRepository;

public interface ReviewRepository extends CrudRepository<Review, GamePubKey> {
    Iterable<Review> findByIdGameId(Integer gameId);
    Iterable<Review> findByGameTitle(String gameTitle);
}
