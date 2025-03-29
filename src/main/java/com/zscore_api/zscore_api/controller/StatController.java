package com.zscore_api.zscore_api.controller;

import com.zscore_api.zscore_api.entity.Stat;
import com.zscore_api.zscore_api.service.StatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/stat")
public class StatController {

    @Autowired
    private StatService statService;

    @GetMapping(path="/all")
    public ResponseEntity<Iterable<Stat>> getAllStats() {
        return new ResponseEntity<>(statService.getAllStats(), HttpStatus.OK);
    }

    @GetMapping(path="/game/{gameId}")
    public ResponseEntity<Iterable<Stat>> getStatsByGameId(@PathVariable(value="gameId") Integer gameId) {
        Iterable<Stat> result = statService.getStatsByGameId(gameId);
        if (result.iterator().hasNext()) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(result, HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping(path="/game/title/{gameTitle}")
    public ResponseEntity<Iterable<Stat>> getStatsByGameTitle(@PathVariable(value="gameTitle") String gameTitle) {
        Iterable<Stat> result = statService.getStatsByGameTitle(gameTitle);
        if (result.iterator().hasNext()) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(result, HttpStatus.NO_CONTENT);
        }
    }
}
