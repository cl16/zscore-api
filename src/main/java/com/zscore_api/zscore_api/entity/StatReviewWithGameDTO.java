package com.zscore_api.zscore_api.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class StatReviewWithGameDTO {

    private Integer gameId;
    private String title;
    private BigDecimal averageScore;
    private BigDecimal averageZscore;
    private Long numberOfReviews;

    public StatReviewWithGameDTO(Integer gameId, String title, BigDecimal averageScore, BigDecimal averageZscore, Long numberOfReviews) {
        this.gameId = gameId;
        this.title = title;
        this.averageScore = averageScore;
        this.averageZscore = averageZscore;
        this.numberOfReviews = numberOfReviews;
    }
}
