package com.zscore_api.zscore_api.controller;

import com.zscore_api.zscore_api.entity.StatReview;
import com.zscore_api.zscore_api.entity.StatReviewWithGameDTO;
import com.zscore_api.zscore_api.service.StatReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
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
    public ResponseEntity getStatReviewById(@RequestParam Map<String, String> params, Pageable pageable) {
        Iterable<StatReview> result = statReviewService.getStatReviewsByParams(params, pageable);
        if (result.iterator().hasNext()) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(result, HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping(path="/gameAverages")
    public ResponseEntity getAllStatReviewGameGroupsWithAverages(@RequestParam(name = "minReviewCount", defaultValue = "4") String minReviewCount, Pageable pageable) {
        Iterable<StatReviewWithGameDTO> result = statReviewService.getAllStatReviewGameGroupsWithAverages(minReviewCount, pageable);
        if (result.iterator().hasNext()) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(result, HttpStatus.NO_CONTENT);
        }
    }
}
