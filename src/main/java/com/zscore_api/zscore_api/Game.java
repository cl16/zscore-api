package com.zscore_api.zscore_api;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Game {

    @Id
    @Column(name = "gameId")
    private Long gameId;
    private String title;

    protected Game() {}

    public Game(Long gameId, String title) {
        this.gameId = gameId;
        this.title = title;
    }

    public Long getGameId() {
        return this.gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
