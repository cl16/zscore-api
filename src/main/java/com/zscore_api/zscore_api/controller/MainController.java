package com.zscore_api.zscore_api.controller;

import com.zscore_api.zscore_api.entity.Game;
import com.zscore_api.zscore_api.entity.Publication;
import com.zscore_api.zscore_api.entity.Review;
import com.zscore_api.zscore_api.entity.Stat;
import com.zscore_api.zscore_api.record.StatReview;
import com.zscore_api.zscore_api.repository.AdvancedRepository;
import com.zscore_api.zscore_api.service.GameService;
import com.zscore_api.zscore_api.service.PublicationService;
import com.zscore_api.zscore_api.service.ReviewService;
import com.zscore_api.zscore_api.service.StatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

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

    @Autowired
    private StatService statService;
    @Autowired
    private AdvancedRepository advancedRepository;

    @GetMapping(path="/game/all")
    public ResponseEntity<Iterable<Game>> getAllGames() {
        Iterable<Game> result = gameService.getAllGames();
        if (result.iterator().hasNext()) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(path="/game/{gameId}")
    public ResponseEntity<Optional<Game>> getGameById(@PathVariable(value="gameId") Integer gameId) {
        Optional<Game> result = gameService.getGameById(gameId);
        if (result.isPresent()) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping(path="/game/title/{gameTitle}")
    public ResponseEntity<Iterable<Game>> getGameByTitle(@PathVariable(value="gameTitle") String gameTitle) {
        Iterable<Game> result = gameService.getGameByTitle(gameTitle);
        if (result.iterator().hasNext()) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(result, HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping(path="/publication/all")
    public ResponseEntity<Iterable<Publication>> getAllPublications() {
        Iterable<Publication> result = publicationService.getAllPublications();
        if (result.iterator().hasNext()) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(result, HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping(path="/publication/{pubId}")
    public ResponseEntity<Optional<Publication>> getPublicationById(@PathVariable(value="pubId") Integer pubId) {
        Optional<Publication> result = publicationService.getPublicationById(pubId);
        if (result.isPresent()) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(result, HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping(path="/review/all")
    public ResponseEntity<Iterable<Review>> getAllReviews() {
        Iterable<Review> result = reviewService.getAllReviews();
        if (result.iterator().hasNext()) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(result, HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping(path="/review/game/{gameId}")
    public ResponseEntity<Iterable<Review>> getReviewsByGameId(@PathVariable(value="gameId") Integer gameId) {
        Iterable<Review> result = reviewService.getReviewsByGameId(gameId);
        if (result.iterator().hasNext()) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(result, HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping(path="/review/game/title/{gameTitle}")
    public ResponseEntity<Iterable<Review>> getReviewsByGameTitle(@PathVariable(value="gameTitle") String gameTitle) {
        Iterable<Review> result = reviewService.getReviewsByGameTitle(gameTitle);
        if (result.iterator().hasNext()) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(result, HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping(path="/stat/all")
    public ResponseEntity<Iterable<Stat>> getAllStats() {
        return new ResponseEntity<>(statService.getAllStats(), HttpStatus.OK);
    }

    @GetMapping(path="/stat/game/{gameId}")
    public ResponseEntity<Iterable<Stat>> getStatsByGameId(@PathVariable(value="gameId") Integer gameId) {
        Iterable<Stat> result = statService.getStatsByGameId(gameId);
        if (result.iterator().hasNext()) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(result, HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping(path="/stat/game/title/{gameTitle}")
    public ResponseEntity<Iterable<Stat>> getStatsByGameTitle(@PathVariable(value="gameTitle") String gameTitle) {
        Iterable<Stat> result = statService.getStatsByGameTitle(gameTitle);
        if (result.iterator().hasNext()) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(result, HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping(path="/stat-review/game/{gameId}")
    public ResponseEntity<Iterable<StatReview>> getStatReviewsByGameId(@PathVariable(value="gameId") Integer gameId) {
        Iterable<StatReview> result = advancedRepository.findStatReviewsByGameId(gameId);
        if (result.iterator().hasNext()) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(result, HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping(path="/stat-review/game/title/{gameTitle}")
    public ResponseEntity<Iterable<StatReview>> getStatReviewsByGameTitle(@PathVariable(value="gameTitle") String gameTitle) {
        Iterable<StatReview> result = advancedRepository.findStatReviewsByGameTitle(gameTitle);
        if (result.iterator().hasNext()) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(result, HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping(path="/stat-review/publication/{pubId}")
    public ResponseEntity<Iterable<StatReview>> getStatReviewsByPubId(@PathVariable(value="pubId") Integer pubId) {
        Iterable<StatReview> result = advancedRepository.findStatReviewsByPubId(pubId);
        if (result.iterator().hasNext()) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(result, HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping(path="/stat-review/publication/name/{pubName}")
    public ResponseEntity<Iterable<StatReview>> getStatReviewsByPubName(@PathVariable(value="pubName") String pubName) {
        Iterable<StatReview> result = advancedRepository.findStatReviewsByPubName(pubName);
        if (result.iterator().hasNext()) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(result, HttpStatus.NO_CONTENT);
        }
    }
}
