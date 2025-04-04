package com.zscore_api.zscore_api.record;

public record GameAverageScore (Integer gameId, String gameTitle, Double avgScore, Long reviewCount) {
}
