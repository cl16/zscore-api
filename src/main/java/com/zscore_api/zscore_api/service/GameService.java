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

    Set<String> validRequestParams = new HashSet<>(Arrays.asList("title", "titleContains"));
    Set<String> sortArgs = new HashSet<>(Arrays.asList("gameId", "title"));

    public Iterable<Game> getAllGames(Pageable pageable) {
        return gameRepository.findAll(pageable);
    }

    public Optional<Game> getGameById(Integer gameId) {
        return gameRepository.findById(gameId);
    }

    public Iterable<Game> getGamesByParams(Map<String, String> params, Pageable pageable) {
        ParamValidator.validatePagingAndSortingArgs(params, sortArgs);
        ParamValidator.validateDomainRequestParams(params, validRequestParams);
        this.validateRequestParamLogicRules(params);

        if (params.containsKey("title")) {
            return gameRepository.findByTitle(params.get("title"), pageable);
        } else {
            return gameRepository.findByTitleContains(params.get("titleContains"), pageable);
        }
    }

    private void validateRequestParamLogicRules(Map<String, String> params) {
        Set<String> nonPagingAndSortingParams = ParamValidator.nonPagingAndSortingParams(params.keySet());
        if (nonPagingAndSortingParams.size() > 1) {
            throw new IllegalArgumentException("Invalid request parameters: only 1 allowed from title, titleContains");
        }
    }
}