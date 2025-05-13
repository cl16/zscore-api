package com.zscore_api.zscore_api.controller;

import com.zscore_api.zscore_api.entity.Review;
import com.zscore_api.zscore_api.record.GameAverageScore;
import com.zscore_api.zscore_api.service.ReviewService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/review")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    private static final Logger logger = LogManager.getLogger(ReviewService.class);

    @GetMapping(path="/all")
    public ResponseEntity<Page<Review>> getAllReviews(Pageable pageable) {
        logger.debug(" [ REQUEST RECEIVED ]");
        Page<Review> result = reviewService.getAllReviews(pageable);
        if (result.iterator().hasNext()) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(result, HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping(path="/game/{gameId}")
    public ResponseEntity<Page<Review>> getReviewsByGameId(@PathVariable(value="gameId") Integer gameId, Pageable pageable) {
        logger.debug(" [ REQUEST RECEIVED ]");
        Page<Review> result = reviewService.getReviewsByGameId(gameId, pageable);
        if (result.iterator().hasNext()) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(result, HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping(path="/game/title/{gameTitle}")
    public ResponseEntity<Page<Review>> getReviewsByGameTitle(@PathVariable(value="gameTitle") String gameTitle, Pageable pageable) {
        logger.debug(" [ REQUEST RECEIVED ]");
        Page<Review> result = reviewService.getReviewsByGameTitle(gameTitle, pageable);
        if (result.iterator().hasNext()) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(result, HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping(path="/game/byAverageScore")
    public ResponseEntity<Page<GameAverageScore>> getGamesByAverageScore(Pageable pageable) {
        logger.debug(" [ REQUEST RECEIVED ]");
        return new ResponseEntity<>(reviewService.getAllGamesByAverageScore(pageable), HttpStatus.OK);
    }
}
