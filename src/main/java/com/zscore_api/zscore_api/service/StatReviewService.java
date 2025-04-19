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

import java.util.*;

@Service
public class StatReviewService {

    private static Logger logger = LogManager.getLogger(StatReviewService.class);

    @Autowired
    private StatReviewRepository statReviewRepository;

    Set<String> pagingAndSortingParams = new HashSet<>(Arrays.asList("page", "size", "sort"));

    Set<String> statReviewParams = new HashSet<>(Arrays.asList(
            "gameId", "pubId", "gameTitle", "gameTitleContains", "pubName", "pubNameContains",
            "scoreAbove", "scoreBelow"
    ));
    Set<String> gameDefiningParams = new HashSet<>(Arrays.asList("gameId", "gameTitle", "gameTitleContains"));
    Set<String> pubDefiningParams = new HashSet<>(Arrays.asList("pubId", "pubName", "pubNameContains"));

    Set<String> statReviewGroupParams = new HashSet<>(Arrays.asList(
            "minReviewCount", "minAvgScore", "maxAvgScore", "minAvgZscore", "maxAvgZscore"
    ));
    Set<String> statReviewGroupSortArgs = new HashSet<>(Arrays.asList("avgScore", "avgZscore"));



    public Iterable<StatReview> getStatReviewById(Integer gameId, Integer pubId) throws IllegalArgumentException {
        if (gameId != null && pubId != null) {
            Optional<StatReview> result = statReviewRepository.findById(new GamePubKey(gameId, pubId));
            if (result.isPresent()) {
                return new ArrayList<>(List.of(result.get()));
            } else {
                return new ArrayList<>();
            }
        } else if (gameId != null) {
            return statReviewRepository.findByIdGameId(gameId);
        } else if (pubId != null) {
            return statReviewRepository.findByIdPubId(pubId);
        } else {
            throw new IllegalArgumentException("At least one of GameId or PubId must be provided.");
        }
    }

    public Iterable<StatReview> getStatReviewsByParams(Map<String, String> params, Pageable pageable) throws IllegalArgumentException {
        logger.info("getStatReviewsByParams params: " + params);
        this.validateParamsAgainstExpected(params, this.statReviewParams);
        this.enforceStatReviewRequestParamLogicRules(params);

        QStatReview statReview = QStatReview.statReview;
        BooleanBuilder predicate = new BooleanBuilder();

        if (params.containsKey("gameId")) {
            predicate.and(statReview.id.gameId.eq(Integer.parseInt(params.get("gameId"))));
        }
        if (params.containsKey("pubId")) {
            predicate.and(statReview.id.pubId.eq(Integer.parseInt(params.get("pubId"))));
        }
        if (params.containsKey("gameTitle")) {
            predicate.and(statReview.game.title.eq(params.get("gameTitle")));
        }
        if (params.containsKey("gameTitleContains")) {
            predicate.and(statReview.game.title.containsIgnoreCase(params.get("gameTitleContains")));
        }
        if (params.containsKey("pubName")) {
            predicate.and(statReview.publication.name.eq(params.get("pubName")));
        }
        if (params.containsKey("pubNameContains")) {
            predicate.and(statReview.publication.name.containsIgnoreCase(params.get("pubNameContains")));
        }
        if (params.containsKey("scoreAbove")) {
            predicate.and(statReview.score.gt(Integer.parseInt(params.get("scoreAbove"))));
        }
        if (params.containsKey("scoreBelow")) {
            predicate.and(statReview.score.lt((Integer.parseInt(params.get("scoreBelow")))));
        }

        return statReviewRepository.findAll(predicate, pageable);
    }

    private void validateParamsAgainstExpected(Map<String, String> params, Set<String> expected) throws IllegalArgumentException {
        Set<String> allExpected = SetOps.union(this.pagingAndSortingParams, expected);
        if (!allExpected.containsAll(params.keySet())) {
            Set<String> unexpected = SetOps.subtract(params.keySet(), allExpected);
            throw new IllegalArgumentException("Invalid request parameters: " + String.join(", ", unexpected));
        }
    }

    private void enforceStatReviewRequestParamLogicRules(Map<String, String> params) {
        if (SetOps.numIntersecting(params.keySet(), this.gameDefiningParams) > 1) {
            throw new IllegalArgumentException("Invalid request parameters");
        }
        if (SetOps.numIntersecting(params.keySet(), this.pubDefiningParams) > 1) {
            throw new IllegalArgumentException("Invalid request parameters");
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
        this.validateParamsAgainstExpected(params, this.statReviewGroupParams);
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