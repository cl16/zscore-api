package com.zscore_api.zscore_api.service;

import com.zscore_api.zscore_api.entity.StatReview;
import com.zscore_api.zscore_api.key.GamePubKey;
import com.zscore_api.zscore_api.repository.StatReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class StatReviewService {

    @Autowired
    private StatReviewRepository statReviewRepository;

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
}
