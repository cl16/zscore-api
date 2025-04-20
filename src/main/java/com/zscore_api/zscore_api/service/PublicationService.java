package com.zscore_api.zscore_api.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.Param;
import com.zscore_api.zscore_api.entity.QPublication;
import com.zscore_api.zscore_api.helper.ParamValidator;
import com.zscore_api.zscore_api.repository.PublicationRepository;
import com.zscore_api.zscore_api.entity.Publication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class PublicationService {

    @Autowired
    private PublicationRepository publicationRepository;

    Set<String> validRequestParams = new HashSet<>(Arrays.asList(
            "name", "nameContains", "minScoreAvg", "maxScoreAvg", "minScoreStd", "maxScoreStd"
    ));
    Set<String> sortArgs = new HashSet<>(Arrays.asList("name", "scoreAvg", "scoreStd"));

    public Iterable<Publication> getAllPublications(Map<String, String> params, Pageable pageable) {
        ParamValidator.validatePagingAndSortingArgs(params, sortArgs);
        ParamValidator.validateDomainRequestParams(params, new HashSet<>());
        return publicationRepository.findAll(pageable);
    }

    public Optional<Publication> getPublicationById(Integer id, Map<String, String> params) {
        ParamValidator.blockAllRequestParams(params);
        return publicationRepository.findById(id);
    }

    public Iterable<Publication> getPublicationsByParams(Map<String, String> params, Pageable pageable) {
        ParamValidator.validatePagingAndSortingArgs(params, sortArgs);
        ParamValidator.validateDomainRequestParams(params, validRequestParams);
        this.validateRequestParamLogicRules(params);

        QPublication publication = QPublication.publication;
        BooleanBuilder predicate = new BooleanBuilder();

        if (params.containsKey("name")) {
            predicate.and(publication.name.eq(params.get("name")));
        }
        if (params.containsKey("nameContains")) {
            predicate.and(publication.name.containsIgnoreCase(params.get("nameContains")));
        }
        if (params.containsKey("minScoreAvg")) {
            predicate.and(publication.scoreAvg.goe(new BigDecimal(params.get("minScoreAvg"))));
        }
        if (params.containsKey("maxScoreAvg")) {
            predicate.and(publication.scoreAvg.loe(new BigDecimal(params.get("maxScoreAvg"))));
        }
        if (params.containsKey("minScoreStd")) {
            predicate.and(publication.scoreStd.goe(new BigDecimal(params.get("minScoreStd"))));
        }
        if (params.containsKey("maxScoreStd")) {
            predicate.and(publication.scoreStd.loe(new BigDecimal(params.get("maxScoreStd"))));
        }

        return publicationRepository.findAll(predicate, pageable);
    }

    private void validateRequestParamLogicRules(Map<String, String> params) {
        if (params.containsKey("name") && params.containsKey("nameContains")) {
            throw new IllegalArgumentException("Invalid request parameters: only 1 allowed from name, nameContains");
        }

        if (params.containsKey("minScoreAvg")) {
            ParamValidator.validateNumericArg("minScoreAvg", params.get("minScoreAvg"));
            ParamValidator.validateNumericRange("minScoreAvg", params.get("minScoreAvg"), 0f, 100f);
        }

        if (params.containsKey("maxScoreAvg")) {
            String param = "maxScoreAvg";
            String arg = params.get(param);
            ParamValidator.validateNumericArg("maxScoreAvg", params.get("maxScoreAvg"));
            ParamValidator.validateNumericRange("maxScoreAvg", params.get("maxScoreAvg"), 0f, 100f);
        }

        if (params.containsKey("minScoreAvg") && params.containsKey("maxScoreAvg")) {
            ParamValidator.validateMinLTEMax(
                    "minScoreAvg",
                    "maxScoreAvg",
                    params.get("minScoreAvg"),
                    params.get("maxScoreAvg")
            );
        }

        if (params.containsKey("minScoreStd")) {
            ParamValidator.validateNumericArg("minScoreStd", params.get("minScoreStd"));
            ParamValidator.validateNumericRange("minScoreStd", params.get("minScoreStd"), 0f, 100f);
        }

        if (params.containsKey("maxScoreStd")) {
            ParamValidator.validateNumericArg("maxScoreStd", params.get("maxScoreStd"));
            ParamValidator.validateNumericRange("maxScoreStd", params.get("maxScoreStd"), 0f, 100f);
        }

        if (params.containsKey("minScoreStd") && params.containsKey("maxScoreStd")) {
            ParamValidator.validateMinLTEMax(
                    "minScoreStd",
                    "maxScoreStd",
                    params.get("minScoreStd"),
                    params.get("maxScoreStd")
            );
        }
    }
}
