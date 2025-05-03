package com.zscore_api.zscore_api.service;

import com.zscore_api.zscore_api.helper.ParamValidator;
import com.zscore_api.zscore_api.repository.GameRepository;
import com.zscore_api.zscore_api.entity.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GameService {

    @Autowired
    GameRepository gameRepository;

    private final String ID_STRING = "gameId";
    private final String TITLE_STRING = "title";
    private final String TITLE_CONTAINS_STRING = "titleContains";

    Set<String> validRequestParams = new HashSet<>(Arrays.asList(TITLE_STRING, TITLE_CONTAINS_STRING));
    Set<String> sortArgs = new HashSet<>(Arrays.asList(ID_STRING, TITLE_STRING));

    public Iterable<Game> getAllGames(Map<String, String> params, Pageable pageable) {
        ParamValidator.validatePagingAndSortingArgs(params, sortArgs);
        ParamValidator.validateDomainRequestParams(params, new HashSet<>());
        return gameRepository.findAll(pageable);
    }

    public Optional<Game> getGameById(Integer gameId, Map<String, String> params) {
        ParamValidator.blockAllRequestParams(params);
        return gameRepository.findById(gameId);
    }

    public Iterable<Game> getGamesByParams(Map<String, String> params, Pageable pageable) {
        ParamValidator.validatePagingAndSortingArgs(params, sortArgs);
        ParamValidator.validateDomainRequestParams(params, validRequestParams);
        this.validateRequestParamLogicRules(params);

        if (params.containsKey(TITLE_STRING)) {
            return gameRepository.findByTitle(params.get(TITLE_STRING), pageable);
        } else {
            return gameRepository.findByTitleContains(params.get(TITLE_CONTAINS_STRING), pageable);
        }
    }

    private void validateRequestParamLogicRules(Map<String, String> params) {
        Set<String> nonPagingAndSortingParams = ParamValidator.nonPagingAndSortingParams(params.keySet());
        if (nonPagingAndSortingParams.size() > 1) {
            throw new IllegalArgumentException(
                    String.format(
                            "Invalid request parameters: only 1 allowed from %s, %s",
                            TITLE_STRING,
                            TITLE_CONTAINS_STRING
                    )
            );
        }
    }
}