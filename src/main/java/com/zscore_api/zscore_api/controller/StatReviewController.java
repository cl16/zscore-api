package com.zscore_api.zscore_api.controller;

import com.zscore_api.zscore_api.entity.StatReview;
import com.zscore_api.zscore_api.service.StatReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
@RequestMapping("/statReview")
public class StatReviewController {

    @Autowired
    private StatReviewService statReviewService;

    @GetMapping(path="")
    public ResponseEntity getStatReviewById(@RequestParam Map<String, String> params) {
        Iterable<StatReview> result = statReviewService.getStatReviewsByParams(params);
        if (result.iterator().hasNext()) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(result, HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping(path="/test")
    public ResponseEntity testStatReview() {
        return ResponseEntity.status(HttpStatus.OK).body("This worked!!!");
    }
}
