package com.zscore_api.zscore_api.controller;

import com.zscore_api.zscore_api.entity.Publication;
import com.zscore_api.zscore_api.service.PublicationService;
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
@RequestMapping("/publication")
public class PublicationController {

    @Autowired
    PublicationService publicationService;

    @GetMapping(path="/all")
    public ResponseEntity<Iterable<Publication>> getAllPublications() {
        Iterable<Publication> result = publicationService.getAllPublications();
        if (result.iterator().hasNext()) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(result, HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping(path="/{pubId}")
    public ResponseEntity<Optional<Publication>> getPublicationById(@PathVariable(value="pubId") Integer pubId) {
        Optional<Publication> result = publicationService.getPublicationById(pubId);
        if (result.isPresent()) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(result, HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping(path="")
    public ResponseEntity getPublicationsByParams(@RequestParam Map<String, String> params) {
        Iterable<Publication> result = publicationService.getPublicationsByParams(params);
        if (result.iterator().hasNext()) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
        }
    }
}
