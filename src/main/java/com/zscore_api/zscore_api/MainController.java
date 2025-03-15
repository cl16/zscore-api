package com.zscore_api.zscore_api;

import org.springframework.beans.factory.annotation.Autowired;
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
    public @ResponseBody Optional<Game> getGameById(@PathVariable(value="gameId") Integer gameId) {
        return gameService.getGameById(gameId);
    }

    @GetMapping(path="/game/title/{gameTitle}")
    public @ResponseBody Iterable<Game> getGameByTitle(@PathVariable(value="gameTitle") String gameTitle) {
        return gameService.getGameByTitle(gameTitle);
    }

    @GetMapping(path="/publication/all")
    public @ResponseBody Iterable<Publication> getAllPublications() { return publicationService.getAllPublications(); }

    @GetMapping(path="/publication/{pubId}")
    public @ResponseBody Optional<Publication> getPublicationById(@PathVariable(value="pubId") Integer pubId) {
        return publicationService.getPublicationById(pubId);
    }

    @GetMapping(path="/review/all")
    public @ResponseBody Iterable<Review> getAllReviews() { return reviewService.getAllReviews(); }

    @GetMapping(path="/review/game/{gameId}")
    public @ResponseBody Iterable<Review> getReviewsByGameId(@PathVariable(value="gameId") Integer gameId) {
        return reviewService.getReviewsByGameId(gameId);
    }

    @ GetMapping(path="/review/game/title/{gameTitle}")
    public @ResponseBody Iterable<Review> getReviewsByGameTitle(@PathVariable(value="gameTitle") String gameTitle) {
        return reviewService.getReviewsByGameTitle(gameTitle);
    }
}
