package com.pixivic.service;

import com.pixivic.model.Comment;
import com.pixivic.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;
import reactor.core.publisher.Mono;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CommentService {
    private final CommentRepository commentRepository;

    public Mono<Comment> push(Comment comment) {
        return commentRepository.save(comment);
    }

    public Mono<Comment> findById(String id) {
        return commentRepository.findById(id);
    }

    public void init(Comment comment) {
        comment.setId(null);
        comment.setTime(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));//转义html
        comment.setEmail(HtmlUtils.htmlEscape(comment.getEmail()));//转义html
        comment.setUser(HtmlUtils.htmlEscape(comment.getUser()));//转义html
    }

    public Mono<List<Comment>> pull() {
        return commentRepository.findAll().filter(comment -> !"0".equals(comment.getId())).collectList();
    }
}
