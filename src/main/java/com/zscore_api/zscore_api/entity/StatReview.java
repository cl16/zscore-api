package com.zscore_api.zscore_api.entity;

import com.zscore_api.zscore_api.key.GamePubKey;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
@Table(name = "review")
@SecondaryTable(name = "stat")
public class StatReview {

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

    @Column(table = "review")
    private Date date;

    @Column(table = "review")
    private Integer score;

    @Column(table = "stat")
    private BigDecimal zscore;
}
