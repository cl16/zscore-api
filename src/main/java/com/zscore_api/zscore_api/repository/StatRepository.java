package com.zscore_api.zscore_api.repository;

import com.zscore_api.zscore_api.entity.Stat;
import com.zscore_api.zscore_api.key.GamePubKey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface StatRepository extends
        CrudRepository<Stat, GamePubKey>,
        PagingAndSortingRepository<Stat, GamePubKey> {

    Page<Stat> findByIdGameId(Integer gameId, Pageable pageable);
    Page<Stat> findByGameTitle(String gameTitle, Pageable pageable);
}
