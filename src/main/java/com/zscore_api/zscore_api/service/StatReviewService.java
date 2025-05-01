package com.zscore_api.zscore_api.service;

import com.querydsl.core.BooleanBuilder;
import com.zscore_api.zscore_api.entity.QStatReview;
import com.zscore_api.zscore_api.entity.StatReview;
import com.zscore_api.zscore_api.entity.StatReviewWithGameDTO;
import com.zscore_api.zscore_api.helper.ParamValidator;
import com.zscore_api.zscore_api.helper.SetOps;
import com.zscore_api.zscore_api.key.GamePubKey;
import com.zscore_api.zscore_api.repository.StatReviewRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class StatReviewService {

    private static Logger logger = LogManager.getLogger(StatReviewService.class);

    @Autowired
    private StatReviewRepository statReviewRepository;

    Set<String> pagingAndSortingParams = new HashSet<>(Arrays.asList("page", "size", "sort"));

    Set<String> statReviewParams = new HashSet<>(Arrays.asList(
            "gameId", "pubId", "gameTitleContains", "pubNameContains",
            "minScore", "maxScore", "minZscore", "maxZscore"
    ));
    Set<String> statReviewSortArgs = new HashSet<>(Arrays.asList("score", "zscore"));

    Set<String> statReviewGroupParams = new HashSet<>(Arrays.asList(
            "minReviewCount", "minAvgScore", "maxAvgScore", "minAvgZscore", "maxAvgZscore"
    ));
    Set<String> statReviewGroupSortArgs = new HashSet<>(Arrays.asList("avgScore", "avgZscore"));

    public Optional<StatReview> getStatReviewById(Integer gameId, Integer pubId, Map<String, String> params) {
        ParamValidator.blockAllRequestParams(params);
        return statReviewRepository.findById(new GamePubKey(gameId, pubId));
    }

    public Iterable<StatReview> getStatReviewsByGameId(Integer gameId, Map<String, String> params, Pageable pageable) {
        ParamValidator.validatePagingAndSortingArgs(params, statReviewSortArgs);
        ParamValidator.validateDomainRequestParams(params, new HashSet<>());
        return statReviewRepository.findByIdGameId(gameId, pageable);
    }

    public Iterable<StatReview> getStatReviewsByPubId(Integer pubId, Map<String, String> params, Pageable pageable) {
        ParamValidator.validatePagingAndSortingArgs(params, statReviewSortArgs);
        ParamValidator.validateDomainRequestParams(params, new HashSet<>());
        return statReviewRepository.findByIdPubId(pubId, pageable);
    }

    public Iterable<StatReview> getStatReviewsByParams(Map<String, String> params, Pageable pageable) throws IllegalArgumentException {
        ParamValidator.validatePagingAndSortingArgs(params, statReviewSortArgs);
        ParamValidator.validateDomainRequestParams(params, statReviewParams);
        this.validateStatReviewParamLogicRules(params);

        QStatReview statReview = QStatReview.statReview;
        BooleanBuilder predicate = new BooleanBuilder();

        if (params.containsKey("gameId")) {
            predicate.and(statReview.id.gameId.eq(Integer.parseInt(params.get("gameId"))));
        }
        if (params.containsKey("pubId")) {
            predicate.and(statReview.id.pubId.eq(Integer.parseInt(params.get("pubId"))));
        }
        if (params.containsKey("gameTitleContains")) {
            predicate.and(statReview.game.title.containsIgnoreCase(params.get("gameTitleContains")));
        }
        if (params.containsKey("pubNameContains")) {
            predicate.and(statReview.publication.name.containsIgnoreCase(params.get("pubNameContains")));
        }
        if (params.containsKey("minScore")) {
            predicate.and(statReview.score.goe(Integer.parseInt(params.get("minScore"))));
        }
        if (params.containsKey("maxScore")) {
            predicate.and(statReview.score.loe(Integer.parseInt(params.get("maxScore"))));
        }
        if (params.containsKey("minZscore")) {
            predicate.and(statReview.zscore.goe(new BigDecimal(params.get("minZscore"))));
        }
        if (params.containsKey("maxZscore")) {
            predicate.and(statReview.zscore.loe(new BigDecimal(params.get("maxZscore"))));
        }

        return statReviewRepository.findAll(predicate, pageable);
    }

    private void validateStatReviewParamLogicRules(Map<String, String> params) {
        if (params.containsKey("minScore")) {
            ParamValidator.validateNumericArg("minScore", params.get("minScore"));
            ParamValidator.validateNumericRange("minScore", params.get("minScore"), 0f, 100f);
        }

        if (params.containsKey("maxScore")) {
            ParamValidator.validateNumericArg("maxScore", params.get("maxScore"));
            ParamValidator.validateNumericRange("maxScore", params.get("maxScore"), 0f, 100f);
        }

        if (params.containsKey("minScore") && params.containsKey("maxScore")) {
            ParamValidator.validateMinLOEMax(
                    "minScore",
                    "maxScore",
                    params.get("minScore"),
                    params.get("maxScore")
            );
        }

        if (params.containsKey("minZscore")) {
            ParamValidator.validateNumericArg("minZscore", params.get("minZscore"));
        }

        if (params.containsKey("maxZscore")) {
            ParamValidator.validateNumericArg("maxZscore", params.get("maxZscore"));
        }

        if (params.containsKey("minZscore") && params.containsKey("maxZscore")) {
            ParamValidator.validateMinLOEMax(
                    "minZscore",
                    "maxZscore",
                    params.get("minZscore"),
                    params.get("maxZscore")
            );
        }
    }

    private void enforceStatReviewGroupRequestParamLogicRules(Map<String, String> params) {
        // Check values are numeric
        Set<String> nonPagingAndSortingKeys = SetOps.subtract(new HashSet<>(params.keySet()), this.pagingAndSortingParams);
        for (String key : nonPagingAndSortingKeys) {
            String value = params.get(key);
            if (!value.matches("^-?[0-9]+\\.?[0-9]*$")) {
                throw new IllegalArgumentException("Request parameter value must be numeric, failed value: " + value);
            }
        }

        // Check values within accepted ranges
        if (params.containsKey("minReviewCount") && Float.parseFloat(params.get("minReviewCount")) < 0) {
            throw new IllegalArgumentException("minReviewCount must be greater than 0");
        }
        if (params.containsKey("minAvgScore")) {
            if (Float.parseFloat(params.get("minAvgScore")) < 0 || (Float.parseFloat(params.get("minAvgScore")) >= 100)) {
                throw new IllegalArgumentException("minAvgScore must be greater than or equal to 0, and less than 100");
            }
        }
        if (params.containsKey("maxAvgScore")) {
            if (Float.parseFloat(params.get("maxAvgScore")) <= 0 || Float.parseFloat(params.get("maxAvgScore")) > 100) {
                throw new IllegalArgumentException("maxAvgScore must be greater than 0, and less than or equal to 100");
            }
        }

        if (params.containsKey("minAvgScore") && params.containsKey("maxAvgScore")) {
            if (Float.parseFloat(params.get("minAvgScore")) > Float.parseFloat(params.get("maxAvgScore"))) {
                throw new IllegalArgumentException("minAvgScore argument must be <= maxAvgScore argument");
            }
        }

        if (params.containsKey("minAvgZscore") && params.containsKey("maxAvgZscore")) {
            if (Float.parseFloat(params.get("minAvgZscore")) > Float.parseFloat(params.get("maxAvgZscore"))) {
                throw new IllegalArgumentException("minAvgZscore argument must be <= maxAvgZscore argument");
            }
        }
    }

    private Map<String, Float> convertStatReviewGroupParamsWithDefaults(Map<String, String> params) {
        Map<String, Float> checkedParams = new HashMap<>();
        if (!params.containsKey("minReviewCount")) {
            checkedParams.put("minReviewCount", 4f);
        } else {
            checkedParams.put("minReviewCount", Float.parseFloat(params.get("minReviewCount")));
        }
        if (!params.containsKey("minAvgScore")) {
            checkedParams.put("minAvgScore", 0f);
        } else {
            checkedParams.put("minAvgScore", Float.parseFloat(params.get("minAvgScore")));
        }
        if (!params.containsKey("maxAvgScore")) {
            checkedParams.put("maxAvgScore", 100f);
        } else {
            checkedParams.put("maxAvgScore", Float.parseFloat(params.get("maxAvgScore")));
        }
        if (!params.containsKey("minAvgZscore")) {
            checkedParams.put("minAvgZscore", -100f); // just number that won't exclude practical min zscore in data
        } else {
            checkedParams.put("minAvgZscore", Float.parseFloat(params.get("minAvgZscore")));
        }
        if (!params.containsKey("maxAvgZscore")) {
            checkedParams.put("maxAvgZscore", 100f); // just number that won't exclude practical max zscore in data
        } else {
            checkedParams.put("maxAvgZscore", Float.parseFloat(params.get("maxAvgZscore")));
        }
        return checkedParams;
    }

    public Iterable<StatReviewWithGameDTO> getAllStatReviewGameGroupsWithAverages(Map<String, String> params, Pageable pageable) {
        logger.info("getAllStatReviewGameGroupsWithAverages params: {}", params);

        ParamValidator.validatePagingAndSortingArgs(params, statReviewGroupSortArgs);
        this.enforceStatReviewGroupRequestParamLogicRules(params);
        Map<String, Float> convertedParams = this.convertStatReviewGroupParamsWithDefaults(params);
        return statReviewRepository.findAllStatReviewsGameGroupsWithAverages(
                convertedParams.get("minReviewCount"),
                convertedParams.get("minAvgScore"),
                convertedParams.get("maxAvgScore"),
                convertedParams.get("minAvgZscore"),
                convertedParams.get("maxAvgZscore"),
                pageable
        );
    }
}