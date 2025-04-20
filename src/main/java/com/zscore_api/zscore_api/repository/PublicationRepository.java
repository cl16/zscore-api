package com.zscore_api.zscore_api.repository;

import com.zscore_api.zscore_api.entity.Publication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PublicationRepository extends CrudRepository<Publication, Integer>,
        QuerydslPredicateExecutor<Publication>,
        JpaRepository<Publication, Integer>,
        PagingAndSortingRepository<Publication, Integer> {
}
