package com.zscore_api.zscore_api;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Review {

    @EmbeddedId
    private ReviewKey id;

    @ManyToOne
    @MapsId("gameId")
    @JoinColumn(name="game_id")
    private Game game;

    @ManyToOne
    @MapsId("pubId")
    @JoinColumn(name="pub_id")
    private Publication publication;

    private Integer score;
}
