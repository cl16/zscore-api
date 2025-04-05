package com.zscore_api.zscore_api.service;

import com.querydsl.core.BooleanBuilder;
import com.zscore_api.zscore_api.entity.QStatReview;
import com.zscore_api.zscore_api.entity.StatReview;
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

    Set<String> genericParams = new HashSet<>(Arrays.asList("page", "size", "sort"));

    Set<String> validRequestParams = new HashSet<>(Arrays.asList(
            "gameId", "pubId", "gameTitle", "gameTitleContains", "pubName", "pubNameContains",
            "scoreAbove", "scoreBelow"
    ));

    Set<String> gameDefiningParams = new HashSet<>(Arrays.asList("gameId", "gameTitle", "gameTitleContains"));
    Set<String> pubDefiningParams = new HashSet<>(Arrays.asList("pubId", "pubName", "pubNameContains"));

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
        this.checkValidRequestParams(params);

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

    private void checkValidRequestParams(Map<String, String> params) {
        Set<String> expectedParams = SetOps.union(this.genericParams, this.validRequestParams);
        if (!expectedParams.containsAll(params.keySet())) {
            throw new IllegalArgumentException("Invalid request parameters");
        }
        if (SetOps.numIntersecting(params.keySet(), this.gameDefiningParams) > 1) {
            throw new IllegalArgumentException("Invalid request parameters");
        }
        if (SetOps.numIntersecting(params.keySet(), this.pubDefiningParams) > 1) {
            throw new IllegalArgumentException("Invalid request parameters");
        }
    }
}
