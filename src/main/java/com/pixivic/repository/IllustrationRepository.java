package com.pixivic.repository;

import com.pixivic.model.Illustration;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IllustrationRepository extends ReactiveMongoRepository<Illustration, String> {
}
