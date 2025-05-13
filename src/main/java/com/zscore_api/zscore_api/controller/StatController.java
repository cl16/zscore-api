package com.zscore_api.zscore_api.controller;

import com.zscore_api.zscore_api.entity.Stat;
import com.zscore_api.zscore_api.service.StatService;
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
@RequestMapping("/stat")
public class StatController {

    @Autowired
    private StatService statService;

    private static final Logger logger = LogManager.getLogger(StatService.class);

    @GetMapping(path="/all")
    public ResponseEntity<Page<Stat>> getAllStats(Pageable pageable) {
        logger.debug(" [ REQUEST RECEIVED ]");
        return new ResponseEntity<>(statService.getAllStats(pageable), HttpStatus.OK);
    }

    @GetMapping(path="/game/{gameId}")
    public ResponseEntity<Page<Stat>> getStatsByGameId(@PathVariable(value="gameId") Integer gameId, Pageable pageable) {
        logger.debug(" [ REQUEST RECEIVED ]");
        Page<Stat> result = statService.getStatsByGameId(gameId, pageable);
        if (result.iterator().hasNext()) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(result, HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping(path="/game/title/{gameTitle}")
    public ResponseEntity<Page<Stat>> getStatsByGameTitle(@PathVariable(value="gameTitle") String gameTitle, Pageable pageable) {
        logger.debug(" [ REQUEST RECEIVED ]");
        Page<Stat> result = statService.getStatsByGameTitle(gameTitle, pageable);
        if (result.iterator().hasNext()) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(result, HttpStatus.NO_CONTENT);
        }
    }
}
