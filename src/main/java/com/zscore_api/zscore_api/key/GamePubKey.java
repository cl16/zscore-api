package com.zscore_api.zscore_api.key;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class GamePubKey {
    private Integer gameId;
    private Integer pubId;

    public GamePubKey(Integer gameId, Integer pubId) {
        this.gameId = gameId;
        this.pubId = pubId;
    }

    public GamePubKey() {}
}
