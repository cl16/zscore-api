package com.zscore_api.zscore_api.entity;

import com.zscore_api.zscore_api.key.GamePubKey;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
public class Stat {

    @EmbeddedId
    private GamePubKey id;

    @ManyToOne
    @MapsId("gameId")
    @JoinColumn(name="game_id")
    private Game game;

    @ManyToOne
    @MapsId("pubId")
    @JoinColumn(name="pub_id")
    private Publication publication;

    private BigDecimal zscore;
}
