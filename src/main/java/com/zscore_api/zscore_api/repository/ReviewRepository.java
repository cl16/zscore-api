package com.zscore_api.zscore_api.repository;

import com.zscore_api.zscore_api.key.ReviewKey;
import com.zscore_api.zscore_api.entity.Review;
import org.springframework.data.repository.CrudRepository;

public interface ReviewRepository extends CrudRepository<Review, ReviewKey> {
    Iterable<Review> findByIdGameId(Integer gameId);
    Iterable<Review> findByGameTitle(String gameTitle);
}
