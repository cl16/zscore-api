package com.zscore_api.zscore_api.controller;

import com.zscore_api.zscore_api.entity.Game;
import com.zscore_api.zscore_api.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/game")
public class GameController {

    @Autowired
    private GameService gameService;

    @GetMapping(path="/all")
    public ResponseEntity<Iterable<Game>> getAllGames(@RequestParam Map<String, String> params,Pageable pageable) {
        Iterable<Game> result = gameService.getAllGames(params, pageable);
        if (result.iterator().hasNext()) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(path="/{gameId}")
    public ResponseEntity<Optional<Game>> getGameById(@PathVariable(value="gameId") Integer gameId, @RequestParam Map<String, String> params) {
        Optional<Game> result = gameService.getGameById(gameId, params);
        if (result.isPresent()) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping(path="")
    public ResponseEntity getGamesByParams(@RequestParam Map<String, String> params, Pageable pageable) {
        Iterable<Game> result = gameService.getGamesByParams(params, pageable);
        if (result.iterator().hasNext()) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
        }
    }
}
