package com.pixivic.repository;

import com.pixivic.model.Comment;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface CommentRepository  extends ReactiveMongoRepository<Comment, String> {
/*    @Query(fields = "{'pid' : 1,'id' : 1,'user' : 1,'content' : 1,'time' : 1}")
    Flux<Comment> findAll();*/
}
