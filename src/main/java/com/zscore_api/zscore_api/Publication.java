package com.zscore_api.zscore_api;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Publication {

    @Id
    private Integer pubId;
    private String name;

    protected Publication() {}

    public Publication(Integer pubId, String name) {
        this.pubId = pubId;
        this.name  = name;
    }

    public Integer getPubId() { return this.pubId; }

    public void setPubId(Integer pubId) { this.pubId = pubId; }

    public String getName() { return this.name; }

    public void setName(String name) { this.name = name; }
}
