package com.zscore_api.zscore_api.entity;

import com.zscore_api.zscore_api.key.StatKey;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Stat {

    @EmbeddedId
    private StatKey id;

    @ManyToOne
    @MapsId("gameId")
    @JoinColumn(name="game_id")
    private Game game;

    @ManyToOne
    @MapsId("pubId")
    @JoinColumn(name="pub_id")
    private Publication publication;

    private Float zscore;
}
