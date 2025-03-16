package com.zscore_api.zscore_api.repository;

import com.zscore_api.zscore_api.entity.Stat;
import com.zscore_api.zscore_api.key.GamePubKey;
import org.springframework.data.repository.CrudRepository;

public interface StatRepository extends CrudRepository<Stat, GamePubKey> {
    Iterable<Stat> findByIdGameId(Integer gameId);
    Iterable<Stat> findByGameTitle(String gameTitle);
}
