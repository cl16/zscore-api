package com.zscore_api.zscore_api.repository;

import com.zscore_api.zscore_api.entity.Stat;
import com.zscore_api.zscore_api.key.StatKey;
import org.springframework.data.repository.CrudRepository;

public interface StatRepository extends CrudRepository<Stat, StatKey> {
    Iterable<Stat> findByIdGameId(Integer gameId);
    Iterable<Stat> findByGameTitle(String gameTitle);
}
