package com.zscore_api.zscore_api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GameService {

    @Autowired
    GameRepository gameRepository;

    public Iterable<Game> getAllGames() {
        return gameRepository.findAll();
    }

    public Optional<Game> getGameById(Integer gameId) {
        return gameRepository.findById(gameId);
    }

    public Iterable<Game> getGameByTitle(String title) {
        return gameRepository.findByTitle(title);
    }
}
