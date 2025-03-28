package com.zscore_api.zscore_api.service;

import com.querydsl.core.BooleanBuilder;
import com.zscore_api.zscore_api.entity.QPublication;
import com.zscore_api.zscore_api.repository.PublicationRepository;
import com.zscore_api.zscore_api.entity.Publication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class PublicationService {

    @Autowired
    private PublicationRepository publicationRepository;

    Set<String> validRequestParams = new HashSet<>(Arrays.asList(
            "name", "nameContains", "avgAbove", "avgBelow", "stdAbove", "stdBelow"
    ));

    public Iterable<Publication> getAllPublications() {
        return publicationRepository.findAll();
    }

    public Optional<Publication> getPublicationById(Integer id) {
        return publicationRepository.findById(id);
    }

    public Iterable<Publication> getPublicationsByParams(Map<String, String> params) throws IllegalArgumentException {
        if (!validRequestParams.containsAll(params.keySet())) {
            throw new IllegalArgumentException("Invalid request parameters");
        }
        if (params.containsKey("name") && params.containsKey("nameContains")) {
            throw new IllegalArgumentException("Invalid request parameters");
        }

        QPublication publication = QPublication.publication;
        BooleanBuilder predicate = new BooleanBuilder();

        if (params.get("name") != null) {
            predicate.and(publication.name.eq(params.get("name")));
        }
        if (params.get("nameContains") != null) {
            predicate.and(publication.name.containsIgnoreCase(params.get("nameContains")));
        }
        if (params.get("avgAbove") != null) {
            predicate.and(publication.scoreAvg.gt(new BigDecimal(params.get("avgAbove"))));
        }
        if (params.get("avgBelow") != null) {
            predicate.and(publication.scoreAvg.lt(new BigDecimal(params.get("avgBelow"))));
        }
        if (params.get("stdAbove") != null) {
            predicate.and(publication.scoreStd.gt(new BigDecimal(params.get("stdAbove"))));
        }
        if (params.get("stdBelow") != null) {
            predicate.and(publication.scoreStd.lt(new BigDecimal(params.get("stdBelow"))));
        }

        return publicationRepository.findAll(predicate);
    }
}
