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

    Set<String> validRequestParams = new HashSet<>(Arrays.asList("name", "nameContains", "averageAbove", "averageBelow"));

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
        if (params.get("averageAbove") != null) {
            predicate.and(publication.scoreAvg.gt(new BigDecimal(params.get("averageAbove"))));
        }
        if (params.get("averageBelow") != null) {
            predicate.and(publication.scoreAvg.lt(new BigDecimal(params.get("averageBelow"))));
        }

        return publicationRepository.findAll(predicate);
    }

}
