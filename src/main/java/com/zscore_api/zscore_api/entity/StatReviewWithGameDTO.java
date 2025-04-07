package com.zscore_api.zscore_api.entity;

import lombok.Data;

@Data
public class StatReviewWithGameDTO {

    private Integer gameId;
    private String title;
    private Double averageScore;
    private Double averageZscore;
    private Long numberOfReviews;

    public StatReviewWithGameDTO(Integer gameId, String title, Double averageScore, Double averageZscore, Long numberOfReviews) {
        this.gameId = gameId;
        this.title = title;
        this.averageScore = averageScore;
        this.averageZscore = averageZscore;
        this.numberOfReviews = numberOfReviews;
    }
}
