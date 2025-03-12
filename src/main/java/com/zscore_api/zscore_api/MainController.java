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
    private GameRepository gameRepository;

    @Autowired PublicationRepository publicationRepository;

    @Autowired ReviewRepository reviewRepository;

    @GetMapping(path="/game/all")
    public @ResponseBody Iterable<Game> getAllGames() {
        return gameRepository.findAll();
    }

    @GetMapping(path="/game/{gameId}")
    public @ResponseBody Optional<Game> getGameById(@PathVariable(value="gameId") String gameId) {
        return gameRepository.findById(Integer.parseInt(gameId));
    }

    @GetMapping(path="/publication/all")
    public @ResponseBody Iterable<Publication> getAllPublications() { return publicationRepository.findAll(); }

    @GetMapping(path="/publication/{pubId}")
    public @ResponseBody Optional<Publication> getPublicationById(@PathVariable(value="pubId") String pubId) {
        return publicationRepository.findById(Integer.parseInt(pubId));
    }

    @GetMapping(path="/score/all")
    public @ResponseBody Iterable<Review> getAllReviews() { return reviewRepository.findAll(); }
}
