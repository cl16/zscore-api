package com.zscore_api.zscore_api.repository;

import com.zscore_api.zscore_api.key.ReviewKey;
import com.zscore_api.zscore_api.entity.Review;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ReviewRepository extends CrudRepository<Review, ReviewKey> {
    List<Review> findByIdGameId(Integer gameId);
    List<Review> findByGameTitle(String gameTitle);
}
