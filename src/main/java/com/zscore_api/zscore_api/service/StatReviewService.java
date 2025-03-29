package com.zscore_api.zscore_api.service;

import com.zscore_api.zscore_api.entity.StatReview;
import com.zscore_api.zscore_api.key.GamePubKey;
import com.zscore_api.zscore_api.repository.StatReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StatReviewService {

    @Autowired
    private StatReviewRepository statReviewRepository;

    public Optional<StatReview> getStatReviewById(Integer gameId, Integer pubId) {
        return statReviewRepository.findById(new GamePubKey(gameId, pubId));
    }
}
