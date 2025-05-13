package com.zscore_api.zscore_api.service;

import com.zscore_api.zscore_api.entity.Stat;
import com.zscore_api.zscore_api.repository.StatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class StatService {

    @Autowired
    private StatRepository statRepository;

    public Page<Stat> getAllStats(Pageable pageable) {
        return statRepository.findAll(pageable);
    }

    public Page<Stat> getStatsByGameId(Integer gameId, Pageable pageable) {
        return statRepository.findByIdGameId(gameId, pageable);
    }

    public Page<Stat> getStatsByGameTitle(String gameTitle, Pageable pageable) {
        return statRepository.findByGameTitle(gameTitle, pageable);
    }
}
