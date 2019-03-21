package com.pixivic.repository;

import com.pixivic.model.Comment;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository  extends ReactiveMongoRepository<Comment, String> {
}
