package com.zscore_api.zscore_api.controller;

import com.zscore_api.zscore_api.entity.Game;
import com.zscore_api.zscore_api.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/game")
public class GameController {

    @Autowired
    private GameService gameService;

    @GetMapping(path="/all")
    public ResponseEntity<Iterable<Game>> getAllGames() {
        Iterable<Game> result = gameService.getAllGames();
        if (result.iterator().hasNext()) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(path="/{gameId}")
    public ResponseEntity<Optional<Game>> getGameById(@PathVariable(value="gameId") Integer gameId) {
        Optional<Game> result = gameService.getGameById(gameId);
        if (result.isPresent()) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping(path="/title/{gameTitle}")
    public ResponseEntity<Iterable<Game>> getGameByTitle(@PathVariable(value="gameTitle") String gameTitle) {
        Iterable<Game> result = gameService.getGameByTitle(gameTitle);
        if (result.iterator().hasNext()) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(result, HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping(path="")
    public ResponseEntity getGamesByParams(@RequestParam Map<String, String> params) {
        Iterable<Game> result = gameService.getGamesByParams(params);
        if (result.iterator().hasNext()) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
        }
    }
}
