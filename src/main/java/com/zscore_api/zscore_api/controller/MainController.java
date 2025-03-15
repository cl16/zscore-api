package com.zscore_api.zscore_api.controller;

import com.zscore_api.zscore_api.entity.Game;
import com.zscore_api.zscore_api.entity.Publication;
import com.zscore_api.zscore_api.entity.Review;
import com.zscore_api.zscore_api.service.GameService;
import com.zscore_api.zscore_api.service.PublicationService;
import com.zscore_api.zscore_api.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;

@Controller
@RequestMapping("")
public class MainController {

    @Autowired
    private GameService gameService;

    @Autowired
    private PublicationService publicationService;

    @Autowired
    private ReviewService reviewService;

    @GetMapping(path="/game/all")
    public @ResponseBody Iterable<Game> getAllGames() {
        return gameService.getAllGames();
    }

    @GetMapping(path="/game/{gameId}")
    public ResponseEntity<Optional<Game>> getGameById(@PathVariable(value="gameId") Integer gameId) {
        Optional<Game> result = gameService.getGameById(gameId);
        if (result.isPresent()) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(path="/game/title/{gameTitle}")
    public ResponseEntity<Iterable<Game>> getGameByTitle(@PathVariable(value="gameTitle") String gameTitle) {
        Iterable<Game> result = gameService.getGameByTitle(gameTitle);
        if (result.iterator().hasNext()) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(path="/publication/all")
    public @ResponseBody Iterable<Publication> getAllPublications() {
        return publicationService.getAllPublications();
    }

    @GetMapping(path="/publication/{pubId}")
    public ResponseEntity<Optional<Publication>> getPublicationById(@PathVariable(value="pubId") Integer pubId) {
        Optional<Publication> result = publicationService.getPublicationById(pubId);
        if (result.isPresent()) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(path="/review/all")
    public @ResponseBody Iterable<Review> getAllReviews() {
        return reviewService.getAllReviews();
    }

    @GetMapping(path="/review/game/{gameId}")
    public ResponseEntity<Iterable<Review>> getReviewsByGameId(@PathVariable(value="gameId") Integer gameId) {
        Iterable<Review> result = reviewService.getReviewsByGameId(gameId);
        if (result.iterator().hasNext()) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
        }
    }

    @ GetMapping(path="/review/game/title/{gameTitle}")
    public ResponseEntity<Iterable<Review>> getReviewsByGameTitle(@PathVariable(value="gameTitle") String gameTitle) {
        Iterable<Review> result = reviewService.getReviewsByGameTitle(gameTitle);
        if (result.iterator().hasNext()) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
        }
    }
}
