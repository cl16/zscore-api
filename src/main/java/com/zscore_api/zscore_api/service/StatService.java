package com.zscore_api.zscore_api.service;

import com.zscore_api.zscore_api.entity.Stat;
import com.zscore_api.zscore_api.repository.StatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatService {

    @Autowired
    private StatRepository statRepository;

    public Iterable<Stat> getAllStats() {
        return statRepository.findAll();
    }

    public Iterable<Stat> getStatsByGameId(Integer gameId) {
        return statRepository.findByIdGameId(gameId);
    }

    public Iterable<Stat> getStatsByGameTitle(String gameTitle) {
        return statRepository.findByGameTitle(gameTitle);
    }
}
