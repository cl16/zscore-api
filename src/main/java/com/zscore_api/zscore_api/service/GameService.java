package com.zscore_api.zscore_api.service;

import com.zscore_api.zscore_api.repository.GameRepository;
import com.zscore_api.zscore_api.entity.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Service
public class GameService {

    @Autowired
    GameRepository gameRepository;

    Set<String> validRequestParams = new HashSet<>(Arrays.asList("title", "titleContains"));

    public Iterable<Game> getAllGames() {
        return gameRepository.findAll();
    }

    public Optional<Game> getGameById(Integer gameId) {
        return gameRepository.findById(gameId);
    }

    public Iterable<Game> getGameByTitle(String title) {
        return gameRepository.findByTitle(title);
    }

    public Iterable<Game> getGamesByParams(Map<String, String> params) throws IllegalArgumentException {
        if (!validRequestParams.containsAll(params.keySet())) {
            throw new IllegalArgumentException("Invalid request parameters");
        } else if (params.containsKey("title") && params.containsKey("titleContains")) {
            throw new IllegalArgumentException("Invalid request parameters");
        } else if (params.containsKey("title")) {
            return gameRepository.findByTitle(params.get("title"));
        } else if (params.containsKey("titleContains")) {
            return gameRepository.findByTitleContains(params.get("titleContains"));
        } else {
            throw new IllegalArgumentException("Invalid request parameters");
        }
    }
}
