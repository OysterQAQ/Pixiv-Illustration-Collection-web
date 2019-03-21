package com.pixivic.repository;

import com.pixivic.model.Rank;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RankRepository extends ReactiveMongoRepository<Rank, String> {
}
