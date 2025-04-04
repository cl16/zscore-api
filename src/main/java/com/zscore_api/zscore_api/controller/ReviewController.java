package com.zscore_api.zscore_api.controller;

import com.zscore_api.zscore_api.entity.Review;
import com.zscore_api.zscore_api.record.GameAverageScore;
import com.zscore_api.zscore_api.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping(path="/all")
    public ResponseEntity<Iterable<Review>> getAllReviews() {
        Iterable<Review> result = reviewService.getAllReviews();
        if (result.iterator().hasNext()) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(result, HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping(path="/game/{gameId}")
    public ResponseEntity<Iterable<Review>> getReviewsByGameId(@PathVariable(value="gameId") Integer gameId) {
        Iterable<Review> result = reviewService.getReviewsByGameId(gameId);
        if (result.iterator().hasNext()) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(result, HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping(path="/game/title/{gameTitle}")
    public ResponseEntity<Iterable<Review>> getReviewsByGameTitle(@PathVariable(value="gameTitle") String gameTitle) {
        Iterable<Review> result = reviewService.getReviewsByGameTitle(gameTitle);
        if (result.iterator().hasNext()) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(result, HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping(path="/game/byAverageScore")
    public ResponseEntity<Iterable<GameAverageScore>> getGamesByAverageScore() {
        return new ResponseEntity<>(reviewService.getAllGamesByAverageScore(), HttpStatus.OK);
    }
}
