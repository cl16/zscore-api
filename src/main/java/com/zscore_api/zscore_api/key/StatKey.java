package com.zscore_api.zscore_api.key;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Data
@Embeddable
public class StatKey implements Serializable {
    private Integer gameId;
    private Integer pubId;
}
