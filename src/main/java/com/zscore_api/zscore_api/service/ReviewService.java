package com.zscore_api.zscore_api.service;

import com.zscore_api.zscore_api.record.GameAverageScore;
import com.zscore_api.zscore_api.repository.ReviewRepository;
import com.zscore_api.zscore_api.entity.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    public Page<Review> getAllReviews(Pageable pageable) {
        return reviewRepository.findAll(pageable);
    }

    public Page<Review> getReviewsByGameId(Integer gameId, Pageable pageable) {
        return reviewRepository.findByIdGameId(gameId, pageable);
    }

    public Page<Review> getReviewsByGameTitle(String gameTitle, Pageable pageable) {
        return reviewRepository.findByGameTitle(gameTitle, pageable);
    }

    public Page<GameAverageScore> getAllGamesByAverageScore(Pageable pageable) {
        return reviewRepository.findAllGamesByAverageScore(pageable);
    }
}
