package com.zscore_api.zscore_api;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ReviewRepository extends CrudRepository<Review, ReviewKey> {
    List<Review> findByIdGameId(Integer gameId);
    List<Review> findByGameTitle(String gameTitle);
}
