package com.zscore_api.zscore_api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    public Iterable<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    public Iterable<Review> getReviewsByGameId(Integer gameId) {
        return reviewRepository.findByIdGameId(gameId);
    }

    public Iterable<Review> getReviewsByGameTitle(String gameTitle) {
        return reviewRepository.findByGameTitle(gameTitle);
    }
}
