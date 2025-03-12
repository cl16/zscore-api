package com.zscore_api.zscore_api;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Publication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pubId;
    private String name;

    public Publication(Integer pubId, String name) {
        this.pubId = pubId;
        this.name  = name;
    }
}
