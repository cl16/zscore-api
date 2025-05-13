package com.zscore_api.zscore_api.controller;

import com.zscore_api.zscore_api.entity.Publication;
import com.zscore_api.zscore_api.service.PublicationService;
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
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/publication")
public class PublicationController {

    @Autowired
    PublicationService publicationService;

    private static final Logger logger = LogManager.getLogger(PublicationService.class);

    @GetMapping(path="/all")
    public ResponseEntity<Page<Publication>> getAllPublications(@RequestParam Map<String, String> params, Pageable pageable) {
        logger.debug(" [ REQUEST RECEIVED ]");
        Page<Publication> result = publicationService.getAllPublications(params, pageable);
        if (result.iterator().hasNext()) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(result, HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping(path="/{pubId}")
    public ResponseEntity<Optional<Publication>> getPublicationById(@PathVariable(value="pubId") Integer pubId, @RequestParam Map<String, String> params) {
        logger.debug(" [ REQUEST RECEIVED ]");
        Optional<Publication> result = publicationService.getPublicationById(pubId, params);
        if (result.isPresent()) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(result, HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping(path="")
    public ResponseEntity<Page<Publication>> getPublicationsByParams(@RequestParam Map<String, String> params, Pageable pageable) {
        logger.debug(" [ REQUEST RECEIVED ]");
        Page<Publication> result = publicationService.getPublicationsByParams(params, pageable);
        if (result.iterator().hasNext()) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
        }
    }
}
