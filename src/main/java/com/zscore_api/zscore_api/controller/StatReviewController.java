package com.zscore_api.zscore_api.controller;

import com.zscore_api.zscore_api.entity.StatReview;
import com.zscore_api.zscore_api.entity.StatReviewWithGameDTO;
import com.zscore_api.zscore_api.service.StatReviewService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
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
@RequestMapping("/statReview")
public class StatReviewController {

    @Autowired
    private StatReviewService statReviewService;

    private static final Logger logger = LogManager.getLogger(StatReviewService.class);

    @GetMapping(path="/{gameId}-{pubId}")
    public ResponseEntity getStatReviewById(@PathVariable Integer gameId, @PathVariable Integer pubId, @RequestParam Map<String, String> params) {
        logger.debug(" [ REQUEST RECEIVED ]");
        Optional<StatReview> result = statReviewService.getStatReviewById(gameId, pubId, params);
        if (result.isPresent()) {
            return new ResponseEntity(result, HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping(path="")
    public ResponseEntity getStatReviewsByParams(@RequestParam Map<String, String> params, Pageable pageable) {
        logger.debug(" [ REQUEST RECEIVED ]");
        Iterable<StatReview> result = statReviewService.getStatReviewsByParams(params, pageable);
        if (result.iterator().hasNext()) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(result, HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping(path="/game/{gameId}")
    public ResponseEntity getStatReviewsByGameId(@PathVariable Integer gameId, @RequestParam Map<String, String> params, Pageable pageable) {
        logger.debug(" [ REQUEST RECEIVED ]");
        Iterable<StatReview> result = statReviewService.getStatReviewsByGameId(gameId, params, pageable);
        if (result.iterator().hasNext()) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(result, HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping(path="/publication/{pubId}")
    public ResponseEntity getStatReviewsByPubId(@PathVariable Integer pubId, @RequestParam Map<String, String> params, Pageable pageable) {
        logger.debug(" [ REQUEST RECEIVED ]");
        Iterable<StatReview> result = statReviewService.getStatReviewsByPubId(pubId, params, pageable);
        if (result.iterator().hasNext()) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(result, HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping(path="/gameAggregate")
    public ResponseEntity getAllStatReviewGameAggregates(@RequestParam Map<String, String> params, Pageable pageable) {
        logger.debug(" [ REQUEST RECEIVED ]");
        Iterable<StatReviewWithGameDTO> result = statReviewService.getAllStatReviewGameAggregates(params, pageable);
        if (result.iterator().hasNext()) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(result, HttpStatus.NO_CONTENT);
        }
    }
}
