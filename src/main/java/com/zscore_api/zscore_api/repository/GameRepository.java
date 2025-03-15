package com.zscore_api.zscore_api.repository;

import com.zscore_api.zscore_api.entity.Game;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface GameRepository extends CrudRepository<Game, Integer> {
    List<Game> findByTitle(String title);
}
