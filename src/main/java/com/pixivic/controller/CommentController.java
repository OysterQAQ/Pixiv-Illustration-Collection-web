package com.pixivic.controller;

import com.pixivic.model.Comment;
import com.pixivic.model.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CommentController {

    @PostMapping
    public Mono<ResponseEntity<Result<Comment>>> pushComment() {
        return null;
    }

    @GetMapping
    public Mono<ResponseEntity<Result<List<Comment>>>> pullComments() {
        return null;
    }

}
