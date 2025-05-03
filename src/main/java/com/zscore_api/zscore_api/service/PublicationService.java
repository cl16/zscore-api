package com.zscore_api.zscore_api.service;

import com.querydsl.core.BooleanBuilder;
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

    private final String NAME_STR = "name";
    private final String NAME_CONTAINS_STR = "nameContains";
    private final String SCORE_AVG_STR = "scoreAvg";
    private final String SCORE_STD_STR = "scoreStd";
    private final String MIN_SCORE_AVG_STR = "minScoreAvg";
    private final String MAX_SCORE_AVG_STR = "maxScoreAvg";
    private final String MIN_SCORE_STD_STR = "minScoreStd";
    private final String MAX_SCORE_STD_STR = "maxScoreStd";

    Set<String> validRequestParams = new HashSet<>(Arrays.asList(
            NAME_STR,
            NAME_CONTAINS_STR,
            MIN_SCORE_AVG_STR,
            MAX_SCORE_AVG_STR,
            MIN_SCORE_STD_STR,
            MAX_SCORE_STD_STR
    ));
    Set<String> sortArgs = new HashSet<>(Arrays.asList(NAME_STR, SCORE_AVG_STR, SCORE_STD_STR));

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

        if (params.containsKey(NAME_STR)) {
            predicate.and(publication.name.eq(params.get(NAME_STR)));
        }
        if (params.containsKey(NAME_CONTAINS_STR)) {
            predicate.and(publication.name.containsIgnoreCase(params.get(NAME_CONTAINS_STR)));
        }
        if (params.containsKey(MIN_SCORE_AVG_STR)) {
            predicate.and(publication.scoreAvg.goe(new BigDecimal(params.get(MIN_SCORE_AVG_STR))));
        }
        if (params.containsKey(MAX_SCORE_AVG_STR)) {
            predicate.and(publication.scoreAvg.loe(new BigDecimal(params.get(MAX_SCORE_AVG_STR))));
        }
        if (params.containsKey(MIN_SCORE_STD_STR)) {
            predicate.and(publication.scoreStd.goe(new BigDecimal(params.get(MIN_SCORE_STD_STR))));
        }
        if (params.containsKey(MAX_SCORE_STD_STR)) {
            predicate.and(publication.scoreStd.loe(new BigDecimal(params.get(MAX_SCORE_STD_STR))));
        }

        return publicationRepository.findAll(predicate, pageable);
    }

    private void validateRequestParamLogicRules(Map<String, String> params) {
        ParamValidator.validateMutuallyIncompatibleParams(params, new HashSet<>(Arrays.asList(NAME_STR, NAME_CONTAINS_STR)));

        if (params.containsKey(MIN_SCORE_AVG_STR)) {
            ParamValidator.validateNumericArg(MIN_SCORE_AVG_STR, params.get(MIN_SCORE_AVG_STR));
            ParamValidator.validateNumericRange(MIN_SCORE_AVG_STR, params.get(MIN_SCORE_AVG_STR), 0f, 100f);
        }

        if (params.containsKey(MAX_SCORE_AVG_STR)) {
            ParamValidator.validateNumericArg(MAX_SCORE_AVG_STR, params.get(MAX_SCORE_AVG_STR));
            ParamValidator.validateNumericRange(MAX_SCORE_AVG_STR, params.get(MAX_SCORE_AVG_STR), 0f, 100f);
        }

        if (params.containsKey(MIN_SCORE_AVG_STR) && params.containsKey(MAX_SCORE_AVG_STR)) {
            ParamValidator.validateMinLOEMax(
                    MIN_SCORE_AVG_STR,
                    MAX_SCORE_AVG_STR,
                    params.get(MIN_SCORE_AVG_STR),
                    params.get(MAX_SCORE_AVG_STR)
            );
        }

        if (params.containsKey(MIN_SCORE_STD_STR)) {
            ParamValidator.validateNumericArg(MIN_SCORE_STD_STR, params.get(MIN_SCORE_STD_STR));
            ParamValidator.validateNumericRange(MIN_SCORE_STD_STR, params.get(MIN_SCORE_STD_STR), 0f, 100f);
        }

        if (params.containsKey(MAX_SCORE_STD_STR)) {
            ParamValidator.validateNumericArg(MAX_SCORE_STD_STR, params.get(MAX_SCORE_STD_STR));
            ParamValidator.validateNumericRange(MAX_SCORE_STD_STR, params.get(MAX_SCORE_STD_STR), 0f, 100f);
        }

        if (params.containsKey(MIN_SCORE_STD_STR) && params.containsKey(MAX_SCORE_STD_STR)) {
            ParamValidator.validateMinLOEMax(
                    MIN_SCORE_STD_STR,
                    MAX_SCORE_STD_STR,
                    params.get(MIN_SCORE_STD_STR),
                    params.get(MAX_SCORE_STD_STR)
            );
        }
    }
}
