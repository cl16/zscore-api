package com.zscore_api.zscore_api.service;

import com.zscore_api.zscore_api.record.GameAverageScore;
import com.zscore_api.zscore_api.repository.ReviewRepository;
import com.zscore_api.zscore_api.entity.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<GameAverageScore> getAllGamesByAverageScore() {
        return reviewRepository.findAllGamesByAverageScore();
    }
}
