package com.zscore_api.zscore_api.service;

import com.querydsl.core.BooleanBuilder;
import com.zscore_api.zscore_api.entity.QStatReview;
import com.zscore_api.zscore_api.entity.StatReview;
import com.zscore_api.zscore_api.entity.StatReviewWithGameDTO;
import com.zscore_api.zscore_api.helper.ParamValidator;
import com.zscore_api.zscore_api.key.GamePubKey;
import com.zscore_api.zscore_api.repository.StatReviewRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class StatReviewService {

    @Autowired
    private StatReviewRepository statReviewRepository;

    private final String GAME_ID_STR = "gameId";
    private final String PUB_ID_STR = "pubId";
    private final String GAME_TITLE_CONTAINS_STR = "gameTitleContains";
    private final String PUB_NAME_CONTAINS_STR = "pubNameContains";
    private final String SCORE_STR = "score";
    private final String ZSCORE_STR = "zscore";
    private final String AVG_SCORE_STR = "scoreAvg";
    private final String AVG_ZSCORE_STR = "zscoreAvg";
    private final String MIN_SCORE_STR = "minScore";
    private final String MAX_SCORE_STR = "maxScore";
    private final String MIN_ZSCORE_STR = "minZscore";
    private final String MAX_ZSCORE_STR = "maxZscore";
    private final String MIN_REVIEW_COUNT_STR = "minReviewCount";
    private final String MIN_AVG_SCORE_STR = "minScoreAvg";
    private final String MAX_AVG_SCORE_STR = "maxScoreAvg";
    private final String MIN_AVG_ZSCORE_STR = "minZscoreAvg";
    private final String MAX_AVG_ZSCORE_STR = "maxZscoreAvg";
    
    Set<String> statReviewParams = new HashSet<>(Arrays.asList(
            GAME_ID_STR,
            PUB_ID_STR,
            GAME_TITLE_CONTAINS_STR,
            PUB_NAME_CONTAINS_STR,
            MIN_SCORE_STR,
            MAX_SCORE_STR,
            MIN_ZSCORE_STR,
            MAX_ZSCORE_STR
    ));
    Set<String> statReviewSortArgs = new HashSet<>(Arrays.asList(SCORE_STR, ZSCORE_STR));
    Set<String> statReviewGroupParams = new HashSet<>(Arrays.asList(
            MIN_REVIEW_COUNT_STR,
            MIN_AVG_SCORE_STR,
            MAX_AVG_SCORE_STR,
            MIN_AVG_ZSCORE_STR,
            MAX_AVG_ZSCORE_STR
    ));
    Set<String> statReviewGroupSortArgs = new HashSet<>(Arrays.asList(AVG_SCORE_STR, AVG_ZSCORE_STR));

    private static final Logger logger = LogManager.getLogger(StatReviewService.class);

    public Optional<StatReview> getStatReviewById(Integer gameId, Integer pubId, Map<String, String> params) {
        logger.debug(String.format("gameId %s, pubId %s, args %s", gameId, pubId, params));
        ParamValidator.blockAllRequestParams(params);
        return statReviewRepository.findById(new GamePubKey(gameId, pubId));
    }

    public Page<StatReview> getStatReviewsByGameId(Integer gameId, Map<String, String> params, Pageable pageable) {
        logger.debug(String.format("gameId %s, args %s", gameId, params));
        ParamValidator.validatePagingAndSortingArgs(params, statReviewSortArgs);
        ParamValidator.validateDomainRequestParams(params, new HashSet<>());
        return statReviewRepository.findByIdGameId(gameId, pageable);
    }

    public Page<StatReview> getStatReviewsByPubId(Integer pubId, Map<String, String> params, Pageable pageable) {
        logger.debug(String.format("pubId %s, args %s", pubId, params));
        ParamValidator.validatePagingAndSortingArgs(params, statReviewSortArgs);
        ParamValidator.validateDomainRequestParams(params, new HashSet<>());
        return statReviewRepository.findByIdPubId(pubId, pageable);
    }

    public Page<StatReview> getStatReviewsByParams(Map<String, String> params, Pageable pageable) {
        logger.debug(String.format("args %s", params));
        ParamValidator.validatePagingAndSortingArgs(params, statReviewSortArgs);
        ParamValidator.validateDomainRequestParams(params, statReviewParams);
        this.validateStatReviewParamLogicRules(params);

        QStatReview statReview = QStatReview.statReview;
        BooleanBuilder predicate = new BooleanBuilder();

        if (params.containsKey(GAME_ID_STR)) {
            predicate.and(statReview.id.gameId.eq(Integer.parseInt(params.get(GAME_ID_STR))));
        }
        if (params.containsKey(PUB_ID_STR)) {
            predicate.and(statReview.id.pubId.eq(Integer.parseInt(params.get(PUB_ID_STR))));
        }
        if (params.containsKey(GAME_TITLE_CONTAINS_STR)) {
            predicate.and(statReview.game.title.containsIgnoreCase(params.get(GAME_TITLE_CONTAINS_STR)));
        }
        if (params.containsKey(PUB_NAME_CONTAINS_STR)) {
            predicate.and(statReview.publication.name.containsIgnoreCase(params.get(PUB_NAME_CONTAINS_STR)));
        }
        if (params.containsKey(MIN_SCORE_STR)) {
            predicate.and(statReview.score.goe(Integer.parseInt(params.get(MIN_SCORE_STR))));
        }
        if (params.containsKey(MAX_SCORE_STR)) {
            predicate.and(statReview.score.loe(Integer.parseInt(params.get(MAX_SCORE_STR))));
        }
        if (params.containsKey(MIN_ZSCORE_STR)) {
            predicate.and(statReview.zscore.goe(new BigDecimal(params.get(MIN_ZSCORE_STR))));
        }
        if (params.containsKey(MAX_ZSCORE_STR)) {
            predicate.and(statReview.zscore.loe(new BigDecimal(params.get(MAX_ZSCORE_STR))));
        }

        return statReviewRepository.findAll(predicate, pageable);
    }

    private void validateStatReviewParamLogicRules(Map<String, String> params) {
        if (params.containsKey(MIN_SCORE_STR)) {
            ParamValidator.validateNumericArg(MIN_SCORE_STR, params.get(MIN_SCORE_STR));
            ParamValidator.validateNumericRange(MIN_SCORE_STR, params.get(MIN_SCORE_STR), 0f, 100f);
        }

        if (params.containsKey(MAX_SCORE_STR)) {
            ParamValidator.validateNumericArg(MAX_SCORE_STR, params.get(MAX_SCORE_STR));
            ParamValidator.validateNumericRange(MAX_SCORE_STR, params.get(MAX_SCORE_STR), 0f, 100f);
        }

        if (params.containsKey(MIN_SCORE_STR) && params.containsKey(MAX_SCORE_STR)) {
            ParamValidator.validateMinLOEMax(
                    MIN_SCORE_STR,
                    MAX_SCORE_STR,
                    params.get(MIN_SCORE_STR),
                    params.get(MAX_SCORE_STR)
            );
        }

        if (params.containsKey(MIN_ZSCORE_STR)) {
            ParamValidator.validateNumericArg(MIN_ZSCORE_STR, params.get(MIN_ZSCORE_STR));
        }

        if (params.containsKey(MAX_ZSCORE_STR)) {
            ParamValidator.validateNumericArg(MAX_ZSCORE_STR, params.get(MAX_ZSCORE_STR));
        }

        if (params.containsKey(MIN_ZSCORE_STR) && params.containsKey(MAX_ZSCORE_STR)) {
            ParamValidator.validateMinLOEMax(
                    MIN_ZSCORE_STR,
                    MAX_ZSCORE_STR,
                    params.get(MIN_ZSCORE_STR),
                    params.get(MAX_ZSCORE_STR)
            );
        }
    }

    private void validateStatReviewGroupParamLogicRules(Map<String, String> params) {
        if (params.containsKey(MIN_REVIEW_COUNT_STR)) {
            ParamValidator.validateNumericArg(MIN_REVIEW_COUNT_STR, params.get(MIN_REVIEW_COUNT_STR));
        }

        if (params.containsKey(MIN_AVG_SCORE_STR)) {
            ParamValidator.validateNumericArg(MIN_AVG_SCORE_STR, params.get(MIN_AVG_SCORE_STR));
            ParamValidator.validateNumericRange(MIN_AVG_SCORE_STR, params.get(MIN_AVG_SCORE_STR), 0f, 100f);
        }

        if (params.containsKey(MAX_AVG_SCORE_STR)) {
            ParamValidator.validateNumericArg(MAX_AVG_SCORE_STR, params.get(MAX_AVG_SCORE_STR));
            ParamValidator.validateNumericRange(MAX_AVG_SCORE_STR, params.get(MAX_AVG_SCORE_STR), 0f, 100f);
        }

        if (params.containsKey(MIN_AVG_SCORE_STR) && params.containsKey(MAX_AVG_SCORE_STR)) {
            ParamValidator.validateMinLOEMax(
                    MIN_AVG_SCORE_STR,
                    MAX_AVG_SCORE_STR,
                    params.get(MIN_AVG_SCORE_STR),
                    params.get(MAX_AVG_SCORE_STR)
            );
        }

        if (params.containsKey(MIN_AVG_ZSCORE_STR)) {
            ParamValidator.validateNumericArg(MIN_AVG_ZSCORE_STR, params.get(MIN_AVG_ZSCORE_STR));
        }

        if (params.containsKey(MAX_AVG_ZSCORE_STR)) {
            ParamValidator.validateNumericArg(MAX_AVG_ZSCORE_STR, params.get(MAX_AVG_ZSCORE_STR));
        }

        if (params.containsKey(MIN_AVG_ZSCORE_STR) && params.containsKey(MAX_AVG_ZSCORE_STR)) {
            ParamValidator.validateMinLOEMax(
                    MIN_AVG_ZSCORE_STR,
                    MAX_AVG_ZSCORE_STR,
                    params.get(MIN_AVG_ZSCORE_STR),
                    params.get(MAX_AVG_ZSCORE_STR)
            );
        }
    }

    private Map<String, Float> convertStatReviewGroupParamsWithDefaults(Map<String, String> params) {
        Map<String, Float> checkedParams = new HashMap<>();
        if (!params.containsKey(MIN_REVIEW_COUNT_STR)) {
            checkedParams.put(MIN_REVIEW_COUNT_STR, 4f);
        } else {
            checkedParams.put(MIN_REVIEW_COUNT_STR, Float.parseFloat(params.get(MIN_REVIEW_COUNT_STR)));
        }
        if (!params.containsKey(MIN_AVG_SCORE_STR)) {
            checkedParams.put(MIN_AVG_SCORE_STR, 0f);
        } else {
            checkedParams.put(MIN_AVG_SCORE_STR, Float.parseFloat(params.get(MIN_AVG_SCORE_STR)));
        }
        if (!params.containsKey(MAX_AVG_SCORE_STR)) {
            checkedParams.put(MAX_AVG_SCORE_STR, 100f);
        } else {
            checkedParams.put(MAX_AVG_SCORE_STR, Float.parseFloat(params.get(MAX_AVG_SCORE_STR)));
        }
        if (!params.containsKey(MIN_AVG_ZSCORE_STR)) {
            checkedParams.put(MIN_AVG_ZSCORE_STR, -100f); // just number that won't exclude practical min zscore in data
        } else {
            checkedParams.put(MIN_AVG_ZSCORE_STR, Float.parseFloat(params.get(MIN_AVG_ZSCORE_STR)));
        }
        if (!params.containsKey(MAX_AVG_ZSCORE_STR)) {
            checkedParams.put(MAX_AVG_ZSCORE_STR, 100f); // just number that won't exclude practical max zscore in data
        } else {
            checkedParams.put(MAX_AVG_ZSCORE_STR, Float.parseFloat(params.get(MAX_AVG_ZSCORE_STR)));
        }
        return checkedParams;
    }

    public Page<StatReviewWithGameDTO> getAllStatReviewGameAggregates(Map<String, String> params, Pageable pageable) {
        logger.debug(String.format("args %s", params));

        ParamValidator.validatePagingAndSortingArgs(params, statReviewGroupSortArgs);
        ParamValidator.validateDomainRequestParams(params, statReviewGroupParams);
        this.validateStatReviewGroupParamLogicRules(params);
        Map<String, Float> convertedParams = this.convertStatReviewGroupParamsWithDefaults(params);
        return statReviewRepository.findAllStatReviewsGameAggregates(
                convertedParams.get(MIN_REVIEW_COUNT_STR),
                convertedParams.get(MIN_AVG_SCORE_STR),
                convertedParams.get(MAX_AVG_SCORE_STR),
                convertedParams.get(MIN_AVG_ZSCORE_STR),
                convertedParams.get(MAX_AVG_ZSCORE_STR),
                pageable
        );
    }
}